package org.alexdev.krishna.scenes.loader;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.alexdev.krishna.Krishna;
import org.alexdev.krishna.scenes.HabboScene;
import org.alexdev.krishna.scenes.HabboSceneType;
import org.alexdev.krishna.util.DateUtil;
import org.alexdev.krishna.util.DimensionUtil;

import java.io.File;

public class LoaderManager extends HabboScene {
    private Pane pane;
    private Scene scene;
    private boolean isInitialised;
    private long timeSinceStart;

    private ImageView loadingLogo;
    private ImageView loadingBar;
    private int ticked = -1;

    public LoaderManager() {
        this.timeSinceStart = DateUtil.getCurrentTimeSeconds();
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.pane = new Pane();
        this.scene = HabboScene.create(this.pane);

        this.loadingLogo = new ImageView();
        this.loadingLogo.setImage(new Image(new File("resources/scenes/loader/logo.png").toURI().toString()));
        this.loadingLogo.setPreserveRatio(true);

        this.loadingBar = new ImageView();
        this.loadingBar.setImage(new Image(new File("resources/scenes/loader/loader_bar_0.png").toURI().toString()));
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

    public void updateTick() {
        if (!this.isInitialised)
            return;

        long timeDifference = DateUtil.getCurrentTimeSeconds() - this.timeSinceStart;
        long tickInterval = 2; //

        if ((timeDifference % tickInterval) == 0) {
            this.ticked++;
            int progress = (timeDifference > 0 ? (int) ((timeDifference * 25) / tickInterval) : 0) + 50;

            if (progress <= 100 && (progress % 25) == 0) {
                Platform.runLater(() -> {
                    this.loadingBar.setVisible(true);
                    this.loadingBar.setImage(new Image(new File("resources/scenes/loader/loader_bar_" + progress + ".png").toURI().toString()));
                });
            }
        } else {
            int progress = timeDifference > 0 ? (int) ((timeDifference * 25) / tickInterval) : 0;

            if (progress == 112) {
                this.isInitialised = false;
                Platform.runLater(() -> Krishna.getClient().showStage(HabboSceneType.HOTEL_VIEW));
            }
        }
    }

    public void renderTick() {
        if (!this.isInitialised)
            return;

        /*if (isDragged) {
            this.logo.setX(X);
            this.logo.setY(Y);
        } else {*/

        var loadingLogoCords = DimensionUtil.getCenterCords(this.loadingLogo.getImage().getWidth(), this.loadingLogo.getImage().getHeight());
        this.loadingLogo.setX(loadingLogoCords.getX());
        this.loadingLogo.setY(DimensionUtil.roundEven(loadingLogoCords.getY() - (loadingLogoCords.getY() * 0.20)));

        var loadingBarCords = DimensionUtil.getCenterCords(this.loadingBar.getImage().getWidth(), this.loadingBar.getImage().getHeight());
        this.loadingBar.setX(loadingBarCords.getX());
        this.loadingBar.setY(DimensionUtil.roundEven(loadingBarCords.getY() + (loadingBarCords.getY() * 0.80)));

        //}
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
