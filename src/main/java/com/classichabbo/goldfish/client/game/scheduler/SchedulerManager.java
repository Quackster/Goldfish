package com.classichabbo.goldfish.client.game.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SchedulerManager {
    private static SchedulerManager instance;

    public SchedulerManager() {

    }

    public Thread asyncCallback(Runnable run) {
        var thread = new Thread(run);
        thread.start();
        return thread;
    }

    public static SchedulerManager getInstance() {
        if (instance == null) {
            instance = new SchedulerManager();
        }

        return instance;
    }
}
