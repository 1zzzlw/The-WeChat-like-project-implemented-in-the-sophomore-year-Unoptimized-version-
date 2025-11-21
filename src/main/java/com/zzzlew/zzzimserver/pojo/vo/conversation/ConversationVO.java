package com.zzzlew.zzzimserver.pojo.vo.conversation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/21 - 11 - 21 - 21:30
 * @Description: com.zzzlew.zzzimserver.pojo.vo.conversation
 * @version: 1.0
 */
@Data
public class ConversationVO {
    /**
     * 会话ID
     */
    private String id;

    /**
     * 会话类型
     */
    private Integer conversationType;

    /**
     * 关联ID
     */
    private Long relatedId;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 未读消息数量
     */
    private Integer unreadCount;

    /**
     * 最后一条消息
     */
    private String latestMsg;

    /**
     * 最后一条消息时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestMsgTime;

    /**
     * 会话状态
     */
    private Integer status;
}
