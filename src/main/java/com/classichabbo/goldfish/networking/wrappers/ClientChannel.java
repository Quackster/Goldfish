package com.classichabbo.goldfish.networking.wrappers;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.networking.util.StringUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;

public class ClientChannel {
    private final Channel channel;

    public ClientChannel(Channel channel) {
        this.channel = channel;
    }


    public void send(String tCmd, Object... data) {
        var messageCommand = Movie.getInstance().getCommands().stream()
                .filter(x -> x.getHeaderName().equals(tCmd))
                .findFirst().orElse(null);//.map(x -> x.getHeader()).findFirst().orElse(null);

        if (messageCommand == null) {
            System.out.println("Command '" + tCmd + "' not found");
            return;
        }

        var header = messageCommand.getHeader();
        this.channel.writeAndFlush(new Command(header, data));
    }
}
