package com.zzzlew.zzzimserver.server.impl;

import cn.hutool.core.bean.BeanUtil;
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

        // 保存消息到数据库
        messageMapper.saveMessage(messageDTO);

        MessageVO messageVO = BeanUtil.copyProperties(messageDTO, MessageVO.class);
        messageVO.setSendTime(LocalDateTime.now());
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
