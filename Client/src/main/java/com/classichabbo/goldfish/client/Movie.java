package com.classichabbo.goldfish.client;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.scheduler.types.GraphicsScheduler;
import com.classichabbo.goldfish.client.game.scheduler.types.InterfaceScheduler;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.interfaces.types.entry.EntryView;
import com.classichabbo.goldfish.client.interfaces.types.loader.LoadingScreen;
import com.classichabbo.goldfish.client.interfaces.types.widgets.Widget;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import com.classichabbo.goldfish.networking.NettyClient;
import com.classichabbo.goldfish.networking.wrappers.messages.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Movie extends Application {
    private static Movie instance;
    private Stage primaryStage;

    private GraphicsScheduler gameScheduler;
    private InterfaceScheduler interfaceScheduler;

    //private final Map<VisualiserType, Visualiser> visualisers;
    private final List<Interface> interfaces;

    //private Visualiser currentVisualiser;

    private Map<Integer, Message> incomingHandlers;
    private Map<String, Integer> outgoingHandlers;

    private Pane pane;
    private Scene mainScene;

    public Movie() {
        /*this.visualisers = new ConcurrentHashMap<>();
        this.visualisers.put(VisualiserType.LOADER, new LoaderVisualiser());
        this.visualisers.put(VisualiserType.HOTEL_VIEW, new EntryVisualiser());
        this.visualisers.put(VisualiserType.ROOM, new RoomVisualiser());*/

        this.incomingHandlers = new ConcurrentHashMap<>();
        this.outgoingHandlers = new ConcurrentHashMap<>();

        this.interfaces = new CopyOnWriteArrayList<>();
    }

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void init() {
        instance = this;

        startGameScheduler();
        startInterfaceScheduler();
    }

    @Override
    public void start(Stage primaryStage) {
        // Needed for volter font shit :)
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.subpixeltext", "false");

        ResourceManager.getInstance().loadFonts();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Habbo Client");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        var WIDTH = (int) DimensionUtil.roundEven(width * 0.5);
        var HEIGHT = (int) DimensionUtil.roundEven(height * 0.5);

        this.pane = new Pane();
        this.pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.mainScene = new Scene(this.pane, WIDTH, HEIGHT, Color.BLACK);

        primaryStage.setScene(this.mainScene);
        primaryStage.show();

        this.createObject(new LoadingScreen());

        //this.showVisualiser(VisualiserType.LOADER);
        // this.showVisualiser(VisualiserType.ROOM);
    }

    @Override
    public void stop() {
        this.stopGameScheduler();
        this.stopInterfaceScheduler();

        NettyClient.getInstance().dispose();
        System.exit(0);
    }

    /**
     * Method to initialise interface
     * @param control
     */
    private void setupInterface(Interface control) {


    }

    /**
     * Create interface to appear on current scene
     */
    public void createObject(Interface control) {
        Platform.runLater(() -> {
            if (!this.pane.getChildren().contains(control)) {
                this.pane.getChildren().add(control);

            }

            control.start();
            this.interfaces.add(control);


        });
    }

    public void removeObject(Interface control) {
        Platform.runLater(() -> {
            if (this.pane.getChildren().contains(control)) {
                this.pane.getChildren().remove(control);
            }

            control.stop();
            this.interfaces.remove(control);
        });
    }

    public boolean isInterfaceActive(Class<?> clazz) {
        return this.interfaces.stream().anyMatch(x -> x.getClass() == clazz || x.getClass().isAssignableFrom(clazz));
    }

    public <T extends Interface> List<T> getInterfacesByClass(Class<T> interfaceClass) {
        List<T> entities = new ArrayList<>();

        for (Interface entity : this.interfaces) {
            if (entity.getClass().isAssignableFrom(interfaceClass)) {
                entities.add(interfaceClass.cast(entity));
            }
        }

        return entities;
    }

    public <T extends Interface> T getInterfaceByClass(Class<T> interfaceClass) {
        return this.getInterfacesByClass(interfaceClass).stream().findFirst().orElse(null);
    }

    // Hide widgets (navigator, catalogue etc), used for stuff such as room entry
    public void hideWidgets() {
        this.interfaces.stream().filter(x -> x instanceof Widget).forEach(x -> {
            if (!x.isHidden()) {
                x.setHidden(true);
            }
        });
    }

    public void startGameScheduler() {
        if (this.gameScheduler != null) {
            stopGameScheduler();
        }

        this.gameScheduler = new GraphicsScheduler();
    }

    public void stopGameScheduler() {
        if (this.gameScheduler == null) {
            return;
        }

        this.gameScheduler.setRunning(false);
        this.gameScheduler = null;
    }

    public void startInterfaceScheduler() {
        if (this.interfaceScheduler != null) {
            stopInterfaceScheduler();
        }

        this.interfaceScheduler = new InterfaceScheduler();
    }

    public void stopInterfaceScheduler() {
        if (this.interfaceScheduler == null) {
            return;
        }

        this.interfaceScheduler.setRunning(false);
        this.interfaceScheduler = null;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public GraphicsScheduler getGameScheduler() {
        return gameScheduler;
    }

    public InterfaceScheduler getInterfaceScheduler() {
        return interfaceScheduler;
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public static Movie getInstance() {
        return instance;
    }

    public Pane getPane() {
        return pane;
    }

    public Scene getMainScene() {
        return mainScene;
    }
}