package com.classichabbo.goldfish.networking.wrappers.messages.types;

import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;

public class MessageListener<T extends View> {
    private final Class<? extends MessageHandler> handlerClass;
    private final Integer header;
    private final MessageRequest message;
    private final View view;

    public MessageListener(Class<? extends MessageHandler> handlerClass, Integer header, MessageRequest delegate, View view) {
        this.handlerClass = handlerClass;
        this.header = header;
        this.message = delegate;
        this.view = view;
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

    public View getView() {
        return view;
    }
}
