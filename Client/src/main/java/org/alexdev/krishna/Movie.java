package org.alexdev.krishna;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.alexdev.krishna.game.scheduler.types.InterfaceScheduler;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.game.scheduler.types.GraphicsScheduler;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.visualisers.VisualiserType;
import org.alexdev.krishna.visualisers.types.entry.EntryVisualiser;
import org.alexdev.krishna.visualisers.types.loader.LoaderVisualiser;
import org.alexdev.krishna.visualisers.types.room.RoomVisualiser;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Movie extends Application {
    private static Movie instance;
    private Stage primaryStage;

    private GraphicsScheduler gameScheduler;
    private InterfaceScheduler interfaceScheduler;

    private final Map<VisualiserType, Visualiser> visualisers;
    private final List<Interface> interfaces;
    private Visualiser currentVisualiser;

    public Movie() {
        this.visualisers = new ConcurrentHashMap<>();
        this.visualisers.put(VisualiserType.LOADER, new LoaderVisualiser());
        this.visualisers.put(VisualiserType.HOTEL_VIEW, new EntryVisualiser());
        this.visualisers.put(VisualiserType.ROOM, new RoomVisualiser());

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

        Pane pane = new Pane();
        Scene mainScene = new Scene(pane, WIDTH, HEIGHT, Color.BLACK);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        this.showVisualiser(VisualiserType.LOADER);
        // this.showVisualiser(VisualiserType.ROOM);
    }

    @Override
    public void stop(){
        this.stopGameScheduler();
        this.stopInterfaceScheduler();
    }

    public void showVisualiser(VisualiserType type) {
        Platform.runLater(() -> {
            var previousVisualiser = this.currentVisualiser;

            if (previousVisualiser != null) {
                previousVisualiser.stop();
            }

            var visualiser = this.visualisers.get(type);

            if (visualiser != null) {
                this.currentVisualiser = visualiser;
                setupVisualiser(visualiser);
                this.primaryStage.setScene(visualiser.getScene());

                this.interfaces.forEach(control -> {
                    if (!visualiser.getPane().getChildren().contains(control)) {
                        visualiser.getPane().getChildren().add(control);
                    }

                    control.visualiserChanged(previousVisualiser, this.currentVisualiser);
                });
            }
        });
    }

    /**
     * Set up the visualiser we're about to show
     */
    private void setupVisualiser(Visualiser visualiser) {
        visualiser.start();
        visualiser.getComponent().init();
        visualiser.update();

        visualiser.getScene().setOnMouseClicked(x -> {
            if (x.getButton() == MouseButton.SECONDARY) {
                if (this.gameScheduler != null) {
                    stopGameScheduler();
                } else {
                    startGameScheduler();
                }
            }
        });
    }

    /**
     * Method to initialise interface
     * @param control
     */
    private void setupInterface(Interface control) {
        control.start();
        control.update();
        control.visualiserChanged(null, this.currentVisualiser);
    }

    /**
     * Create new scene management
     */
    public Scene createScene(Pane pane) {
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        return new Scene(pane, DimensionUtil.getProgramWidth(), DimensionUtil.getProgramHeight(), Color.BLACK);
    }

    /**
     * Create interface to appear on current scene
     */
    public void createObject(Interface control) {
        Platform.runLater(() -> {
            this.setupInterface(control);
            this.interfaces.add(control);

            if (!this.currentVisualiser.getPane().getChildren().contains(control)) {
                this.currentVisualiser.getPane().getChildren().add(control);
            }
        });
    }

    public void removeObject(Interface control) {
        Platform.runLater(() -> {
            this.visualisers.values().forEach(visualiser -> {
                if (visualiser != null && visualiser.getPane() != null) {
                    if (visualiser.getPane().getChildren().contains(control)) {
                        visualiser.getPane().getChildren().remove(control);
                    }
                }
            });

            //if (this.currentVisualiser.getPane().getChildren().contains(control)) {
            //    this.currentVisualiser.getPane().getChildren().remove(control);
            //}

            control.stop();
            this.interfaces.remove(control);
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

    public Map<VisualiserType, Visualiser> getVisualisers() {
        return visualisers;
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public static Movie getInstance() {
        return instance;
    }

    public Visualiser getCurrentVisualiser() {
        return currentVisualiser;
    }
}