package org.alexdev.krishna;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.interfaces.Alert;
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.game.GameLoop;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.visualisers.VisualiserType;
import org.alexdev.krishna.visualisers.types.entry.EntryVisualiser;
import org.alexdev.krishna.visualisers.types.loader.LoaderVisualiser;
import org.alexdev.krishna.visualisers.types.room.RoomVisualiser;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class HabboClient extends Application {
    public static Font volter;
    public static Font volterBold;
    public static Font volterLarge;
    public static Font volterBoldLarge;

    private ScheduledExecutorService schedulerService;

    private static HabboClient instance;
    private Stage primaryStage;
    private GameLoop gameLoop;

    private final ConcurrentMap<VisualiserType, Visualiser> visualisers;
    private final List<Interface> interfaces;

    public HabboClient() {
        this.visualisers = new ConcurrentHashMap<>();
        this.interfaces = new ArrayList<Interface>();
        this.schedulerService =  Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
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
        // Needed for volter font shit :)
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.subpixeltext", "false");

        //this.loadFonts();

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
        this.setupInterfaces();
        //this.showStage(HabboSceneType.LOADER);
        this.showVisualiser(VisualiserType.LOADER);
    }

    @Override
    public void stop(){
        if (this.gameLoop != null)
            this.gameLoop.setRunning(false);
    }

    public void loadFonts() {
        new Thread(() -> {
            try {
                volter = Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter.woff").openStream(), 9);
                volterBold = Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter_bold.woff").openStream(), 9);
                volterLarge = Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter.woff").openStream(), 18);
                volterBoldLarge = Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter_bold.woff").openStream(), 18);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void showVisualiser(VisualiserType type) {
        var visualiser = this.visualisers.get(type);

        if (visualiser != null) {
            visualiser.init();
            setupVisualiser(visualiser);
            this.primaryStage.setScene(visualiser.getScene());
            //visualiser.update();
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
        //this.visualisers.put(VisualiserType.HOTEL_VIEW, new EntryVisualiser());
        //this.visualisers.put(VisualiserType.ROOM, new RoomVisualiser());
    }

    private void setupInterfaces() {
        this.interfaces.add(new Alert("This is a test"));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ConcurrentMap<VisualiserType, Visualiser> getVisualisers() {
        return visualisers;
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public static HabboClient getInstance() {
        return instance;
    }

    public ScheduledExecutorService getScheduler() {
        return schedulerService;
    }
}