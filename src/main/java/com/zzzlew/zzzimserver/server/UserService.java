package com.zzzlew.zzzimserver.server;

import com.zzzlew.zzzimserver.pojo.dto.user.UserLoginDTO;
import com.zzzlew.zzzimserver.pojo.dto.user.UserRegisterDTO;
import com.zzzlew.zzzimserver.pojo.vo.user.UserLoginVO;
import jakarta.servlet.http.HttpServletResponse;


/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:08
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface UserService {

    UserLoginVO login(UserLoginDTO userLoginDTO);

    void createCode(HttpServletResponse response);

    String register(UserRegisterDTO userRegisterDTO);

    String createPhoneCode(String phone);

    void pendingLogin(String token);

    String refreshToken();

}
