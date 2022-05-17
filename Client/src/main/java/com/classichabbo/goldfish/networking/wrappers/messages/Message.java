package com.classichabbo.goldfish.networking.wrappers.messages;

import com.classichabbo.goldfish.networking.wrappers.Request;
import io.netty.channel.Channel;

public interface Message {
    void received(Channel channel, Request request);
}
