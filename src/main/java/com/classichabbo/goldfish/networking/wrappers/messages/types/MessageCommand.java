package com.classichabbo.goldfish.networking.wrappers.messages.types;

import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;

public class MessageCommand {
    private final Class<? extends MessageHandler> handlerClass;
    private final Integer header;
    private final String headerName;

    public MessageCommand(Class<? extends MessageHandler> handlerClass, Integer header, String headerName) {
        this.handlerClass = handlerClass;
        this.header = header;
        this.headerName = headerName;
    }

    public Class<? extends MessageHandler> getHandlerClass() {
        return handlerClass;
    }

    public Integer getHeader() {
        return header;
    }

    public String getHeaderName() {
        return headerName;
    }
}
