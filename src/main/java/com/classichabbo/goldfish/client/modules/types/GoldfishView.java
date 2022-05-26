package com.classichabbo.goldfish.client.modules.types;

import com.classichabbo.goldfish.client.modules.GoldfishHandler;
import com.classichabbo.goldfish.client.modules.View;

public class GoldfishView extends View {
    private final GoldfishHandler handler;
    private final GoldfishComponent component;

    public GoldfishView() {
        this.handler = new GoldfishHandler(this);
        this.component = new GoldfishComponent(this);
    }

    @Override
    public void start() {

    }

    @Override
    public GoldfishHandler getHandler() {
        return handler;
    }

    @Override
    public GoldfishComponent getComponent() { return component; }
}