package com.zzzlew.zzzimserver.server;

import com.zzzlew.zzzimserver.pojo.dto.apply.DealApplyDTO;
import com.zzzlew.zzzimserver.pojo.dto.apply.SendApplyDTO;
import com.zzzlew.zzzimserver.pojo.vo.apply.ApplyVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/14 - 11 - 14 - 22:34
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface ApplyService {

    /**
     * 发送好友申请
     * 
     * @param sendApplyDTO 好友申请信息
     */
    void sendApply(SendApplyDTO sendApplyDTO);

    /**
     * 获取好友申请列表
     *
     * @return 好友申请列表
     */
    List<ApplyVO> getApplyList();

    /**
     * 同意好友申请
     * 
     * @param dealApplyDTO 好友申请信息
     */
    void dealApply(DealApplyDTO dealApplyDTO);

}
