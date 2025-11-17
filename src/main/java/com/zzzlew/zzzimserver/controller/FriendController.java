package com.zzzlew.zzzimserver.controller;

import com.zzzlew.zzzimserver.pojo.vo.friend.FriendRelationVO;
import com.zzzlew.zzzimserver.pojo.vo.user.UserSearchVO;
import com.zzzlew.zzzimserver.result.Result;
import com.zzzlew.zzzimserver.server.FriendService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/12 - 11 - 12 - 22:51
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Resource
    private FriendService friendService;

    /**
     * 获取好友列表
     *  TODO 优化的时候改成分页查询吧
     * @return 好友列表
     */
    @GetMapping("/list")
    public Result<List<FriendRelationVO>> getFriendList() {
        log.info("获取好友列表");
        List<FriendRelationVO> friendRelationVOList = friendService.getFriendList();
        return Result.success(friendRelationVOList);
    }

    @GetMapping("/search")
    public Result<UserSearchVO> search(String phone) {
        log.info("搜索用户 {} 的信息", phone);
        UserSearchVO userSearchVO = friendService.search(phone);
        return Result.success(userSearchVO);
    }

}
