package com.classichabbo.goldfish.client.visualisers.types.loader;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.visualisers.Visualiser;
import com.classichabbo.goldfish.client.visualisers.VisualiserType;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.interfaces.types.misc.LoadingBar;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import com.classichabbo.goldfish.client.util.DimensionUtil;

public class LoaderVisualiser extends Visualiser {
    private LoaderComponent component;

    private Pane pane;
    private Scene scene;
    private ImageView loadingLogo;

    public LoaderVisualiser() {
        super();
    }

    @Override
    public void start() {
        this.component = new LoaderComponent();

        this.pane = new Pane();
        this.scene = Movie.getInstance().createScene(this.pane);

        this.loadingLogo = new ImageView();
        this.loadingLogo.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/loader/logo.png"));
        this.pane.getChildren().add(this.loadingLogo);

        // Add loader bar to the interfaces, make it transition from loading to hotel view easily
        Movie.getInstance().createObject(new LoadingBar(this));
        // Movie.getInstance().createObject(new Alert("test 123"));

        // Queue to receive
        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        this.handleResize();
    }

    private void handleResize() {
        var loadingLogoCords = DimensionUtil.getCenterCoords(this.loadingLogo.getImage().getWidth(), this.loadingLogo.getImage().getHeight());
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
    public Scene getScene() {
        return scene;
    }

    @Override
    public VisualiserType getType() {
        return VisualiserType.LOADER;
    }
}
