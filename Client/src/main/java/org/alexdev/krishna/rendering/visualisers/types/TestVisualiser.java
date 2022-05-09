package org.alexdev.krishna.rendering.visualisers.types;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.alexdev.krishna.rendering.visualisers.Visualiser;

public class TestVisualiser extends Visualiser {
    private Pane pane;
    private Scene scene;

    private boolean isInitialised;

    @Override
    public void init() {
        if (!this.isInitialised)
            return;

        this.pane = new Pane();
        this.scene = Visualiser.create(this.pane);

        this.isInitialised = true;
    }

    @Override
    public void update() {

    }

    @Override
    public Scene getScene() {
        return null;
    }

    @Override
    public boolean isReady() {
        return this.isInitialised;
    }
}
