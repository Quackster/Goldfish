package com.classichabbo.goldfish.networking.wrappers.messages;

import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;

public interface MessageRequest {
    void received(Connection channel, Request request);
}
