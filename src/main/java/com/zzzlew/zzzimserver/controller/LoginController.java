package com.zzzlew.zzzimserver.controller;

import org.springframework.web.bind.annotation.*;

import com.zzzlew.zzzimserver.pojo.dto.user.UserLoginDTO;
import com.zzzlew.zzzimserver.pojo.vo.user.UserLoginVO;
import com.zzzlew.zzzimserver.result.Result;
import com.zzzlew.zzzimserver.server.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:37
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private UserService userService;

    @PostMapping
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("当前登录用户信息：{}", userLoginDTO);

        UserLoginVO userLoginVO = userService.login(userLoginDTO);

        log.info("登录成功，当前登录用户信息：{}", userLoginVO);

        return Result.success(userLoginVO);
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletResponse response) {
        log.info("随机生成登录验证码");
        userService.createCode(response);
    }

    @GetMapping("/pendingLogin")
    public Result<Object> pendingLogin(@RequestParam("token") String token) {
        log.info("用户 {} 正在登录", token);
        userService.pendingLogin(token);
        return Result.success();
    }
}
