package org.alexdev.krishna.game.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class SchedulerManager {
    private static SchedulerManager instance;
    private final ScheduledExecutorService scheduledPool;
    private final ExecutorService cachedPool;

    public SchedulerManager() {
        this.scheduledPool =  Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        this.cachedPool =  Executors.newCachedThreadPool();
    }

    public ScheduledExecutorService getScheduledPool() {
        return scheduledPool;
    }

    public ExecutorService getCachedPool() {
        return cachedPool;
    }

    public static SchedulerManager getInstance() {
        if (instance == null) {
            instance = new SchedulerManager();
        }

        return instance;
    }
}
