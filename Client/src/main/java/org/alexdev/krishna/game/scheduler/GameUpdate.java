package org.alexdev.krishna.game.scheduler;

public abstract class GameUpdate {
    private boolean isReady;

    public abstract void update();

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
