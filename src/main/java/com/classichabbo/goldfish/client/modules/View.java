package com.classichabbo.goldfish.client.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.classichabbo.goldfish.client.game.scheduler.GameUpdate;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import javafx.application.Platform;

import javafx.scene.layout.Pane;

public class View extends Pane implements GameUpdate {
    private View owner;
    private List<View> children;
    private boolean isHidden;

    public View() {
        this.children = new ArrayList<>();
    }

    public void start() { }
    public void stop() { }
    public void registerUpdate() { }
    public void removeUpdate() { }
    public void update() { }
    public void remove() {
        Movie.getInstance().removeObject(this);
    }

    public MessageHandler getHandler() { return null; }
    public Component getComponent() { return null; }

    @Override
    public void toFront() {
        Platform.runLater(() -> {
            var viewOrderValues = new ArrayList<Double>();

            for (var ui : Movie.getInstance().getViews()) {
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

            for (var ui : Movie.getInstance().getViews()) {
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
        this.setVisible(!isHidden); // isVisible is the inverse of isHidden, so use ! to flip it
    }

    public void toggleVisibility() {
        this.setHidden(!this.isHidden);
    }

    public boolean isHidden() {
        return isHidden;
    }

    public View getOwner() {
        return owner;
    }

    public void setOwner(View owner) {
        this.owner = owner;
    }
}
