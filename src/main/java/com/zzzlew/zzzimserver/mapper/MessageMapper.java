package com.zzzlew.zzzimserver.mapper;

import com.zzzlew.zzzimserver.pojo.dto.message.MessageDTO;
import com.zzzlew.zzzimserver.pojo.vo.message.MessageVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/15 - 11 - 15 - 23:53
 * @Description: com.zzzlew.zzzimserver.mapper
 * @version: 1.0
 */
public interface MessageMapper {

    /**
     * 保存消息
     * 
     * @param messageDTO 消息DTO
     */
    void saveMessage(MessageDTO messageDTO);

    List<MessageVO> getMessageList(String conversationId);

}
