package com.classichabbo.goldfish.networking.codec;

import com.classichabbo.goldfish.networking.wrappers.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.UnsupportedEncodingException;
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

        out.add(new Request(content));
    }
}