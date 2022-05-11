package org.alexdev.krishna.interfaces.types;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.game.scheduler.SchedulerManager;
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.interfaces.InterfaceType;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.VisualiserType;
import org.alexdev.krishna.visualisers.types.loader.LoaderComponent;
import org.alexdev.krishna.visualisers.types.loader.LoaderVisualiser;

import java.util.ArrayList;

public class LoadingBar extends Interface {
    private ImageView loadingBar;

    private int loaderProgress;
    private ArrayList<String> loaderSteps;

    private final LoaderVisualiser loaderVisualiser;
    private LoaderComponent component;

    private boolean isInitialised;

    private double draggedX;
    private double draggedY;

    private double mousePressedX;
    private double mousePressedY;

    public LoadingBar(LoaderVisualiser loaderVisualiser) {
        this.loaderVisualiser = loaderVisualiser;
    }

    @Override
    public void init() {
        if (this.isInitialised) {
            return;
        }

        this.loaderSteps = new ArrayList<>();
        this.loaderSteps.add("load_client_config");
        this.loaderSteps.add("load_external_variables");
        this.loaderSteps.add("load_external_texts");

        this.loaderProgress = 0;
        this.component = this.loaderVisualiser.getComponent();

        this.loadingBar = new ImageView();
        this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/loader_bar_0.png"));
        this.getChildren().add(this.loadingBar);

        // this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.loadingBar.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.loadingBar.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        this.sceneChanged();
        this.isInitialised = true;
    }

    @Override
    public void sceneChanged() {
         this.setViewOrder(-2000);
    }

    @Override
    public boolean isReady() {
        return isInitialised;
    }

    @Override
    public void update() {
        if (!this.isInitialised)
            return;

        if (this.loaderProgress >= 75) {
            if (HabboClient.getInstance().getCurrentVisualiser() instanceof LoaderVisualiser)
                Platform.runLater(() -> HabboClient.getInstance().showVisualiser(VisualiserType.HOTEL_VIEW));
        }

        updateLoader();
        handleResize();
        progressLoader();
        dragging();
    }

    private void dragging() {
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.setTranslateX(this.draggedX + this.getTranslateX() - this.mousePressedX);
            this.setTranslateY(this.draggedY + this.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }

    private void updateLoader() {
        if (this.loaderProgress >= 0) {
            this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/loader_bar_" + this.loaderProgress + ".png"));
        }
    }

    private void handleResize() {
        var loadingBarCords = DimensionUtil.getCenterCords(this.loadingBar.getImage().getWidth(), this.loadingBar.getImage().getHeight());
        this.loadingBar.setX(loadingBarCords.getX());
        this.loadingBar.setY(DimensionUtil.roundEven(loadingBarCords.getY() + (loadingBarCords.getY() * 0.80)));
    }

    private void progressLoader() {
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
                        this.loaderProgress += 25;
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
                        this.loaderProgress += 25;
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
                        this.loaderProgress += 25;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.LOADING_BAR;
    }
}
