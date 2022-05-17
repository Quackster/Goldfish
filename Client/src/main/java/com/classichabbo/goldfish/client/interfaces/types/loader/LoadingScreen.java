package com.classichabbo.goldfish.client.interfaces.types.loader;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.scheduler.SchedulerManager;
import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.interfaces.types.entry.EntryView;
import com.classichabbo.goldfish.client.interfaces.types.error.ErrorWindow;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import com.classichabbo.goldfish.networking.NettyClient;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class LoadingScreen extends Interface {
    private ImageView loadingLogoImage;
    private ImageView loaderBarImage;
    private Rectangle loaderProgress;

    private Interface loaderBar;

    private int totalLoaderProgress;
    private int queueLoaderProgress;

    private ArrayList<String> loaderSteps;
    private ArrayList<String> loaderStepsFinished;

    private LoaderComponent component;

    private double draggedX;
    private double draggedY;

    private double mousePressedX;
    private double mousePressedY;

    public LoadingScreen() {
        this.component = new LoaderComponent();
    }

    @Override
    public void start() {
        // This fixes the issue by making transparent areas also mouse-transparent, however I would suggest not adding a child
        // ImageView and instead setting the background image of this (as in LoadingBar / this.setBackground) - feel free to
        // message me if you have any questions :) - Parsnip
        this.setPickOnBounds(false);

        this.loaderBar = new Interface();
        this.getChildren().add(this.loaderBar);

        this.queueLoaderProgress = 0;
        this.totalLoaderProgress = 0;

        this.loaderStepsFinished = new ArrayList<>();
        this.loaderSteps = new ArrayList<>();

        this.loaderSteps.add("load_client_config");
        this.loaderSteps.add("load_external_variables");
        this.loaderSteps.add("load_external_texts");
        this.loaderSteps.add("connect_server");

        this.component = new LoaderComponent();

        this.loadingLogoImage = new ImageView();
        this.loadingLogoImage.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/logo.png"));
        this.getChildren().add(this.loadingLogoImage);

        this.loaderBarImage = new ImageView();
        this.loaderBarImage.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/loader_bar_0.png"));
        this.loaderBar.getChildren().add(this.loaderBarImage);

        this.loaderProgress = new Rectangle(0, 12);
        this.loaderProgress.setFill(Color.web("#808080"));
        this.loaderBar.getChildren().add(this.loaderProgress);

        this.loaderBar.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
            this.loaderBar.toFront();
        });

        this.loaderBar.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        this.loaderBar.setPickOnBounds(false);

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        updateLoader();
        handleResize();
        loaderSteps();
        dragging();
    }

    private void updateLoader() {
        if (this.queueLoaderProgress > 0) {
            var newWidth = this.loaderProgress.getWidth() + 3;

            if (newWidth > 296) {
                newWidth = 296;
            }

            this.loaderProgress.setWidth(newWidth);

            this.queueLoaderProgress--;
            this.totalLoaderProgress++;
        }
    }


    private void progressLoader(int newProgress) {
        this.queueLoaderProgress += newProgress;
    }

    private void loaderSteps() {
        if (this.queueLoaderProgress > 0)
            return;

        try {
            // Load basic client configuration
            if (this.loaderSteps.contains("load_client_config")) {
                if (this.component.getClientConfigTask() == null) {
                    this.component.setClientConfigTask(
                            SchedulerManager.getInstance().getCachedPool().submit(() -> this.component.loadClientConfig())
                    );

                    return;
                }

                if (this.component.getClientConfigTask().isDone()) {
                    if (this.component.getClientConfigTask().get()) {
                        this.loaderSteps.remove("load_client_config");
                        this.loaderStepsFinished.add("load_client_config");
                        this.progressLoader(20);
                    } else {
                        this.component.setClientConfigTask(null);
                    }
                }
            }

            // Load external variables
            if (this.loaderSteps.contains("load_external_variables")) {
                if (this.loaderStepsFinished.contains("load_client_config")) {
                    if (this.component.getExternalVariablesTask() == null) {
                        this.component.setExternalVariablesTask(
                                SchedulerManager.getInstance().getCachedPool().submit(() -> this.component.loadExternalVariables())
                        );

                        return;
                    }

                    if (this.component.getExternalVariablesTask().isDone()) {
                        if (this.component.getExternalVariablesTask().get()) {
                            this.loaderSteps.remove("load_external_variables");
                            this.loaderStepsFinished.add("load_external_variables");
                            this.progressLoader(20);
                        } else {
                            this.component.setExternalVariablesTask(null);
                        }
                    }
                }
            }

            // Load external variables
            if (this.loaderSteps.contains("load_external_texts")) {
                if (this.loaderStepsFinished.contains("load_external_variables")) {
                    if (this.component.getExternalTextsTask() == null) {
                        this.component.setExternalTextsTask(
                                SchedulerManager.getInstance().getCachedPool().submit(() -> this.component.loadExternalTexts())
                        );

                        return;
                    }

                    if (this.component.getExternalTextsTask().isDone()) {
                        if (this.component.getExternalTextsTask().get()) {
                            this.loaderSteps.remove("load_external_texts");
                            this.loaderStepsFinished.add("load_external_texts");
                            this.progressLoader(20);
                        } else {
                            this.component.setExternalTextsTask(null);
                        }
                    }
                }
            }

            // Connect server
            if (this.loaderSteps.contains("connect_server")) {
                if (this.loaderStepsFinished.contains("load_external_texts")) {
                    if (NettyClient.getInstance().isConnected() ||
                            NettyClient.getInstance().getConnectionAttempts().get() >= PropertiesManager.getInstance().getInt("connection.max.attempts", 5)) {
                        this.loaderSteps.remove("connect_server");
                        this.loaderStepsFinished.add("connect_server");

                        if (NettyClient.getInstance().isConnected()) {
                            this.loadingLogoImage.setVisible(false);
                            this.progressLoader(40);

                            Movie.getInstance().createObject(new EntryView());
                        } else {
                            Movie.getInstance().createObject(new ErrorWindow(
                                    TextsManager.getInstance().getString("Alert_ConnectionNotReady"),
                                    TextsManager.getInstance().getString("Alert_ConnectionDisconnected"),
                                    true
                            ));
                            // Show error ??
                        }
                        return;
                    }

                    if (!NettyClient.getInstance().isConnected() &&
                            !NettyClient.getInstance().isConnecting()) {
                        this.component.connectServer();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleResize() {
        var loadingBarCords = DimensionUtil.getCenterCoords(this.loaderBarImage.getImage().getWidth(), this.loaderBarImage.getImage().getHeight());
        this.loaderBarImage.setX(loadingBarCords.getX());
        this.loaderBarImage.setY(DimensionUtil.roundEven(loadingBarCords.getY() + (loadingBarCords.getY() * 0.80)));

        this.loaderProgress.setX((int) this.loaderBarImage.getX() + 2);
        this.loaderProgress.setY((int) this.loaderBarImage.getY() + 2);

        var loadingLogoCords = DimensionUtil.getCenterCoords(this.loadingLogoImage.getImage().getWidth(), this.loadingLogoImage.getImage().getHeight());
        this.loadingLogoImage.setX(loadingLogoCords.getX());
        this.loadingLogoImage.setY(DimensionUtil.roundEven(loadingLogoCords.getY() - (loadingLogoCords.getY() * 0.20)));
    }

    private void dragging() {
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.loaderBar.setTranslateX(this.draggedX + this.loaderBar.getTranslateX() - this.mousePressedX);
            this.loaderBar.setTranslateY(this.draggedY + this.loaderBar.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }

    public int getTotalLoaderProgress() {
        return totalLoaderProgress;
    }
}