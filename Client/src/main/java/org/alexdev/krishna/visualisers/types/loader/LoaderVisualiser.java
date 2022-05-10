package org.alexdev.krishna.visualisers.types.loader;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.values.types.PropertiesManager;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.util.libraries.ClientLoadStep;
import org.alexdev.krishna.visualisers.Component;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.util.DateUtil;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.VisualiserType;

import java.util.HashMap;
import java.util.Map;

public class LoaderVisualiser extends Visualiser {
    private LoaderComponent component;

    private Pane pane;
    private Scene scene;
    private boolean isInitialised;
    private long timeSinceStart;

    private ImageView loadingLogo;
    private ImageView loadingBar;
    private int loaderProgress;
    private Map<String, ClientLoadStep> loaderSteps;

    public LoaderVisualiser() {
        this.timeSinceStart = DateUtil.getCurrentTimeSeconds();
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.component = new LoaderComponent();

        this.loaderProgress = 0;
        this.loaderSteps = new HashMap<>();
        this.loaderSteps.put("load_client_config", LoaderComponent::loadClientConfig);
        this.loaderSteps.put("load_external_variables", LoaderComponent::loadExternalVariables);

        this.pane = new Pane();
        this.scene = Visualiser.create(this.pane);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.loadingLogo = new ImageView();
        //this.loadingLogo.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/logo.png"));
        this.loadingLogo.setPreserveRatio(true);

        this.loadingBar = new ImageView();
        //this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/loader_bar_0.png"));
        this.loadingBar.setVisible(false);
        this.loadingBar.setPreserveRatio(true);

        this.pane.getChildren().add(this.loadingLogo);
        this.pane.getChildren().add(this.loadingBar);


        //Creating the mouse event handler
        /*
        this.logo.setOnMousePressed(x -> {
            isDragged = true;
        });

        this.logo.setOnMouseReleased(x -> {
            isDragged = false;
        });

        this.scene.setOnMouseMoved(x -> {
            X = x.getSceneX();
            Y = x.getSceneY();
        });

        this.scene.setOnMouseDragged(x -> {
            X = x.getSceneX();
            Y = x.getSceneY();
        });

        this.scene.setOnMouseDragged(x -> {
            X = x.getSceneX();
            Y = x.getSceneY();
        });
         */

        //this.isInitialised = true;

    }

    public void update() {
        if (!this.isInitialised)
            return;

        this.handleResize();
        this.updateLoader();
        this.progressLoader();
    }

    private void updateLoader() {
        if (this.loaderProgress >= 0) {
            this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/loader_bar_" + this.loaderProgress + ".png"));
        }

        if (this.loaderProgress == 100) {
            this.isInitialised = false;
            Platform.runLater(() -> HabboClient.getInstance().showVisualiser(VisualiserType.HOTEL_VIEW));
        }
    }

    private void handleResize() {
        var loadingLogoCords = DimensionUtil.getCenterCords(this.loadingLogo.getImage().getWidth(), this.loadingLogo.getImage().getHeight());
        this.loadingLogo.setX(loadingLogoCords.getX());
        this.loadingLogo.setY(DimensionUtil.roundEven(loadingLogoCords.getY() - (loadingLogoCords.getY() * 0.20)));

        var loadingBarCords = DimensionUtil.getCenterCords(this.loadingBar.getImage().getWidth(), this.loadingBar.getImage().getHeight());
        this.loadingBar.setX(loadingBarCords.getX());
        this.loadingBar.setY(DimensionUtil.roundEven(loadingBarCords.getY() + (loadingBarCords.getY() * 0.80)));
    }

    private void progressLoader() {
        /*
        if (PropertiesManager.getInstance().isFinished()) {

            this.loaderProgress += 100;
        } else {

        }*/

        new Thread(() -> {
            //if (PropertiesManager.getInstance() == null)
            //    PropertiesManager.init();
        }).start();
    }

    @Override
    public LoaderComponent getComponent() {
        return this.component;
    }

    @Override
    public boolean isReady() {
        return isInitialised;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
