package org.alexdev.krishna.visualisers.types;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.visualisers.VisualiserType;
import org.alexdev.krishna.util.DateUtil;
import org.alexdev.krishna.util.DimensionUtil;

import java.io.File;

public class LoaderVisualiser extends Visualiser {
    private Pane pane;
    private Scene scene;
    private boolean isInitialised;
    private long timeSinceStart;

    private ImageView loadingLogo;
    private ImageView loadingBar;
    private int ticked = -1;

    public LoaderVisualiser() {
        this.timeSinceStart = DateUtil.getCurrentTimeSeconds();
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.pane = new Pane();
        this.scene = Visualiser.create(this.pane);
        this.pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.loadingLogo = new ImageView();
        this.loadingLogo.setImage(ResourceManager.getInstance().getFxImage("scenes/loader/logo.png"));
        this.loadingLogo.setPreserveRatio(true);

        this.loadingBar = new ImageView();
        this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("scenes/loader/loader_bar_0.png"));
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

        this.isInitialised = true;
    }

    public void update() {
        if (!this.isInitialised)
            return;

        this.handleResize();
        this.progressLoader();
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
        long timeDifference = DateUtil.getCurrentTimeSeconds() - this.timeSinceStart;
        long tickInterval = 2; //

        if ((timeDifference % tickInterval) == 0) {
            this.ticked++;
            int progress = (timeDifference > 0 ? (int) ((timeDifference * 25) / tickInterval) : 0) + 50;

            if (progress <= 100 && (progress % 25) == 0) {
                Platform.runLater(() -> {
                    this.loadingBar.setVisible(true);
                    this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("scenes/loader/loader_bar_" + progress + ".png"));
                });
            }
        } else {
            int progress = timeDifference > 0 ? (int) ((timeDifference * 25) / tickInterval) : 0;

            if (progress == 112) {
                this.isInitialised = false;
                Platform.runLater(() -> HabboClient.getInstance().showVisualiser(VisualiserType.HOTEL_VIEW));
            }
        }
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
