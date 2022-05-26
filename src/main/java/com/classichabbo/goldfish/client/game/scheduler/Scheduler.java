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
    }

    public void restartThread() {
        // If the loop is running, set the loop to stop running and wait
        // for the thread to die by using join();
        if (this.gameLoop != null) {
            try {
                this.isRunning = false;
                this.gameLoop.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
