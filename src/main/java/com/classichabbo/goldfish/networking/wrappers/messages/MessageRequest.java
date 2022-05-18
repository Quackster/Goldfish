package com.classichabbo.goldfish.networking.wrappers.messages;

import com.classichabbo.goldfish.networking.wrappers.ClientChannel;
import com.classichabbo.goldfish.networking.wrappers.Request;
import io.netty.channel.Channel;

public interface MessageRequest {
    void received(ClientChannel channel, Request request);
}
