package com.classichabbo.goldfish.networking.codec;

import com.classichabbo.goldfish.networking.encoding.Base64Encoding;
import com.classichabbo.goldfish.networking.streams.NettyRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

public class NetworkDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws UnsupportedEncodingException {
        var content = ctx.alloc().buffer();

        while (buffer.readableBytes() > 0) {
            var character = buffer.readByte();

            if (character <= 1) {
               break;
            }

            content.writeByte(character);
        }

        out.add(new NettyRequest(content));
    }
}