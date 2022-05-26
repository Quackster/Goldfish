package com.classichabbo.goldfish.client;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.scheduler.types.GraphicsScheduler;
import com.classichabbo.goldfish.client.game.scheduler.types.InterfaceScheduler;
import com.classichabbo.goldfish.client.modules.types.GoldfishView;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.controls.TextField;
import com.classichabbo.goldfish.client.modules.types.entry.EntryView;
import com.classichabbo.goldfish.client.modules.types.loader.LoaderView;
import com.classichabbo.goldfish.client.modules.types.room.RoomTransition;
import com.classichabbo.goldfish.client.modules.types.room.RoomView;
import com.classichabbo.goldfish.client.modules.types.toolbars.entry.EntryToolbarView;
import com.classichabbo.goldfish.client.modules.types.toolbars.RoomToolbar;
import com.classichabbo.goldfish.client.modules.types.widgets.Widget;
import com.classichabbo.goldfish.client.modules.types.widgets.navigator.NavigatorView;
import com.classichabbo.goldfish.util.DimensionUtil;
import com.classichabbo.goldfish.networking.netty.NettyClientConnection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class Movie extends Application {
    private static Movie instance;
    private Stage primaryStage;

    private GraphicsScheduler gameScheduler;
    private InterfaceScheduler interfaceScheduler;

    private final List<View> views;

    private Pane pane;
    private Scene mainScene;
    private TextField currentTextField;

    public Movie() {
        this.views = new CopyOnWriteArrayList<>();
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

        setupKeyboardEvents();
        setupContextMenu();

        // Send the key events to the current text field
        this.mainScene.setOnKeyPressed(e -> {
            if (this.currentTextField != null) {
                this.currentTextField.sendKeyPressed(e);
            }
        });

        this.mainScene.setOnKeyTyped(e -> {
            if (this.currentTextField != null) {
                this.currentTextField.sendKeyTyped(e);
            }
        });

        primaryStage.setScene(this.mainScene);
        primaryStage.show();

        this.createObject(new GoldfishView());
        this.createObject(new LoaderView());

        //this.showVisualiser(VisualiserType.LOADER);
        // this.showVisualiser(VisualiserType.ROOM);
    }

    private void setupContextMenu() {
        Function<Boolean, String> alwaysOnTopText = (isAlwaysOnTop) -> {
            return (isAlwaysOnTop ? Symbols.CROSS : Symbols.TICK) + " Always on top";
        };


        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItem1 = new MenuItem("Take screenshot");
        MenuItem menuItem2 = new MenuItem("Pause Client Ticks");
        MenuItem menuItem3 = new MenuItem(alwaysOnTopText.apply(this.primaryStage.isAlwaysOnTop()));

        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // TODO: Take screenshot
            }
        });

        menuItem3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                boolean isAlwaysOnTop = !primaryStage.isAlwaysOnTop();
                primaryStage.setAlwaysOnTop(isAlwaysOnTop);
                menuItem2.setText(alwaysOnTopText.apply(isAlwaysOnTop));
            }
        });

        contextMenu.getItems().add(menuItem1);
        contextMenu.getItems().add(menuItem2);

        this.mainScene.setOnContextMenuRequested(event -> {
          contextMenu.show(this.mainScene.getWindow(), event.getScreenX(), event.getScreenY());
        });

    }

    private void setupKeyboardEvents() {

    }

    @Override
    public void stop() {
        this.stopGameScheduler();
        this.stopInterfaceScheduler();

        NettyClientConnection.getInstance().dispose();
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
            view.setVisible(false);

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

            view.setVisible(true);

        });

        this.views.add(view);
        // this.printViews();
    }

    /**
     * Remove an object, if view is a child of an object, it will be removed from the parent.
     */
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

        // Remove any possibly orphaned children
        this.views.forEach(x -> {
            if (x.getOwner() == view) {
                // System.out.println("Removed orphan");
                removeObject(x);
            }
        });
       // this.printViews();
    }

    /**
     * Hides an object, the same way the official client does *WITHOUT* affecting view order.
     *
     * Will unregister it from the game loops too - assuming they exist.
     */
    public void hideObject(View view) {
        Platform.runLater(() -> {
            view.setVisible(false);
            view.removeUpdate();
        });
    }

    /**
     * Shows an object, the same way the official client does *WITHOUT* affecting view order.
     *
     * Will register back to the game loops too - assuming they need to be within the overrided View function.
     */
    public void showObject(View view) {
        Platform.runLater(() -> {
            view.registerUpdate();
            view.setVisible(true);
        });
    }


    private void printViews() {
        this.views.forEach(x -> {
            System.out.println(x.getClass().getName());
        });

        System.out.println("------------");
    }

    /**
     * Gets whether the view is active.
     */
    public boolean isViewActive(Class<?> clazz) {
        return this.views.stream().anyMatch(x -> x.getClass() == clazz || x.getClass().isAssignableFrom(clazz));
    }

    /**
     * Gets list of views by class.
     */
    public <T extends View> List<T> getViewsByClass(Class<T> interfaceClass) {
        List<T> entities = new ArrayList<>();

        for (View entity : this.views) {
            if (entity.getClass().isAssignableFrom(interfaceClass)) {
                entities.add(interfaceClass.cast(entity));
            }
        }

        return entities;
    }

    /**
     * Gets view by class
     */
    public <T extends View> T getViewByClass(Class<T> interfaceClass) {
        return this.getViewsByClass(interfaceClass).stream().findFirst().orElse(null);
    }

    /**
     * Hide widgets (navigator, catalogue etc), used for stuff such as room entry
     */
    public void hideWidgets() {
        this.views.stream().filter(x -> x instanceof Widget).forEach(x -> {
            if (!x.isHidden()) {
                x.setHidden(true);
            }
        });
    }

    /**
     * Tell client to leave the room and go to hotel view
     */
    public void goToHotelView() {
        if (!Movie.getInstance().isViewActive(RoomView.class)) {
            return;
        }

        Platform.runLater(() -> {
            if (Movie.getInstance().isViewActive(EntryView.class)) {
                final var entryView = this.getViewByClass(EntryView.class);
                final var roomView = this.getViewByClass(RoomView.class);
                final var roomToolbar = this.getViewByClass(RoomToolbar.class);

                if (roomToolbar != null) {
                    this.removeObject(roomToolbar);
                }

                if (roomView != null) {
                    final RoomTransition roomTransition = new RoomTransition();

                    roomTransition.setRunAfterFinished(() -> {
                        entryView.setRunAfterOpening(() -> entryView.getComponent().toHotelView());
                        this.removeObject(roomTransition);
                        this.showObject(entryView);
                    });

                    this.removeObject(roomView);
                    this.createObject(roomTransition);
                }
            }
        });
    }

    /**
     * Go to room handler
     */
    public void goToRoom(int roomId, String password) {
        Platform.runLater(() -> {
            // TODO Avery move this where you like but needs to send back password result
            // If you're not happy with this just remove the if / else if and the password parameter :)
            if (password != null && password.equals("password")) {
                this.getViewByClass(NavigatorView.class).sendPasswordResult(true);
            }
            else if (password != null) {
                this.getViewByClass(NavigatorView.class).sendPasswordResult(false);
                return;
            }

            if (Movie.getInstance().isViewActive(EntryView.class)) {
                var entryView = Movie.getInstance().getViewByClass(EntryView.class);
                var entryToolbar = Movie.getInstance().getViewByClass(EntryToolbarView.class);

                if (entryToolbar != null) {
                    this.removeObject(entryToolbar);
                }

                entryView.setRunAfterClosing(() -> {
                    final RoomTransition roomTransition = new RoomTransition();

                    roomTransition.setRunAfterFinished(() -> {
                        this.createObject(new RoomView());
                        this.removeObject(roomTransition);
                    });

                    this.createObject(roomTransition);
                    this.hideObject(entryView);
                });

                this.hideWidgets();
            }
        });
    }

    /**
     * Set current text field to receive key events
     */
    public void setCurrentTextField(TextField textField) {
        this.currentTextField = textField;
    }

    /**
     * Return the current text field
     */
    public TextField getCurrentTextField() {
        return this.currentTextField;
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

}