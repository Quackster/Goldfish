package org.alexdev.krishna.scenes.hotelview;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.alexdev.krishna.Krishna;
import org.alexdev.krishna.scenes.HabboScene;
import org.alexdev.krishna.util.DimensionUtil;

import java.io.File;

public class HotelViewManager extends HabboScene {
    private Pane pane;
    private Scene scene;
    private boolean isInitialised;
    private long timeSinceStart;

    private ImageView logo;

    public HotelViewManager() {
        this.timeSinceStart = (System.currentTimeMillis() / 1000L);
        this.pane = new Pane();
        this.scene = HabboScene.create(this.pane);
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.logo = new ImageView();
        this.logo.setImage(new Image(new File("resources/scenes/hotel_view/hotel_au.png").toURI().toString()));
        this.logo.setSmooth(false);
        this.pane.getChildren().add(logo);


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

    }

    public void renderTick() {
        if (!this.isInitialised)
            return;

        /*if (isDragged) {
            this.logo.setX(X);
            this.logo.setY(Y);
        } else {*/
        //}
        var cords = DimensionUtil.getCenterCords(this.logo.getImage().getWidth(), this.logo.getImage().getHeight());

        /*
        this.logo.setX(((int)(Krishna.getClient().getPrimaryStage().getWidth() * 0.05)));
        this.logo.setY(((int)(Krishna.getClient().getPrimaryStage().getHeight() * 0.05)));
        */
        this.logo.setX(cords.getX());
        this.logo.setY(cords.getY());
    }


    @Override
    public Scene getScene() {
        return scene;
    }
}
