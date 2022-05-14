package org.alexdev.krishna.interfaces.types;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.alexdev.krishna.Movie;
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
    private Pane pane;
    private ImageView loadingBar;

    private int loaderProgress;
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
        this.pane = new Pane();
        this.pane.setPickOnBounds(false);
        
        this.loaderSteps = new ArrayList<>();
        this.loaderSteps.add("load_client_config");
        this.loaderSteps.add("load_external_variables");
        this.loaderSteps.add("load_external_texts");

        this.loaderProgress = 0;
        this.component = this.loaderVisualiser.getComponent();

        this.loadingBar = new ImageView();
        this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/loader_bar_0.png"));
        this.pane.getChildren().add(this.loadingBar);

        // this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.loadingBar.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.loadingBar.setOnMouseDragged(event -> {
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
        if (this.loaderProgress >= 100) {
            if (Movie.getInstance().getCurrentVisualiser() instanceof LoaderVisualiser) {
                Movie.getInstance().showVisualiser(VisualiserType.HOTEL_VIEW);
            }
        }

        updateLoader();
        handleResize();
        progressLoader();
        dragging();
    }

    private void dragging() {
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.pane.setTranslateX(this.draggedX + this.pane.getTranslateX() - this.mousePressedX);
            this.pane.setTranslateY(this.draggedY + this.pane.getTranslateY() - this.mousePressedY);

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
                        this.loaderProgress += 25;
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
                        this.loaderProgress += 50;
                    } else {
                        this.component.setExternalTextsTask(null);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.LOADING_BAR;
    }
}
