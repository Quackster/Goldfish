package org.alexdev.krishna;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
    public static final int MAX_FPS = 24;

    private Scene mainScene;
    private Stage primaryStage;
    private Pane pane;

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
        double nsPerTick = 1000000000.0 / HabboClient.MAX_FPS;
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
                render();
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
        for (var scene : this.scenes.values()) {
            if (!scene.isReady()) {
                continue;
            }

            Platform.runLater(() -> {
                try {
                    scene.updateTick();
                } catch (Exception ex) {
                    System.out.println("Scene crash: ");
                    ex.printStackTrace();
                }
            });
        }
    }

    private void render() {
        for (var scene : this.scenes.values()) {
            if (!scene.isReady()) {
                continue;
            }

            Platform.runLater(() -> {
                try {
                    scene.renderTick();
                } catch (Exception ex) {
                    System.out.println("Scene crash: ");
                    ex.printStackTrace();
                }
            });
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.robot = new Robot();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Habbo Client");

        this.pane = new Pane();
        this.mainScene = new Scene(this.pane, StaticSettings.WIDTH, StaticSettings.HEIGHT, Color.BLACK);

        primaryStage.setScene(this.mainScene);
        primaryStage.show();

        this.setupStages();
        // this.showStage(HabboSceneType.LOADER);
         this.showStage(HabboSceneType.HOTEL_VIEW);
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
            scene.renderTick();
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