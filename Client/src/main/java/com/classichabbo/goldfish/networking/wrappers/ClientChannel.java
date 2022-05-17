package com.classichabbo.goldfish.networking.wrappers;

import io.netty.channel.Channel;

public class ClientChannel {
    private final Channel channel;

    public ClientChannel(Channel channel) {
        this.channel = channel;
    }
}
