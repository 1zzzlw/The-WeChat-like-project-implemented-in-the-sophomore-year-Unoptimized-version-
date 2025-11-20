package com.zzzlew.zzzimserver.handler.messageHandler;

import com.zzzlew.zzzimserver.pojo.dto.message.GroupChatRequestDTO;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 21:25
 * @Description: com.zzzlew.zzzimserver.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupChatHandler extends SimpleChannelInboundHandler<GroupChatRequestDTO> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestDTO groupChatRequestDTO)
        throws Exception {

    }

}
