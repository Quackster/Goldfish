package com.classichabbo.goldfish.client.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import com.classichabbo.goldfish.client.game.scheduler.GameUpdate;
import com.classichabbo.goldfish.client.Movie;
import javafx.application.Platform;

import javafx.scene.layout.Pane;

public class Interface extends Pane implements GameUpdate {
    private boolean isHidden;

    public void start() { }
    public void stop() { }
    public void update() { }

    public void remove() {
        Movie.getInstance().removeObject(this);
    }

    @Override
    public void toFront() {
        Platform.runLater(() -> {
            var viewOrderValues = new ArrayList<Double>();

            for (var ui : Movie.getInstance().getInterfaces()) {
                viewOrderValues.add(ui.getViewOrder());

                for (var child : ui.getChildren()) {
                    viewOrderValues.add(child.getViewOrder());
                }

            }

            this.setViewOrder(Collections.min(viewOrderValues) - 1);
        });
    }

    @Override
    public void toBack() {
        Platform.runLater(() -> {
            var viewOrderValues = new ArrayList<Double>();

            for (var ui : Movie.getInstance().getInterfaces()) {
                viewOrderValues.add(ui.getViewOrder());

                for (var child : ui.getChildren()) {
                    viewOrderValues.add(child.getViewOrder());
                }

            }

            this.setViewOrder(Collections.max(viewOrderValues) + 1);
        });
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
        this.setVisible(isHidden);
    }

    public void toggleVisibility() {
        this.setHidden(!this.isHidden);
    }

    public boolean isHidden() {
        return isHidden;
    }
}
