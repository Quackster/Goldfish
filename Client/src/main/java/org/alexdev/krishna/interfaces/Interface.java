package org.alexdev.krishna.interfaces;

import javafx.scene.layout.Pane;

public abstract class Interface extends Pane {
    public abstract boolean isReady();
    public abstract void init();
    public abstract void sceneChanged();
    public abstract void update();
    public abstract InterfaceType getType();
}
