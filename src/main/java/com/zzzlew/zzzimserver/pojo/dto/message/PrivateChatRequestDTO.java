package com.zzzlew.zzzimserver.pojo.dto.message;

import com.zzzlew.zzzimserver.pojo.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 16:12
 * @Description: com.zzzlew.zzzimserver.pojo.dto.message
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PrivateChatRequestDTO extends Message implements Serializable {

    private String conversationId;

    private Long senderId;

    private Long receiverId;

    /**
     * 消息类型 1：文本消息 2：图片消息 3：语音消息 4：视频消息 5：文件消息
     */
    private Integer msgType;

    private String content;

    @Override
    public int getMessageType() {
        return PrivateChatRequestDTO;
    }

}
