package com.zzzlew.zzzimserver.server.impl;

import com.zzzlew.zzzimserver.mapper.ApplyMapper;
import com.zzzlew.zzzimserver.mapper.FriendMapper;
import com.zzzlew.zzzimserver.pojo.dto.apply.DealApplyDTO;
import com.zzzlew.zzzimserver.pojo.dto.apply.SendApplyDTO;
import com.zzzlew.zzzimserver.pojo.vo.apply.ApplyVO;
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

            // 插入好友关系表
            friendMapper.addFriendToRelation(toUserId, fromUserId);
            friendMapper.addFriendToRelation(fromUserId, toUserId);
        }
    }

}
