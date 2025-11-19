package com.zzzlew.zzzimserver.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.zzzlew.zzzimserver.server.WebSocketService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 15:15
 * @Description: com.zzzlew.zzzimserver.websocket
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ConnectSuccessMessageHandler extends ChannelInboundHandlerAdapter {

    private WebSocketService webSocketService;

    // 需要手动注入 WebSocketService，不能使用 @Autowired 注解，因为 NettyWebSocketServerHandler 是手动创建的，
    // 而不是由 Spring 管理的，并且 handler不能注入到spring 容器中，所以需要手动注入 WebSocketService
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (this.webSocketService == null) {
            this.webSocketService = SpringUtil.getBean(WebSocketService.class);
        }
    }

    /**
     * 通道就绪后调用，一般用来初始化
     *
     * @param ctx 通道处理器上下文（包含当前Channel信息）
     * @throws Exception 处理过程中的异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端 {} 建立连接，通道开启！", ctx.channel().remoteAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("ws协议正式搭建完成，通道成功开启，客户端 {} 握手完成！", ctx.channel().remoteAddress());
            webSocketService.online(ctx.channel());
        }
    }
}
