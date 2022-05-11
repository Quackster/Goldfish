package org.alexdev.krishna;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import org.alexdev.krishna.game.InterfaceUpdateLoop;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.interfaces.types.Alert;
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.game.GameUpdateLoop;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.visualisers.VisualiserType;
import org.alexdev.krishna.visualisers.types.entry.EntryVisualiser;
import org.alexdev.krishna.visualisers.types.loader.LoaderVisualiser;

import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

public class HabboClient extends Application {
    public static Font volter;
    public static Font volterBold;
    public static Font volterLarge;
    public static Font volterBoldLarge;

    private static HabboClient instance;
    private Stage primaryStage;

    private GameUpdateLoop gameUpdateLoop;
    private InterfaceUpdateLoop interfaceUpdateLoop;

    private final ConcurrentMap<VisualiserType, Visualiser> visualisers;
    private final CopyOnWriteArrayList<Interface> interfaces;
    private Visualiser currentVisualiser;

    public HabboClient() {
        this.visualisers = new ConcurrentHashMap<>();
        this.visualisers.put(VisualiserType.LOADER, new LoaderVisualiser());
        this.visualisers.put(VisualiserType.HOTEL_VIEW, new EntryVisualiser());

        this.interfaces = new CopyOnWriteArrayList<>();
    }

    public static void main(String[] args) {
        Application.launch();
    }

    public void startGameUpdateLoops() {
        if (this.gameUpdateLoop != null) {
            stopGameUpdateLoops();
        }

        this.gameUpdateLoop = new GameUpdateLoop();
    }

    public void stopGameUpdateLoops() {
        if (this.gameUpdateLoop == null) {
            return;
        }

        this.gameUpdateLoop.setRunning(false);
        this.gameUpdateLoop = null;
    }

    public void startInterfaceUpdateLoops() {
        if (this.interfaceUpdateLoop != null) {
            stopInterfaceUpdateLoops();
        }

        this.interfaceUpdateLoop = new InterfaceUpdateLoop();
    }

    public void stopInterfaceUpdateLoops() {
        if (this.interfaceUpdateLoop == null) {
            return;
        }

        this.interfaceUpdateLoop.setRunning(false);
        this.interfaceUpdateLoop = null;
    }

    @Override
    public void init() {
        instance = this;

        startGameUpdateLoops();
        startInterfaceUpdateLoops();
    }

    @Override
    public void start(Stage primaryStage) {
        // Needed for volter font shit :)
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.subpixeltext", "false");

        this.loadFonts();

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

        //this.showStage(HabboSceneType.LOADER);
        this.showVisualiser(VisualiserType.LOADER);
    }

    @Override
    public void stop(){
        if (this.gameUpdateLoop != null)
            this.gameUpdateLoop.setRunning(false);
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
            this.currentVisualiser = visualiser;
            setupVisualiser(visualiser);
            this.primaryStage.setScene(visualiser.getScene());

            visualiser.getPane().getChildren().addAll(this.interfaces);
            this.interfaces.forEach(Interface::sceneChanged);
        }
    }

    /**
     * Set up the visualiser we're about to show
     */
    private void setupVisualiser(Visualiser visualiser) {
        visualiser.init();
        visualiser.getComponent().init();
        visualiser.update();

        visualiser.getScene().setOnMouseClicked(x -> {
            if (x.getButton() == MouseButton.SECONDARY) {
                if (this.gameUpdateLoop != null) {
                    stopGameUpdateLoops();
                } else {
                    startGameUpdateLoops();
                }
            }
        });
    }

    /**
     * Method to initialise interface
     * @param control
     */
    private void setupInterface(Interface control) {
        control.init();
        control.update();
    }

    /**
     * Create new scene management
     */
    public Scene createScene(Pane pane) {
        return new Scene(pane, DimensionUtil.getProgramWidth(), DimensionUtil.getProgramHeight(), Color.BLACK);
    }

    /**
     * Queue new interface to appear on next scene change
     */
    public void queue(Interface control) {
        this.setupInterface(control);
        this.interfaces.add(control);
    }

    /**
     * Submit new interface to appear on next scene change
     */
    public void submit(Interface control) {
        this.setupInterface(control);
        this.interfaces.add(control);
        this.currentVisualiser.getPane().getChildren().add(control);
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

    public Visualiser getCurrentVisualiser() {
        return currentVisualiser;
    }
}