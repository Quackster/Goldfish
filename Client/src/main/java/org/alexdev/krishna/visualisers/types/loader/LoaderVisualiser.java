package org.alexdev.krishna.visualisers.types.loader;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import org.alexdev.krishna.Movie;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.interfaces.types.Alert;
import org.alexdev.krishna.interfaces.types.LoadingBar;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.util.DimensionUtil;

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
        Movie.getInstance().createObject(new Alert("test 123"));

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
    public Scene getScene() {
        return scene;
    }
}
