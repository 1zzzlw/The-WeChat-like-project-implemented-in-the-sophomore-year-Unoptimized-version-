package com.zzzlew.zzzimserver.server.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.zzzlew.zzzimserver.config.KaptchaConfig;
import com.zzzlew.zzzimserver.constant.JwtClaimsConstant;
import com.zzzlew.zzzimserver.constant.MessageConstant;
import com.zzzlew.zzzimserver.exception.*;
import com.zzzlew.zzzimserver.mapper.UserInfoMapper;
import com.zzzlew.zzzimserver.mapper.UserMapper;
import com.zzzlew.zzzimserver.pojo.dto.user.UserBaseDTO;
import com.zzzlew.zzzimserver.pojo.dto.user.UserLoginDTO;
import com.zzzlew.zzzimserver.pojo.dto.user.UserRegisterDTO;
import com.zzzlew.zzzimserver.pojo.entity.UserAuth;
import com.zzzlew.zzzimserver.pojo.entity.UserInfo;
import com.zzzlew.zzzimserver.pojo.vo.user.UserLoginVO;
import com.zzzlew.zzzimserver.properties.Jwtproperties;
import com.zzzlew.zzzimserver.server.UserService;
import com.zzzlew.zzzimserver.utils.JwtUtil;
import com.zzzlew.zzzimserver.utils.RegexUtils;
import com.zzzlew.zzzimserver.utils.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zzzlew.zzzimserver.constant.RedisConstant.*;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:08
 * @Description: com.zzzlew.zzzimserver.server.impl
 * @version: 1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private Jwtproperties jwtproperties;
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        String password = userLoginDTO.getPassword();
        String verifyCode = userLoginDTO.getVerifyCode();

        UserAuth userAuth = userMapper.getByUsername(account);

        if (userAuth == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // TODO 密码解密处理
        if (!password.equals(userAuth.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 从redis中获取验证码，并进行判断
        String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY);
        if (code == null || !code.equals(verifyCode)) {
            throw new PasswordErrorException(MessageConstant.VERIFYCODE_ERROR);
        }

        // 生成并存储token
        UserLoginVO userLoginVO = generateAndStoreWithUpdateToken(userAuth);

        return userLoginVO;
    }

    /**
     * 处理用户登录界面获取验证码的操作
     * 
     * @param response HttpServletResponse对象
     */
    @Override
    public void createCode(HttpServletResponse response) {
        // 生成验证码文本
        String code = KaptchaConfig.kaptchaProducer().createText();
        // 生成验证码图片
        BufferedImage captchaImage = KaptchaConfig.kaptchaProducer().createImage(code);

        // 将验证码信息存入到redis中
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        log.info("生成的验证为：{}", code);

        // 设置响应内容类型，告诉浏览器“这次返回的是 JPEG 图片”
        response.setContentType("image/jpeg");

        // 禁止浏览器缓存验证码（避免刷新后还是旧图片）
        response.setHeader("Cache-Control", "no-store, no-cache");
        // 兼容老浏览器
        response.setHeader("Pragma", "no-cache");

        // 获取字节输出流，向客户端发送数据，把 BufferedImage（内存图片）转成二进制流，写进响应
        try (OutputStream os = response.getOutputStream()) {
            // ImageIO.write：把图片写入输出流，格式是 jpeg
            ImageIO.write(captchaImage, "jpeg", os);
            // 确保流里的数据全发送给前端
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理用户注册时点击注册的操作
     * 
     * @param userRegisterDTO 用户注册信息
     */
    @Transactional
    @Override
    public String register(UserRegisterDTO userRegisterDTO) {
        // 随机生成一个不会重复的账号
        long timestamp = System.currentTimeMillis();
        String randomStr = RandomUtil.randomString(4);
        int randomNum = new Random().nextInt(90) + 10;
        String account = timestamp + randomStr + randomNum;

        userRegisterDTO.setAccount(account);

        UserInfo userInfo = BeanUtil.copyProperties(userRegisterDTO, UserInfo.class);

        userInfoMapper.insertUserInfo(userInfo);

        // 获取insert语句生成的用户详细信息表中的主键
        Long userId = userInfo.getId();

        UserAuth userAuth = BeanUtil.copyProperties(userRegisterDTO, UserAuth.class);
        userAuth.setUserId(userId);
        userMapper.insertUserAuth(userAuth);
        log.info("主要用户信息：{}", userAuth);
        // 生成并存储用户登录信息在redis中
        UserLoginVO userLoginVO = generateAndStoreWithUpdateToken(userAuth);
        return userLoginVO.getToken();
    }

    /**
     * 处理用户注册时点击获取验证码的操作
     * 
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public String createPhoneCode(String phone) {
        // 校验手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            throw new PhoneErrorException(MessageConstant.PHONE_ERROR);
        }

        // 查询数据库，看手机号是否存在
        // TODO 后期可以加上redis缓存，避免频繁查询数据库
        boolean exists = userInfoMapper.getByPhone(phone);
        if (exists) {
            throw new PhoneAlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
        }

        // 随机生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 将验证码存入到redis中
        String codeKey = REGISTER_CODE_KEY + phone;
        stringRedisTemplate.opsForValue().set(codeKey, code, REGISTER_CODE_TTL, TimeUnit.MINUTES);
        log.info("随机生成的验证码为：{}", code);
        return code;
    }

    /**
     * 处理待登录界面点击确认登录的操作
     *
     * @param token 登录时生成的token
     */
    @Override
    public void pendingLogin(String token) {
        // 校验token是否存在
        String tokenInfoKey = LOGIN_USER_KEY + token;
        // 判断token是否过期
        if (jwtUtil.isTokenExpired(jwtproperties.getSecretKey(), token)) {
            throw new TokenExpiredException(MessageConstant.TOKEN_EXPIRED);
        }
        Boolean hasKey = stringRedisTemplate.hasKey(tokenInfoKey);
        if (!hasKey) {
            throw new TokenNotFoundException(MessageConstant.TOKEN_NOT_FOUND);
        }
    }

    @Override
    public String refreshToken() {
        // 拿到当前登录用户的信息
        UserBaseDTO user = UserHolder.getUser();
        UserAuth userAuth = UserAuth.builder().userId(user.getId()).username(user.getUsername())
            .account(user.getAccount()).avatar(user.getAvatar()).build();
        UserLoginVO userLoginVO = generateAndStoreWithUpdateToken(userAuth);
        return userLoginVO.getToken();
    }

    public UserLoginVO generateAndStoreWithUpdateToken(UserAuth userAuth) {
        // 生成Token令牌并返回
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userAuth.getUserId());
        String token = jwtUtil.createJWT(jwtproperties.getSecretKey(), jwtproperties.getExpiration(), claims);

        Long userId = userAuth.getUserId();
        log.info("当前登录用户id：{}", userId);
        String userKey = LOGIN_USER_TOKEN_LIST_KEY + userId;
        // 首先检查该用户id下是否有已登录的token
        Set<String> oldTokens = stringRedisTemplate.opsForSet().members(userKey);
        if (oldTokens != null && !oldTokens.isEmpty()) {
            // // 遍历集合，拿到每个旧token，或者直接批量处理
            // for (String oldToken : oldTokens) {
            // // 删除单个旧token对应的用户信息（token为key的hash）
            // stringRedisTemplate.delete(LOGIN_USER_KEY + oldToken);
            // }
            // 将旧Token转换为对应的用户信息key集合
            // 遍历删除可能影响效率，考虑批量删除
            Collection<String> oldTokenInfoKeys =
                oldTokens.stream().map(oldToken -> LOGIN_USER_KEY + oldToken).collect(Collectors.toList());
            // 批量删除（一次请求搞定）
            stringRedisTemplate.delete(oldTokenInfoKeys);
            // 再删除这个Set集合本身（旧token列表）
            stringRedisTemplate.delete(userKey);
        }

        // 存储userId为键的所有用户的token信息
        stringRedisTemplate.opsForSet().add(userKey, token);
        // 设置有效期
        stringRedisTemplate.expire(userKey, LOGIN_USER_TOKEN_LIST_KEY_TTL, TimeUnit.MINUTES);

        UserLoginVO userLoginVO = UserLoginVO.builder().id(userAuth.getUserId()).username(userAuth.getUsername())
            .token(token).avatar(userAuth.getAvatar()).onLine(1).account(userAuth.getAccount()).build();

        // 将用户信息转为map集合
        Map<String, Object> map =
            BeanUtil.beanToMap(userLoginVO, new HashMap<>(), CopyOptions.create().setIgnoreNullValue(true)
                .setIgnoreProperties("token").setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        // redis中键的格式为：login:user:token
        String tokenKey = LOGIN_USER_KEY + token;

        // 将用户信息存储到redis中
        stringRedisTemplate.opsForHash().putAll(tokenKey, map);
        // 设置有效期
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_KEY_TTL, TimeUnit.MINUTES);

        return userLoginVO;
    }
}
