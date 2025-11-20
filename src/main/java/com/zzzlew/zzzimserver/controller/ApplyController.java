package com.zzzlew.zzzimserver.controller;

import com.zzzlew.zzzimserver.pojo.dto.apply.DealApplyDTO;
import com.zzzlew.zzzimserver.pojo.dto.apply.SendApplyDTO;
import com.zzzlew.zzzimserver.pojo.vo.apply.ApplyVO;
import com.zzzlew.zzzimserver.result.Result;
import com.zzzlew.zzzimserver.server.ApplyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/14 - 11 - 14 - 22:33
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Resource
    private ApplyService applyService;

    /**
     * 发送好友申请
     * 
     * @param sendApplyDTO 好友申请信息
     */
    @PostMapping("/send")
    public Result<Long> sendApply(@RequestBody SendApplyDTO sendApplyDTO) {
        log.info("添加好友，申请信息：{}", sendApplyDTO);
        Long applyId = applyService.sendApply(sendApplyDTO);
        return Result.success(applyId);
    }

    /**
     * 获取好友申请发送历史
     * 
     * @return 好友申请发送历史
     */
    @GetMapping("/sendHistory")
    public Result<Object> getSendHistory() {
        return Result.success();
    }

    /**
     * 获取好友申请列表
     * 
     * @return 好友申请列表
     */
    @GetMapping("/list")
    public Result<List<ApplyVO>> getApplyList() {
        List<ApplyVO> applyList = applyService.getApplyList();
        return Result.success(applyList);
    }

    /**
     * 处理好友申请
     * 
     * @param dealApplyDTO 好友申请处理信息
     */
    @PostMapping("/deal")
    public Result<Object> dealApply(@RequestBody DealApplyDTO dealApplyDTO) {
        log.info("处理好友申请，申请信息为：{}", dealApplyDTO);
        applyService.dealApply(dealApplyDTO);
        return Result.success();
    }

}
