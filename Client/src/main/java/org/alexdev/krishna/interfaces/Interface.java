package org.alexdev.krishna.interfaces;

import java.util.Collections;
import java.util.stream.Collectors;

import javafx.application.Platform;
import org.alexdev.krishna.Movie;

import javafx.scene.layout.Pane;
import org.alexdev.krishna.game.scheduler.GameUpdate;
import org.alexdev.krishna.visualisers.Visualiser;

public abstract class Interface extends Pane implements GameUpdate {
    public abstract void start();
    public abstract void stop();

    public void remove() {
        Movie.getInstance().removeObject(this);
    }

    public abstract void update();
    public abstract InterfaceType getType();

    /**
     * Called when the visualiser changes.
     *
     * @param oldVisualiser the previous visualiser, if NULL then the {@currentVisualiser} is the first visualiser this interface appears in
     * @param newVisualiser the current visualiser
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
