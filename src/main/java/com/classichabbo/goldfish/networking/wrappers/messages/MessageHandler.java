package com.classichabbo.goldfish.networking.wrappers.messages;

import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.client.views.types.club.ClubView;

import java.util.List;
import java.util.Map;

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
