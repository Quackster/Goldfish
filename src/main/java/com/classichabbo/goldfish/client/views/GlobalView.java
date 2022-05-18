package com.classichabbo.goldfish.client.views;

import com.classichabbo.goldfish.client.handlers.GlobalHandler;

public class GlobalView extends View {
    private final GlobalHandler handler;

    public GlobalView() {
        this.handler = new GlobalHandler();
    }

    @Override
    public void start() {

    }

    @Override
    public GlobalHandler getHandler() {
        return handler;
    }
}