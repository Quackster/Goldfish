package com.classichabbo.goldfish.networking.wrappers.messages;

import com.classichabbo.goldfish.client.modules.View;

public abstract class MessageHandler {
    private final View view;

    public MessageHandler(View view) {
        this.view = view;
    }

    public abstract void regMsgList(boolean tBool);

    public View getView() {
        return view;
    }
}
