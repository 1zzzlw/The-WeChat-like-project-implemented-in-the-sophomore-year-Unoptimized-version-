package com.zzzlew.zzzimserver.server;

import com.zzzlew.zzzimserver.pojo.vo.friend.FriendRelationVO;
import com.zzzlew.zzzimserver.pojo.vo.user.UserSearchVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/12 - 11 - 12 - 23:06
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface FriendService {

    /**
     * 获取好友列表
     * 
     * @return 好友列表
     */
    List<FriendRelationVO> getFriendList();

    /**
     * 搜索用户
     * 
     * @param phone 手机号
     * @return 用户搜索vo
     */
    UserSearchVO search(String phone);

}
