package com.zzzlew.zzzimserver.controller;

import com.zzzlew.zzzimserver.pojo.vo.conversation.ConversationVO;
import com.zzzlew.zzzimserver.result.Result;
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
     * @param conversationVO 会话信息
     * @return 创建的会话信息
     */
    @PostMapping("/create")
    public Result<ConversationVO> createGroupConversation(@RequestBody ConversationVO conversationVO) {
        log.info("创建群聊：{}", conversationVO);
        ConversationVO createdConversationVO = conversationService.createGroupConversation(conversationVO);
        return Result.success(createdConversationVO);
    }

}
