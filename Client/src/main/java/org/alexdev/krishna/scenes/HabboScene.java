package org.alexdev.krishna.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.alexdev.krishna.Krishna;
import org.alexdev.krishna.util.DimensionUtil;

public abstract class HabboScene {
    protected static Scene create(Pane pane) {
        return new Scene(pane, DimensionUtil.getProgramWidth(), DimensionUtil.getProgramHeight(), Color.BLACK);
    }

    public abstract boolean isReady();
    public abstract void init();

    public abstract void updateTick();
    public abstract void renderTick();

    public abstract Scene getScene();
}
