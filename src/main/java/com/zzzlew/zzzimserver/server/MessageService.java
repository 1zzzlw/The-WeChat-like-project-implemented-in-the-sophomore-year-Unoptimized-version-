package com.zzzlew.zzzimserver.server;

import com.zzzlew.zzzimserver.pojo.dto.message.MessageDTO;
import com.zzzlew.zzzimserver.pojo.vo.message.MessageVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/15 - 11 - 15 - 23:52
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface MessageService {

    MessageVO sendMessage(MessageDTO messageDTO);

    List<MessageVO> getMessageList(Long receiverId);

}
