package com.classichabbo.goldfish.client.interfaces.types.misc;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.scheduler.SchedulerManager;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import com.classichabbo.goldfish.client.visualisers.VisualiserType;
import com.classichabbo.goldfish.client.visualisers.types.loader.LoaderComponent;
import com.classichabbo.goldfish.client.visualisers.types.loader.LoaderVisualiser;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class LoadingBar extends Interface {
    private ImageView loaderBar;
    private Rectangle loaderProgress;

    private int totalLoaderProgress;
    private int queueLoaderProgress;

    private ArrayList<String> loaderSteps;

    private final LoaderVisualiser loaderVisualiser;
    private LoaderComponent component;

    private double draggedX;
    private double draggedY;

    private double mousePressedX;
    private double mousePressedY;

    public LoadingBar(LoaderVisualiser loaderVisualiser) {
        this.loaderVisualiser = loaderVisualiser;
    }

    @Override
    public void start() {
        // This fixes the issue by making transparent areas also mouse-transparent, however I would suggest not adding a child
        // ImageView and instead setting the background image of this (as in LoadingBar / this.setBackground) - feel free to
        // message me if you have any questions :) - Parsnip
        this.setPickOnBounds(false);

        this.queueLoaderProgress = 0;
        this.totalLoaderProgress = 0;
        
        this.loaderSteps = new ArrayList<>();
        this.loaderSteps.add("load_client_config");
        this.loaderSteps.add("load_external_variables");
        this.loaderSteps.add("load_external_texts");

        this.component = this.loaderVisualiser.getComponent();

        this.loaderBar = new ImageView();
        this.loaderBar.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/loader_bar_0.png"));
        this.getChildren().add(this.loaderBar);

        this.loaderProgress = new Rectangle(0, 12);
        this.loaderProgress.setFill(Color.web("#808080"));
        this.getChildren().add(this.loaderProgress);

        this.loaderBar.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.loaderBar.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        if (this.totalLoaderProgress >= 100) {
            if (Movie.getInstance().getCurrentVisualiser() instanceof LoaderVisualiser) {
                Movie.getInstance().showVisualiser(VisualiserType.HOTEL_VIEW);
                return;
            }
        }

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

            System.out.println(this.queueLoaderProgress);
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
                        this.progressLoader(25);
                    } else {
                        this.component.setClientConfigTask(null);
                    }
                }
            }

            // Load external variables
            if (this.loaderSteps.contains("load_external_variables")) {
                if (this.component.getExternalVariablesTask() == null) {
                    this.component.setExternalVariablesTask(
                            SchedulerManager.getInstance().getCachedPool().submit(() -> this.component.loadExternalVariables())
                    );

                    return;
                }

                if (this.component.getExternalVariablesTask().isDone()) {
                    if (this.component.getExternalVariablesTask().get()) {
                        this.loaderSteps.remove("load_external_variables");
                        this.progressLoader(25);
                    } else {
                        this.component.setExternalVariablesTask(null);
                    }
                }
            }

            // Load external variables
            if (this.loaderSteps.contains("load_external_texts")) {
                if (this.component.getExternalTextsTask() == null) {
                    this.component.setExternalTextsTask(
                            SchedulerManager.getInstance().getCachedPool().submit(() -> this.component.loadExternalTexts())
                    );

                    return;
                }

                if (this.component.getExternalTextsTask().isDone()) {
                    if (this.component.getExternalTextsTask().get()) {
                        this.loaderSteps.remove("load_external_texts");
                        this.progressLoader(50);
                    } else {
                        this.component.setExternalTextsTask(null);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleResize() {
        var loadingBarCords = DimensionUtil.getCenterCords(this.loaderBar.getImage().getWidth(), this.loaderBar.getImage().getHeight());
        this.loaderBar.setX(loadingBarCords.getX());
        this.loaderBar.setY(DimensionUtil.roundEven(loadingBarCords.getY() + (loadingBarCords.getY() * 0.80)));

        this.loaderProgress.setX((int) this.loaderBar.getX() + 2);
        this.loaderProgress.setY((int) this.loaderBar.getY() + 2);
    }

    private void dragging() {
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.setTranslateX(this.draggedX + this.getTranslateX() - this.mousePressedX);
            this.setTranslateY(this.draggedY + this.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }
}
