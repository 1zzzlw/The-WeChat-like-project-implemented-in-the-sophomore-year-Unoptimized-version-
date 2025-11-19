package com.zzzlew.zzzimserver.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.zzzlew.zzzimserver.server.WebSocketService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 16:23
 * @Description: com.zzzlew.zzzimserver.handler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitLoginHandler extends ChannelInboundHandlerAdapter {

    private WebSocketService webSocketService;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (this.webSocketService == null) {
            this.webSocketService = SpringUtil.getBean(WebSocketService.class);
        }
    }

    /**
     * 当Channel处理过程中发生异常时触发（如IO异常、消息解析失败等）。 此处采用简单策略：关闭异常通道，避免异常扩散影响其他连接。
     *
     * @param ctx 通道处理器上下文
     * @param cause 异常原因
     * @throws Exception 处理过程中的异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常关闭通道
        log.error("发生异常，通道关闭！", cause);
        ctx.channel().close();
    }

    /**
     * 当Handler从Channel的Pipeline中移除时触发（通常在连接断开后）。 主要用于清理当前Handler与Channel的绑定关系，补充处理用户离线逻辑。
     *
     * @param ctx 通道处理器上下文（包含当前Channel信息）
     * @throws Exception 处理过程中的异常
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        offLine(ctx);
    }

    /**
     * 处理用户离线逻辑的私有方法，统一封装离线时的资源清理、状态更新操作。 调用webSocketService.offline()更新用户在线状态、移除Channel映射等。
     *
     * @param ctx 通道处理器上下文
     */
    private void offLine(ChannelHandlerContext ctx) {
        webSocketService.offline(ctx.channel());
        ctx.channel().close();
    }

    /**
     * 当TCP连接断开（Channel从活跃状态变为非活跃状态）时触发。 触发场景包括：客户端主动关闭连接、网络中断、服务端主动关闭等。 主要用于最终确认连接断开，并执行离线清理。
     *
     * @param ctx 通道处理器上下文
     * @throws Exception 处理过程中的异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端 {} 断开连接，通道关闭！", ctx.channel().remoteAddress());
        offLine(ctx);
    }

}
