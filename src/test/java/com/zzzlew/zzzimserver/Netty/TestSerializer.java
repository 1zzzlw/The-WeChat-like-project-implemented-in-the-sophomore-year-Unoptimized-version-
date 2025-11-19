package com.zzzlew.zzzimserver.Netty;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.zzzlew.zzzimserver.pojo.Message;
import com.zzzlew.zzzimserver.pojo.dto.message.PrivateChatRequestDTO;
import com.zzzlew.zzzimserver.properties.SerializerProperties;
import com.zzzlew.zzzimserver.protocol.MessageCodecSharable;
import com.zzzlew.zzzimserver.protocol.Serializer;

import cn.hutool.extra.spring.SpringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 19:50
 * @Description: com.zzzlew.zzzimserver.Netty
 * @version: 1.0
 */
@SpringBootTest
public class TestSerializer {

    // 序列化测试
    @Test
    public void testSerializer() {
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(loggingHandler, messageCodec, loggingHandler);

        PrivateChatRequestDTO privateChatRequestDTO = new PrivateChatRequestDTO();
        privateChatRequestDTO.setConversationId("123");
        privateChatRequestDTO.setSenderId(1L);
        privateChatRequestDTO.setReceiverId(2L);
        privateChatRequestDTO.setMsgType(1);
        privateChatRequestDTO.setContent("hello");
        // 写入消息到通道, 出站，进入编码器，进行序列化
        channel.writeOutbound(privateChatRequestDTO);
        // 触发消息编码和日志打印
        channel.flush();
        channel.finish();
    }

    @Test
    public void testDeserialize() {
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(loggingHandler, messageCodec, loggingHandler);

        PrivateChatRequestDTO privateChatRequestDTO = new PrivateChatRequestDTO();
        privateChatRequestDTO.setConversationId("123");
        privateChatRequestDTO.setSenderId(1L);
        privateChatRequestDTO.setReceiverId(2L);
        privateChatRequestDTO.setMsgType(1);
        privateChatRequestDTO.setContent("hello");

        // 将消息类型序列化才能进行反序列化
        ByteBuf byteBuf = messageToByteBuf(privateChatRequestDTO);
        // 写入消息到通道, 入站，进入解码器，进行反序列化
        channel.writeInbound(byteBuf);
        // 触发消息解码和日志打印
        channel.flush();
        channel.finish();
    }

    public static ByteBuf messageToByteBuf(Message msg) {
        int algorithm = SpringUtil.getBean(SerializerProperties.class).getAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[] {1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }

}
