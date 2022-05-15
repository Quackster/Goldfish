package com.classichabbo.goldfish.client.visualisers;

import com.classichabbo.goldfish.client.game.scheduler.GameUpdate;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public abstract class Visualiser implements GameUpdate {
    public abstract void start();
    public abstract void stop();

    public abstract void update();

    public abstract Component getComponent();
    public abstract Pane getPane();
    public abstract Scene getScene();
    public abstract VisualiserType getType();

    @Override
    public String toString() {
        return getType().name();
    }
}
