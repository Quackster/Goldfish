package com.classichabbo.goldfish.networking.wrappers.messages;

public class MessageRegistered {
    private final Class<? extends MessageHandler> handlerClass;
    private final Integer header;
    private final MessageRequest message;

    public MessageRegistered(Class<? extends MessageHandler> handlerClass, Integer header, MessageRequest delegate) {
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
