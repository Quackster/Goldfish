package com.classichabbo.goldfish.client.views.types;

import com.classichabbo.goldfish.client.handlers.GoldfishHandler;
import com.classichabbo.goldfish.client.views.View;

public class GoldfishView extends View {
    private final GoldfishHandler handler;

    public GoldfishView() {
        this.handler = new GoldfishHandler();
    }

    @Override
    public void start() {

    }

    @Override
    public GoldfishHandler getHandler() {
        return handler;
    }
}