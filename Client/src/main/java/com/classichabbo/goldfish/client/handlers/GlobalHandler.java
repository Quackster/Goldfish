package com.classichabbo.goldfish.client.handlers;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import io.netty.channel.Channel;

import java.util.HashMap;

public class GlobalHandler extends MessageHandler {
    private static void handleHello(Channel channel, Request request) {
        System.out.println("Packet handler");
    }

    @Override
    public void regMsgList(boolean tBool) {
        var incomingHandlers = new HashMap<Integer, MessageRequest>();
        incomingHandlers.put(0, GlobalHandler::handleHello);

        if (tBool) {
            Movie.getInstance().registerListeners(this, incomingHandlers);
        } else {
            Movie.getInstance().unregisterListeners(this, incomingHandlers);
        }
    }
}
