package com.zzzlew.zzzimserver.server.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zzzlew.zzzimserver.mapper.ConversationMapper;
import com.zzzlew.zzzimserver.mapper.MessageMapper;
import com.zzzlew.zzzimserver.pojo.dto.message.MessageDTO;
import com.zzzlew.zzzimserver.pojo.vo.message.MessageVO;
import com.zzzlew.zzzimserver.server.MessageService;
import com.zzzlew.zzzimserver.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/15 - 11 - 15 - 23:52
 * @Description: com.zzzlew.zzzimserver.server.impl
 * @version: 1.0
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private ConversationMapper conversationMapper;

    @Override
    public MessageVO sendMessage(MessageDTO messageDTO) {
        // 获取当前登录用户id
        Long userId = UserHolder.getUser().getId();
        messageDTO.setSenderId(userId);
        Long receiverId = messageDTO.getReceiverId();

        // String conversationId = userId > receiverId ? userId + "_" + receiverId : receiverId + "_" + userId;

        String conversationId = userId > receiverId ? String.format("%d_%d", userId, receiverId)
            : String.format("%d_%d", receiverId, userId);

        messageDTO.setConversationId(conversationId);

        messageDTO.setMsgType(1);

        MessageVO messageVO = BeanUtil.copyProperties(messageDTO, MessageVO.class);
        LocalDateTime sendTime = LocalDateTime.now();
        messageVO.setSendTime(sendTime);

        // 保存消息到数据库
        messageMapper.saveMessage(messageDTO);

        // 修改会话列表中的状态，未读消息数量，最后一条消息时间，最后一条消息内容，以及显示状态。
        conversationMapper.updateConversationStatus(conversationId, messageDTO.getContent(), sendTime, receiverId);

        return messageVO;
    }

    @Override
    public List<MessageVO> getMessageList(Long receiverId) {
        // 获取当前登录用户id
        Long userId = UserHolder.getUser().getId();
        // 构建 conversationId
        String conversationId = userId > receiverId ? String.format("%d_%d", userId, receiverId)
            : String.format("%d_%d", receiverId, userId);
        // 查询数据库中的消息
        List<MessageVO> messageVOList = messageMapper.getMessageList(conversationId);
        // 返回消息列表
        return messageVOList;
    }
}
