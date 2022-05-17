package com.classichabbo.goldfish.client;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.scheduler.types.GraphicsScheduler;
import com.classichabbo.goldfish.client.game.scheduler.types.InterfaceScheduler;
import com.classichabbo.goldfish.client.handlers.GlobalHandler;
import com.classichabbo.goldfish.client.views.GlobalView;
import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.client.views.types.loader.LoadingScreen;
import com.classichabbo.goldfish.client.views.types.widgets.Widget;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import com.classichabbo.goldfish.networking.NettyClient;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.types.MessageCommand;
import com.classichabbo.goldfish.networking.wrappers.messages.types.MessageListener;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Movie extends Application {
    private static Movie instance;
    private Stage primaryStage;

    private GraphicsScheduler gameScheduler;
    private InterfaceScheduler interfaceScheduler;

    //private final Map<VisualiserType, Visualiser> visualisers;
    private final List<View> views;

    //private Visualiser currentVisualiser;

    private List<MessageListener> listeners;
    private List<MessageCommand> commands;

    private Pane pane;
    private Scene mainScene;

    public Movie() {
        /*this.visualisers = new ConcurrentHashMap<>();
        this.visualisers.put(VisualiserType.LOADER, new LoaderVisualiser());
        this.visualisers.put(VisualiserType.HOTEL_VIEW, new EntryVisualiser());
        this.visualisers.put(VisualiserType.ROOM, new RoomVisualiser());*/

        this.listeners = new CopyOnWriteArrayList<>();
        this.commands = new CopyOnWriteArrayList<>();
        this.views = new CopyOnWriteArrayList<>();
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

        this.createObject(new GlobalView());
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
     * Create interface to appear on current scene
     */
    public void createObject(View control) {
        createObject(control, null);
    }

    /**
     * Create interface to appear on current scene.
     *
     * If parent is supplied, then the interface is added to the parent rather than the main screen.
     */
    public void createObject(View view, View parent) {
        Platform.runLater(() -> {
            if (parent != null) {
                if (!parent.getChildren().contains(view)) {
                    parent.getChildren().add(view);
                    view.setOwner(parent);
                }

            } else {
                if (!this.pane.getChildren().contains(view)) {
                    this.pane.getChildren().add(view);
                }
            }

            view.start();
            view.update();

            if (view.getHandler() != null) {
                view.getHandler().regMsgList(true);
            }

            this.views.add(view);
        });
    }

    public void removeObject(View view) {
        Platform.runLater(() -> {
            if (view.getOwner() != null) {
                var parent = view.getOwner();

                if (parent.getChildren().contains(view)) {
                    parent.getChildren().remove(view);
                }

                view.setOwner(null);
                // System.out.println("removed child: " + control.getClass().getName());
            } else {
                if (this.pane.getChildren().contains(view)) {
                    this.pane.getChildren().remove(view);
                }
            }

            view.stop();

            if (view.getHandler() != null) {
                view.getHandler().regMsgList(false);
            }

            this.views.remove(view);
        });
    }

    public void registerListeners(MessageHandler messageHandler, HashMap<Integer, MessageRequest> listeners) {
        for (var x : listeners.entrySet()) {
            this.listeners.add(new MessageListener(messageHandler.getClass(), x.getKey(), x.getValue()));
        }
    }

    public void unregisterListeners(MessageHandler messageHandler, HashMap<Integer, MessageRequest> listeners) {
        listeners.forEach((key, value) -> this.listeners.removeIf(message ->
                message.getHandlerClass() == messageHandler.getClass() && message.getHeader() == key));
    }


    public void registerCommands(MessageHandler messageHandler, HashMap<String, Integer> listeners) {
        for (var x : listeners.entrySet()) {
            this.commands.add(new MessageCommand(messageHandler.getClass(), x.getValue(), x.getKey()));
        }
    }

    public void unregisterCommands(MessageHandler messageHandler, HashMap<String, Integer> commands) {
        commands.forEach((key, value) -> this.commands.removeIf(message ->
                message.getHandlerClass() == messageHandler.getClass() && message.getHeader() == value));
    }

    public boolean isInterfaceActive(Class<?> clazz) {
        return this.views.stream().anyMatch(x -> x.getClass() == clazz || x.getClass().isAssignableFrom(clazz));
    }

    public <T extends View> List<T> getInterfacesByClass(Class<T> interfaceClass) {
        List<T> entities = new ArrayList<>();

        for (View entity : this.views) {
            if (entity.getClass().isAssignableFrom(interfaceClass)) {
                entities.add(interfaceClass.cast(entity));
            }
        }

        return entities;
    }

    public <T extends View> T getInterfaceByClass(Class<T> interfaceClass) {
        return this.getInterfacesByClass(interfaceClass).stream().findFirst().orElse(null);
    }

    // Hide widgets (navigator, catalogue etc), used for stuff such as room entry
    public void hideWidgets() {
        this.views.stream().filter(x -> x instanceof Widget).forEach(x -> {
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

    public List<View> getViews() {
        return views;
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

    public List<MessageListener> getListeners() {
        return listeners;
    }

    public List<MessageCommand> getCommands() {
        return commands;
    }
}