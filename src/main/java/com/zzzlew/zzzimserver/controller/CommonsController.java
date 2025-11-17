package com.zzzlew.zzzimserver.controller;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/17 - 11 - 17 - 20:07
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */

import com.zzzlew.zzzimserver.result.Result;
import com.zzzlew.zzzimserver.server.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用控制器类
 */
@RestController
@RequestMapping("/commons")
public class CommonsController {

    @Resource
    private UserService userService;

    @PostMapping("/refreshToken")
    public Result<String> refreshToken() {
        String token = userService.refreshToken();
        return Result.success(token);
    }

}
