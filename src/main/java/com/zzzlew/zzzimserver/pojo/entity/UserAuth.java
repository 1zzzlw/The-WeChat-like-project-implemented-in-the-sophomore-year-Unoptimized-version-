package com.zzzlew.zzzimserver.pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:13
 * @Description: com.zzzlew.zzzimserver.pojo.entity
 * @version: 1.0
 */
@Builder
@Data
public class UserAuth implements Serializable {

    private Long id;

    private Long userId;

    private String username;

    private String account;

    private String password;

    private String avatar;

    private LocalDateTime create_time;

    private LocalDateTime update_time;
}
