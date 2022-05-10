package org.alexdev.krishna.visualisers.types.room;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.alexdev.krishna.visualisers.Component;
import org.alexdev.krishna.visualisers.Visualiser;

public class RoomVisualiser extends Visualiser {
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
    public Component getComponent() {
        return null;
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
