package com.classichabbo.goldfish.networking.wrappers.messages.types;

import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;

public class MessageListener {
    private final Class<? extends MessageHandler> handlerClass;
    private final Integer header;
    private final MessageRequest message;

    public MessageListener(Class<? extends MessageHandler> handlerClass, Integer header, MessageRequest delegate) {
        this.handlerClass = handlerClass;
        this.header = header;
        this.message = delegate;
    }

    public Class<? extends MessageHandler> getHandlerClass() {
        return handlerClass;
    }

    public Integer getHeader() {
        return header;
    }

    public MessageRequest getMessage() {
        return message;
    }
}
