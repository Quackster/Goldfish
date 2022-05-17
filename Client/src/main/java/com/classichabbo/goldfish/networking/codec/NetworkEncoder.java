package com.classichabbo.goldfish.networking.codec;

import com.classichabbo.goldfish.networking.encoding.Base64Encoding;
import com.classichabbo.goldfish.networking.encoding.VL64Encoding;
import com.classichabbo.goldfish.networking.util.StringUtil;
import com.classichabbo.goldfish.networking.wrappers.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class NetworkEncoder extends MessageToMessageEncoder<Command> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Command command, List<Object> out) throws Exception {
        var message = ctx.alloc().buffer();
        message.writeBytes("@@@".getBytes(StringUtil.getCharset()));
        message.writeBytes(Base64Encoding.encode(command.getHeader(), 2));

        for (var obj : command.getData()) {
            if (obj instanceof Integer) {
                message.writeBytes(VL64Encoding.encode((int) obj));
            }

            if (obj instanceof String) {
                message.writeBytes(Base64Encoding.encode(obj.toString().length(), 2));
                message.writeBytes(obj.toString().getBytes(StringUtil.getCharset()));
            }
        }

        message.setBytes(1, Base64Encoding.encode(message.writerIndex() - 3, 2));
        out.add(message);
    }
}