package com.zzzlew.zzzimserver.server.impl;

import cn.hutool.core.util.IdUtil;
import com.zzzlew.zzzimserver.mapper.ApplyMapper;
import com.zzzlew.zzzimserver.mapper.ConversationMapper;
import com.zzzlew.zzzimserver.mapper.FriendMapper;
import com.zzzlew.zzzimserver.pojo.dto.apply.DealApplyDTO;
import com.zzzlew.zzzimserver.pojo.dto.apply.DealGroupDTO;
import com.zzzlew.zzzimserver.pojo.dto.apply.GroupApplyDTO;
import com.zzzlew.zzzimserver.pojo.dto.apply.SendApplyDTO;
import com.zzzlew.zzzimserver.pojo.dto.conversation.GroupConversationDTO;
import com.zzzlew.zzzimserver.pojo.dto.user.GroupMemberDTO;
import com.zzzlew.zzzimserver.pojo.vo.apply.ApplyVO;
import com.zzzlew.zzzimserver.pojo.vo.apply.GroupApplyVO;
import com.zzzlew.zzzimserver.server.ApplyService;
import com.zzzlew.zzzimserver.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/14 - 11 - 14 - 22:35
 * @Description: com.zzzlew.zzzimserver.server.impl
 * @version: 1.0
 */
@Slf4j
@Service
public class ApplyServiceImpl implements ApplyService {

    @Resource
    private ApplyMapper applyMapper;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private ConversationMapper conversationMapper;

    @Override
    public Long sendApply(SendApplyDTO sendApplyDTO) {
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();
        log.info("当前登录用户id：{}", userId);
        sendApplyDTO.setFromUserId(userId);
        applyMapper.sendApply(sendApplyDTO);
        // 打印好友申请id
        log.info("好友申请表id: {}", sendApplyDTO.getApplyId());
        return sendApplyDTO.getApplyId();
    }

    @Override
    public List<ApplyVO> getApplyList() {
        // 获得当前登录用户的id
        Long userId = UserHolder.getUser().getId();
        // 根据id查询对应的好友列表
        List<ApplyVO> list = applyMapper.getApplyList(userId);
        log.info("好友申请列表: {}", list);
        return list;
    }

    @Transactional
    @Override
    public void dealApply(DealApplyDTO dealApplyDTO) {
        LocalDateTime dealTime = LocalDateTime.now();
        dealApplyDTO.setDealTime(dealTime);
        applyMapper.dealApply(dealApplyDTO);
        // 如果同意好友申请，需要添加好友到好友列表
        if (dealApplyDTO.getDealResult() == 1) {
            // TODO 增加 “幂等性处理”（避免重复添加好友）
            // 获得当前登录用户id
            Long toUserId = UserHolder.getUser().getId();
            Long fromUserId = dealApplyDTO.getFromUserId();

            // TODO 同意添加好友，调用发送信息接口，就是发送一个打招呼信息

            // 插入好友关系表
            friendMapper.addFriendToRelation(toUserId, fromUserId);
            friendMapper.addFriendToRelation(fromUserId, toUserId);
        }
    }

    /**
     * 发送群聊申请
     *
     * @param friendIdList 好友ID列表
     * @param groupApplyDTO 群聊申请信息
     */
    @Transactional
    @Override
    public String createGroupConversation(List<Long> friendIdList, GroupApplyDTO groupApplyDTO) {
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();
        // 获得用户头像
        String avatar = UserHolder.getUser().getAvatar();
        groupApplyDTO.setUserAvatar(avatar);
        // 发送群聊申请之后，就直接创建群聊的会话表
        long snowflakeId = IdUtil.getSnowflakeNextId();
        String conversationId = "g_" + snowflakeId;
        groupApplyDTO.setConversationId(conversationId);
        // 插入群聊表
        applyMapper.sendGroupApply(userId, friendIdList, groupApplyDTO);

        // 插入群聊会话表
        GroupConversationDTO groupConversationDTO = new GroupConversationDTO();
        groupConversationDTO.setId(conversationId);
        groupConversationDTO.setGroupName(groupApplyDTO.getGroupName());
        // TODO 这里就先不使用头像了吧，群聊头像的设计还没想好
        groupConversationDTO.setGroupAvatar("E:\\JavaWeb\\zzz-IM-web\\image\\group.jpg");
        groupConversationDTO.setOwnerId(userId);

        conversationMapper.insertGroupConversation(groupConversationDTO);

        // 插入群成员表
        GroupMemberDTO groupMemberDTO = new GroupMemberDTO();
        groupMemberDTO.setGroupId(conversationId);
        groupMemberDTO.setUserId(userId);
        groupMemberDTO.setRole(2);
        applyMapper.insertGroupMember(groupMemberDTO);

        return conversationId;
    }

    @Override
    public List<GroupApplyVO> getGroupApplyList() {
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();
        // 根据id查询对应的群聊申请列表
        List<GroupApplyVO> list = applyMapper.getGroupApplyList(userId);
        log.info("群聊申请列表: {}", list);
        return list;
    }

    @Transactional
    @Override
    public void dealGroupApply(DealGroupDTO dealGroupDTO) {
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();
        // 修改群聊申请状态
        applyMapper.dealGroupApply(dealGroupDTO);
        if (dealGroupDTO.getStatus() == 2) {
            // 同意入群申请，需要插入群成员表
            GroupMemberDTO groupMemberDTO = new GroupMemberDTO();
            groupMemberDTO.setGroupId(dealGroupDTO.getConversationId());
            groupMemberDTO.setUserId(dealGroupDTO.getMemberId());
            groupMemberDTO.setRole(0);
            applyMapper.insertGroupMember(groupMemberDTO);
            // 更新群聊会话表的群成员数量
            conversationMapper.updateGroupMemberCount(dealGroupDTO.getConversationId());
        } else {
            log.info("用户id：{}拒绝入群申请", userId);
        }
    }

}
