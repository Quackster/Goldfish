package com.classichabbo.goldfish.client.modules.types.club;

import com.classichabbo.goldfish.client.modules.View;

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