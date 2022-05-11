package org.alexdev.krishna.visualisers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.util.DimensionUtil;

public abstract class Visualiser {
    public abstract boolean isReady();
    public abstract void init();
    public abstract void update();

    public abstract Component getComponent();
    public abstract Scene getScene();
}
