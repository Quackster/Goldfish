package org.alexdev.krishna.visualisers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.util.DimensionUtil;

public abstract class Visualiser {
    protected static Scene create(Pane pane) {
        pane.getChildren().addAll(HabboClient.getInstance().getInterfaces());
        return new Scene(pane, DimensionUtil.getProgramWidth(), DimensionUtil.getProgramHeight(), Color.BLACK);
    }

    public abstract boolean isReady();
    public abstract void init();
    public abstract void update();

    public abstract Scene getScene();
}
