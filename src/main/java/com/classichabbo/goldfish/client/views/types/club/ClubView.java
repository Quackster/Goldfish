package com.classichabbo.goldfish.client.views.types.club;

import com.classichabbo.goldfish.client.components.types.ClubComponent;
import com.classichabbo.goldfish.client.handlers.ClubHandler;
import com.classichabbo.goldfish.client.views.View;

public class ClubView extends View {
    private final ClubComponent component;
    private final ClubHandler handler;

    public ClubView() {
        this.handler = new ClubHandler(this);
        this.component = new ClubComponent(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void registerUpdate() {

    }

    @Override
    public void removeUpdate() {

    }

    @Override
    public void update() {

    }

    @Override
    public ClubHandler getHandler() {
        return handler;
    }

    @Override
    public ClubComponent getComponent() {
        return component;
    }
}