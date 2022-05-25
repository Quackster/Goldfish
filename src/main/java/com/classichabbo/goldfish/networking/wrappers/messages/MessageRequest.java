package com.classichabbo.goldfish.networking.wrappers.messages;

import com.classichabbo.goldfish.networking.ChannelConnection;
import com.classichabbo.goldfish.networking.wrappers.Request;

public interface MessageRequest {
    void received(ChannelConnection channel, Request request);
}
