package org.alexdev.krishna;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.alexdev.krishna.game.GameLoop;
import org.alexdev.krishna.scenes.HabboScene;
import org.alexdev.krishna.scenes.HabboSceneType;
import org.alexdev.krishna.scenes.hotelview.HotelViewManager;
import org.alexdev.krishna.scenes.loader.LoaderManager;
import org.alexdev.krishna.util.StaticSettings;
import javafx.scene.robot.Robot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HabboClient extends Application {
    private static HabboClient instance;
    private Stage primaryStage;
    private GameLoop gameLoop;

    private final ConcurrentMap<HabboSceneType, HabboScene> scenes;

    public HabboClient() {
        this.scenes = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        Application.launch();
    }

    public void startGameLoop() {
        if (this.gameLoop != null) {
            stopGameLoop();
        }

        this.gameLoop = new GameLoop();
    }

    public void stopGameLoop() {
        if (this.gameLoop == null) {
            return;
        }

        this.gameLoop.setRunning(false);
        this.gameLoop = null;
    }

    @Override
    public void init() {
        instance = this;
        startGameLoop();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Habbo Client");

        Pane pane = new Pane();
        Scene mainScene = new Scene(pane, StaticSettings.WIDTH, StaticSettings.HEIGHT, Color.BLACK);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        this.setupStages();
        // this.showStage(HabboSceneType.LOADER);
        this.showStage(HabboSceneType.HOTEL_VIEW);
    }

    @Override
    public void stop(){
        if (this.gameLoop != null)
            this.gameLoop.setRunning(false);
    }

    public void showStage(HabboSceneType type) {
        var scene = this.scenes.get(type);

        if (scene != null)
        {
            scene.init();
            setupScene(scene);
            this.primaryStage.setScene(scene.getScene());
            scene.renderTick();
        }
    }

    private void setupScene(HabboScene scene) {
        scene.getScene().setOnMouseClicked(x -> {
            if (x.getButton() == MouseButton.SECONDARY) {
                if (this.gameLoop != null) {
                    stopGameLoop();
                } else {
                    startGameLoop();
                }
            }
        });
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
    public static HabboClient getInstance() {
        return instance;
    }
}