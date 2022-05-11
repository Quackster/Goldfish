package org.alexdev.krishna.game;

import javafx.application.Platform;
import org.alexdev.krishna.HabboClient;

public class GameLoop implements Runnable {
    public static final int MAX_FPS = 12;

    private Thread gameLoop;
    private boolean isRunning;

    public GameLoop() {
        this.isRunning = true;
        this.gameLoop = new Thread(this);
        this.gameLoop.start();
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
        while (isRunning) {
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
        for (var interfacefx : HabboClient.getInstance().getInterfaces()) {
            if (!interfacefx.isReady()) {
                continue;
            }

            Platform.runLater(() -> {
                try {
                    interfacefx.update();
                } catch (Exception ex) {
                    System.out.println("Interface crash: ");
                    ex.printStackTrace();
                }
            });
        }

        for (var scene : HabboClient.getInstance().getVisualisers().values()) {
            if (!scene.isReady()) {
                continue;
            }

            Platform.runLater(() -> {
                try {
                    scene.update();
                } catch (Exception ex) {
                    System.out.println("Scene crash: ");
                    ex.printStackTrace();
                }
            });
        }
    }

    public Thread getGameLoop() {
        return gameLoop;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
