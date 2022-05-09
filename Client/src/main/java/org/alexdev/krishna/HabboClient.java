package org.alexdev.krishna;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.alexdev.krishna.game.GameLoop;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.visualisers.VisualiserType;
import org.alexdev.krishna.visualisers.entry.EntryVisualiser;
import org.alexdev.krishna.visualisers.loader.LoaderVisualiser;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HabboClient extends Application {
    private static HabboClient instance;
    private Stage primaryStage;
    private GameLoop gameLoop;

    private final ConcurrentMap<VisualiserType, Visualiser> visualisers;

    public HabboClient() {
        this.visualisers = new ConcurrentHashMap<>();
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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        var WIDTH = (int) DimensionUtil.roundEven(width * 0.5);
        var HEIGHT = (int) DimensionUtil.roundEven(height * 0.5);

        Pane pane = new Pane();
        Scene mainScene = new Scene(pane, WIDTH, HEIGHT, Color.BLACK);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        this.setupVisualisers();
        //this.showStage(HabboSceneType.LOADER);
        this.showVisualiser(VisualiserType.HOTEL_VIEW);
    }

    @Override
    public void stop(){
        if (this.gameLoop != null)
            this.gameLoop.setRunning(false);
    }

    public void showVisualiser(VisualiserType type) {
        var visualiser = this.visualisers.get(type);

        if (visualiser != null)
        {
            visualiser.init();
            setupVisualiser(visualiser);
            this.primaryStage.setScene(visualiser.getScene());
            visualiser.update();
        }
    }

    private void setupVisualiser(Visualiser visualiser) {
        visualiser.getScene().setOnMouseClicked(x -> {
            if (x.getButton() == MouseButton.SECONDARY) {
                if (this.gameLoop != null) {
                    stopGameLoop();
                } else {
                    startGameLoop();
                }
            }
        });
    }

    private void setupVisualisers() {
        this.visualisers.put(VisualiserType.LOADER, new LoaderVisualiser());
        this.visualisers.put(VisualiserType.HOTEL_VIEW, new EntryVisualiser());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ConcurrentMap<VisualiserType, Visualiser> getVisualisers() {
        return visualisers;
    }

    public static HabboClient getInstance() {
        return instance;
    }
}