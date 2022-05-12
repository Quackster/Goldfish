package org.alexdev.krishna.interfaces;

import java.util.Collections;
import java.util.stream.Collectors;

import org.alexdev.krishna.HabboClient;

import javafx.scene.layout.Pane;

public abstract class Interface extends Pane {
    public abstract boolean isReady();
    public abstract void init();
    public abstract void sceneChanged();
    public abstract void update();
    public abstract InterfaceType getType();

    @Override
    public void toFront() {
        var viewOrderValues = HabboClient.getInstance()
            .getCurrentVisualiser()
            .getPane()
            .getChildren()
            .stream()
            .map(c -> c.getViewOrder())
            .collect(Collectors.toList());
            
        this.setViewOrder(Collections.min(viewOrderValues) - 1);
    }
}
