package com.zzzlew.zzzimserver.pojo.dto.message;

import com.zzzlew.zzzimserver.pojo.Message;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 16:16
 * @Description: com.zzzlew.zzzimserver.pojo.dto.message
 * @version: 1.0
 */
public class GroupChatRequestDTO extends Message implements Serializable {



    @Override
    public int getMessageType() {
        return GroupChatRequestDTO;
    }
}
