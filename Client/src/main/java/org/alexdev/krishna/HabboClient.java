package org.alexdev.krishna;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.alexdev.krishna.scenes.HabboScene;
import org.alexdev.krishna.scenes.HabboSceneType;
import org.alexdev.krishna.scenes.hotelview.HotelViewManager;
import org.alexdev.krishna.scenes.loader.LoaderManager;
import org.alexdev.krishna.util.StaticSettings;
import javafx.scene.robot.Robot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HabboClient extends Application implements Runnable {
    private static final int MAX_FPS = 24;

    private Scene mainScene;
    private Stage primaryStage;
    private StackPane rootStackPane;

    private ConcurrentMap<HabboSceneType, HabboScene> scenes;
    private int fps;
    private int ups;
    private Robot robot;
    private boolean isRunning = false;

    public HabboClient() {
        this.scenes = new ConcurrentHashMap<>();
    }

    @Override
    public void init() {
        Krishna.setClient(this);

        this.isRunning = true;
        new Thread(this).start();
    }

    /**
     * The whole rendering cycling.
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double ns = 1000000000.0 / 24.0;
        int frames = 0;
        int updates = 0;
        long last = System.currentTimeMillis();

        while (this.isRunning) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / ns;
            lastTime = now;
            boolean render = true;
            while (unprocessed >= 1) {
                updates++;
                //Do game updates.
                update();
                unprocessed -= 1;
                render = true;
            }
            if (render) {
                frames++;
                //Start of frame rate limiter.
                double start = System.currentTimeMillis();
                //Render game.
                render();
                double sleep = (1000 / MAX_FPS - (System.currentTimeMillis() - start));
                if (sleep > 0) {
                    try {
                        Thread.sleep((long) sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //End of frame rate limiter.
            }

            if (System.currentTimeMillis() - last > 1000) {
                last += 1000;
                fps = frames;
                ups = updates;
                //Print fps and ups to console.
                //System.out.println(fps + "fps, " + ups + "ups");
                frames = 0;
                updates = 0;
            }
        }
    }

    private void update() {
        for (var scene : this.scenes.values()) {
            scene.updateTick();
        }
    }

    private void render() {
        for (var scene : this.scenes.values()) {
            scene.renderTick();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.robot = new Robot();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Habbo Client");

        this.rootStackPane = new StackPane();
        this.mainScene = new Scene(this.rootStackPane, StaticSettings.WIDTH, StaticSettings.HEIGHT, Color.BLACK);

        primaryStage.setScene(this.mainScene);
        primaryStage.show();

        this.setupStages();
        this.showStage(HabboSceneType.LOADER);
    }

    @Override
    public void stop(){
        this.isRunning = false;
    }

    public void showStage(HabboSceneType type) {
        var scene = this.scenes.get(type);

        if (scene != null)
        {
            scene.init();
            this.primaryStage.setScene(scene.getScene());
        }
    }

    private void setupStages() {
        this.scenes.put(HabboSceneType.LOADER, new LoaderManager());
        this.scenes.put(HabboSceneType.HOTEL_VIEW, new HotelViewManager());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ConcurrentMap<HabboSceneType, HabboScene> getScenes() {
        return scenes;
    }
}