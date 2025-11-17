package com.zzzlew.zzzimserver.pojo.dto.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/15 - 11 - 15 - 23:50
 * @Description: com.zzzlew.zzzimserver.pojo.dto.message
 * @version: 1.0
 */
@Data
public class MessageDTO implements Serializable {

    private String conversationId;

    private Long senderId;

    private Long receiverId;

    private Integer msgType;

    private String content;
}
