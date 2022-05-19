package com.classichabbo.goldfish.networking.wrappers.messages;

import com.classichabbo.goldfish.networking.wrappers.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;

public interface MessageRequest {
    void received(Connection channel, Request request);
}
