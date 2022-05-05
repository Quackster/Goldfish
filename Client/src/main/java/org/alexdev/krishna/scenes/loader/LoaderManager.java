package org.alexdev.krishna.scenes.loader;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.alexdev.krishna.Krishna;
import org.alexdev.krishna.scenes.HabboScene;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.File;

public class LoaderManager extends HabboScene {
    private static double X;
    private static double Y;
    private Pane pane;
    private Scene scene;
    private boolean isInitialised;

    private ImageView logo;
    private boolean isDragged;

    public LoaderManager() {
        this.pane = new Pane();
        this.scene = HabboScene.create(this.pane);
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.logo = new ImageView();
        this.logo.setImage(new Image(new File("resources/scenes/loader/logo.png").toURI().toString()));

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



    public void renderTick() {
        if (!this.isInitialised)
            return;

        /*if (isDragged) {
            this.logo.setX(X);
            this.logo.setY(Y);
        } else {*/
        this.logo.setX((Krishna.getClient().getPrimaryStage().getWidth() / 2) - this.logo.getImage().getHeight());
        this.logo.setY((Krishna.getClient().getPrimaryStage().getHeight() / 2) - this.logo.getImage().getWidth());
        //}
    }


    @Override
    public Scene getScene() {
        return scene;
    }
}
