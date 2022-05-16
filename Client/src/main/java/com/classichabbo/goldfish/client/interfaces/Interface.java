package com.classichabbo.goldfish.client.interfaces;

import java.util.Collections;
import java.util.stream.Collectors;

import com.classichabbo.goldfish.client.game.scheduler.GameUpdate;
import com.classichabbo.goldfish.client.visualisers.Visualiser;
import com.classichabbo.goldfish.client.Movie;
import javafx.application.Platform;

import javafx.scene.layout.Pane;

public abstract class Interface extends Pane implements GameUpdate {
    public abstract void start();
    public abstract void stop();

    public void remove() {
        Movie.getInstance().removeObject(this);
    }

    public abstract void update();

    /**
     * Called when the visualiser changes.
     *
     * @param previousVisualiser the previous visualiser, if NULL then the {@currentVisualiser} is the first visualiser this interface appears in
     * @param currentVisualiser the current visualiser
     */
    public void visualiserChanged(Visualiser previousVisualiser, Visualiser currentVisualiser) { }

    @Override
    public void toFront() {
        Platform.runLater(() -> {
            var viewOrderValues = Movie.getInstance()
                    .getCurrentVisualiser()
                    .getPane()
                    .getChildren()
                    .stream()
                    //.filter(c -> c != this.getPane())
                    .map(c -> c.getViewOrder())
                    .collect(Collectors.toList());

            this.setViewOrder(Collections.min(viewOrderValues) - 1);
        });
    }

    @Override
    public void toBack() {
        Platform.runLater(() -> {
            var viewOrderValues = Movie.getInstance()
                    .getCurrentVisualiser()
                    .getPane()
                    .getChildren()
                    .stream()
                    //.filter(c -> c != this.getPane())
                    .map(c -> c.getViewOrder())
                    .collect(Collectors.toList());

            this.setViewOrder(Collections.max(viewOrderValues) + 1);
        });
    }
}
