package org.alexdev.krishna.interfaces;

import java.util.Collections;
import java.util.stream.Collectors;

import org.alexdev.krishna.HabboClient;

import javafx.scene.layout.Pane;
import org.alexdev.krishna.game.scheduler.GameUpdate;

public abstract class Interface extends GameUpdate {
    public abstract void start();
    public abstract void stop();

    public abstract void sceneChanged();
    public abstract void update();
    public abstract Pane getPane();
    public abstract InterfaceType getType();

    public void toFront() {
        var viewOrderValues = HabboClient.getInstance()
            .getCurrentVisualiser()
            .getPane()
            .getChildren()
            .stream()
            .map(c -> c.getViewOrder())
            .collect(Collectors.toList());
            
        this.getPane().setViewOrder(Collections.min(viewOrderValues) - 1);
    }
}
