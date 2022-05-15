package com.classichabbo.goldfish.client.game.scheduler.types;

import com.classichabbo.goldfish.client.game.scheduler.Scheduler;
import javafx.application.Platform;

public class InterfaceScheduler extends Scheduler {
    public static final int MAX_FPS = 24;

    public InterfaceScheduler() {
    }

    /**
     * The whole rendering cycling.
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / MAX_FPS;
        int frames = 0;
        int ticks = 0;
        long lastTimer1 = System.currentTimeMillis();

        while (this.isRunning()) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (unprocessed >= 1) {
                ticks++;
                unprocessed -= 1;
                update();
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frames++;
            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;
                //System.out.println(ticks + " ticks, " + frames + " fps");
                frames = 0;
                ticks = 0;
            }
        }
    }

    private void update() {
        for (var interfacefx : this.getUpdatesRequired()) {
            Platform.runLater(() -> {
                try {
                    interfacefx.update();
                } catch (Exception ex) {
                    System.out.println("Interface crash: ");
                    ex.printStackTrace();
                }
            });
        }
    }
}
