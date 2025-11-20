package com.zzzlew.zzzimserver.controller;

import com.zzzlew.zzzimserver.pojo.dto.user.UserRegisterDTO;
import com.zzzlew.zzzimserver.result.Result;
import com.zzzlew.zzzimserver.server.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:07
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("注册用户信息为 {}", userRegisterDTO);
        String token = userService.register(userRegisterDTO);
        return Result.success(token);
    }

    @PostMapping("/phoneCode")
    public Result<String> createCode(@RequestParam("phone") String phone) {
        log.info("创建手机号 {} 的验证码", phone);
        String phoneCode = userService.createPhoneCode(phone);
        return Result.success(phoneCode);
    }

}
