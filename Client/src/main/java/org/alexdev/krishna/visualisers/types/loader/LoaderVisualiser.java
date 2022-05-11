package org.alexdev.krishna.visualisers.types.loader;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.interfaces.types.Alert;
import org.alexdev.krishna.interfaces.types.LoadingBar;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.util.DimensionUtil;

public class LoaderVisualiser extends Visualiser {
    private LoaderComponent component;

    private Pane pane;
    private Scene scene;
    private boolean isInitialised;
    private ImageView loadingLogo;

    public LoaderVisualiser() {
        super();
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.component = new LoaderComponent();

        this.pane = new Pane();
        this.scene = HabboClient.getInstance().createScene(this.pane);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.loadingLogo = new ImageView();
        this.loadingLogo.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/logo.png"));
        this.pane.getChildren().add(this.loadingLogo);

        // Add loader bar to the interfaces, make it transition from loading to hotel view easily
        HabboClient.getInstance().queue(new LoadingBar(this));
        HabboClient.getInstance().queue(new Alert("Notice!"));
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

        this.isInitialised = true;

    }

    @Override
    public void update() {
        if (!this.isInitialised)
            return;


        this.handleResize();
    }

    private void handleResize() {
        var loadingLogoCords = DimensionUtil.getCenterCords(this.loadingLogo.getImage().getWidth(), this.loadingLogo.getImage().getHeight());
        this.loadingLogo.setX(loadingLogoCords.getX());
        this.loadingLogo.setY(DimensionUtil.roundEven(loadingLogoCords.getY() - (loadingLogoCords.getY() * 0.20)));
    }

    @Override
    public LoaderComponent getComponent() {
        return this.component;
    }

    @Override
    public Pane getPane() {
        return pane;
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
