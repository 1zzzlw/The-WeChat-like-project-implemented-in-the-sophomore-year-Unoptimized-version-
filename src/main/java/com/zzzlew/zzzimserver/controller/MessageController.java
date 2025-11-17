package com.zzzlew.zzzimserver.controller;

import com.zzzlew.zzzimserver.pojo.dto.message.MessageDTO;
import com.zzzlew.zzzimserver.pojo.vo.message.MessageVO;
import com.zzzlew.zzzimserver.result.Result;
import com.zzzlew.zzzimserver.server.MessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/15 - 11 - 15 - 22:41
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * 发送消息
     * 
     * @return 消息id
     */
    @PostMapping("/send")
    public Result<MessageVO> sendMessage(@RequestBody MessageDTO messageDTO) {
        log.info("发送消息：{}", messageDTO);
        MessageVO messageVO = messageService.sendMessage(messageDTO);
        return Result.success(messageVO);
    }

    /**
     * 获取消息列表
     *
     * @return 消息列表
     */
    @GetMapping("/list")
    public Result<List<MessageVO>> getMessageList(@RequestParam Long receiverId) {
        log.info("接收者id为：{}", receiverId);
        List<MessageVO> messageVOList = messageService.getMessageList(receiverId);
        log.info("消息列表：{}", messageVOList);
        return Result.success(messageVOList);
    }

}
