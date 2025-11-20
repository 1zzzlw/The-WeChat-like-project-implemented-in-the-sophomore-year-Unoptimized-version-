package com.zzzlew.zzzimserver.pojo.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzzlew.zzzimserver.pojo.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 22:46
 * @Description: com.zzzlew.zzzimserver.pojo.vo.message
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateChatResponseVO extends Message implements Serializable {

    private String conversationId;

    private Long senderId;

    private Long receiverId;

    /**
     * 消息类型 1：文本消息 2：图片消息 3：语音消息 4：视频消息 5：文件消息
     */
    private Integer msgType;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @Override
    public int getMessageType() {
        return PrivateChatResponseVO;
    }
}
