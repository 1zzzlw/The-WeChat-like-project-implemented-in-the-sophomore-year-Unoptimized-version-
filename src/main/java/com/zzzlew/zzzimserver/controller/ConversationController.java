package com.zzzlew.zzzimserver.controller;

import com.zzzlew.zzzimserver.pojo.dto.apply.GroupApplyDTO;
import com.zzzlew.zzzimserver.pojo.vo.apply.GroupApplyVO;
import com.zzzlew.zzzimserver.pojo.vo.conversation.ConversationVO;
import com.zzzlew.zzzimserver.result.Result;
import com.zzzlew.zzzimserver.server.ApplyService;
import com.zzzlew.zzzimserver.server.ConversationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/21 - 11 - 21 - 21:00
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Resource
    private ConversationService conversationService;
    @Resource
    private ApplyService applyService;

    /**
     * 获取会话列表
     *
     * @return 会话列表
     */
    @GetMapping("/list/{conversationId}")
    public Result<List<ConversationVO>> getConversationList(@PathVariable String conversationId) {
        List<String> conversationIdList = Arrays.asList(conversationId.split(","));
        log.info("会话ID列表：{}", conversationIdList);
        List<ConversationVO> conversationVOList = conversationService.getConversationList(conversationIdList);
        return Result.success(conversationVOList);
    }

    /**
     * 创建群聊
     *
     * @param friendId 好友ID
     * @param groupApplyDTO 群聊申请信息
     * @return 创建的会话信息
     */
    @PostMapping("/create/{friendId}")
    public Result<Object> createGroupConversation(@PathVariable String friendId,
        @RequestBody GroupApplyDTO groupApplyDTO) {
        log.info("创建群聊：{}，群聊名称：{}", friendId, groupApplyDTO.getGroupName());
        List<Long> friendIdList = Arrays.stream(friendId.split(",")).map(Long::valueOf).toList();
        log.info("好友ID列表：{}", friendIdList);
        applyService.createGroupConversation(friendIdList, groupApplyDTO);
        return Result.success();
    }

    /**
     * 获取群聊申请列表
     *
     * @return 群聊申请列表
     */
    @GetMapping("/groupApplyList")
    public Result<List<GroupApplyVO>> getGroupApplyList() {
        List<GroupApplyVO> groupApplyVOList = applyService.getGroupApplyList();
        return Result.success(groupApplyVOList);
    }

    /**
     * 同意群聊申请
     *
     * @param groupApplyDTO 群聊申请信息
     */
    @PostMapping("/groupApply/agree")
    public Result<Object> agreeGroupApply(@RequestBody GroupApplyDTO groupApplyDTO) {
        log.info("同意群聊申请：{}", groupApplyDTO);
        // applyService.dealGroupApply(groupApplyDTO);
        return Result.success();
    }

}