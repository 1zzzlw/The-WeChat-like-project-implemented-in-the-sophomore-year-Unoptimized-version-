package com.zzzlew.zzzimserver.server.impl;

import com.zzzlew.zzzimserver.mapper.FriendMapper;
import com.zzzlew.zzzimserver.mapper.UserInfoMapper;
import com.zzzlew.zzzimserver.pojo.dto.user.UserBaseDTO;
import com.zzzlew.zzzimserver.pojo.vo.friend.FriendRelationVO;
import com.zzzlew.zzzimserver.pojo.vo.user.UserSearchVO;
import com.zzzlew.zzzimserver.server.FriendService;
import com.zzzlew.zzzimserver.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/12 - 11 - 12 - 23:06
 * @Description: com.zzzlew.zzzimserver.server.impl
 * @version: 1.0
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendMapper friendMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public List<FriendRelationVO> getFriendList() {
        // 获得当前登录用户的信息
        UserBaseDTO userBaseDTO = UserHolder.getUser();
        // 获取当前用户id
        Long userId = userBaseDTO.getId();
        log.info("当前用户id: {}", userId);
        // 根据用户id查询该用户的好友列表
        List<FriendRelationVO> friendRelationVOList = friendMapper.selectFriendList(userId);
        // 打印好友列表
        log.info("好友列表: {}", friendRelationVOList);
        // 返回好友列表
        return friendRelationVOList;
    }

    @Override
    public UserSearchVO search(String phone) {
        // 获得当前登录用户的信息，以便查询和好友的状态关系
        Long userId = UserHolder.getUser().getId();

        // 查询用户信息表，根据手机号或账号查询用户信息
        UserSearchVO userSearchVO = userInfoMapper.getByPhoneOrAccount(userId, phone);
        // 打印查询到的用户信息
        log.info("userSearchVO: {}", userSearchVO);
        if (userSearchVO.getIsFriend() == 0) {
            // 不是好友关系
            log.info("不是好友关系");
        } else {
            // 是好友关系
            log.info("是好友关系");
        }
        return userSearchVO;
    }

}
