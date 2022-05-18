package com.classichabbo.goldfish.client.game.scheduler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Scheduler implements Runnable {
    private List<GameUpdate> updatesRequired;
    private Thread gameLoop;
    private boolean isRunning;

    public Scheduler() {
        this.updatesRequired = new CopyOnWriteArrayList<>();

        this.isRunning = true;
        this.gameLoop = new Thread(this);
        this.gameLoop.start();
    }

    public void receiveUpdate(GameUpdate gameUpdate) {
        if (!this.updatesRequired.contains(gameUpdate)) {
            this.updatesRequired.add(gameUpdate);
        }
    }

    public void removeUpdate(GameUpdate gameUpdate) {
        if (this.updatesRequired.contains(gameUpdate)) {
            this.updatesRequired.remove(gameUpdate);
        }
    }

    public List<GameUpdate> getUpdatesRequired() {
        return updatesRequired;
    }

    public Thread getThread() {
        return gameLoop;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
