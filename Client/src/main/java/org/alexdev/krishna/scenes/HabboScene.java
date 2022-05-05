package org.alexdev.krishna.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.alexdev.krishna.Krishna;

public abstract class HabboScene {
    private Scene scene;

    protected static Scene create(Pane pane) {
        return new Scene(pane, Krishna.getClient().getPrimaryStage().getWidth(), Krishna.getClient().getPrimaryStage().getMaxHeight(), Color.BLACK);
    }

    public abstract void init();
    public abstract void renderTick();

    public abstract Scene getScene();
}
