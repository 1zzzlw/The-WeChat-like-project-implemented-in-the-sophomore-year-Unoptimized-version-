package com.zzzlew.zzzimserver.mapper;

import com.zzzlew.zzzimserver.pojo.dto.apply.DealApplyDTO;
import com.zzzlew.zzzimserver.pojo.dto.apply.SendApplyDTO;
import com.zzzlew.zzzimserver.pojo.vo.apply.ApplyVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/14 - 11 - 14 - 22:24
 * @Description: com.zzzlew.zzzimserver.mapper
 * @version: 1.0
 */
public interface ApplyMapper {

    /**
     * 发送好友申请
     * 
     * @param sendApplyDTO 好友申请信息
     */
    void sendApply(SendApplyDTO sendApplyDTO);

    /**
     * 获取好友申请列表
     * 
     * @param userId 用户id
     * @return 好友申请列表
     */
    List<ApplyVO> getApplyList(Long userId);

    /**
     * 同意好友申请
     * 
     * @param dealApplyDTO 好友申请处理信息
     */
    void dealApply(DealApplyDTO dealApplyDTO);
}
