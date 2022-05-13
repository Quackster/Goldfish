package org.alexdev.krishna.visualisers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.scheduler.GameUpdate;
import org.alexdev.krishna.util.DimensionUtil;

public abstract class Visualiser extends GameUpdate {
    public abstract void start();
    public abstract void stop();

    public abstract void update();

    public abstract Component getComponent();
    public abstract Pane getPane();
    public abstract Scene getScene();
}
