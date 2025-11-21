package com.zzzlew.zzzimserver.server;

import com.zzzlew.zzzimserver.pojo.vo.conversation.ConversationVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/21 - 11 - 21 - 21:40
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface ConversationService {

    List<ConversationVO> getConversationList(List<String> conversationIdList);

    ConversationVO createGroupConversation(ConversationVO conversationVO);

}
