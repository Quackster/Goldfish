package org.alexdev.krishna.visualisers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import org.alexdev.krishna.game.scheduler.GameUpdate;

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
