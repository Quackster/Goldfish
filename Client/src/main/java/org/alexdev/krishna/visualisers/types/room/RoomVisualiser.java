package org.alexdev.krishna.visualisers.types.room;

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
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.Component;
import org.alexdev.krishna.visualisers.Visualiser;

public class RoomVisualiser extends Visualiser {
    private Pane pane;
    private Pane room;

    private double draggedX;
    private double draggedY;

    private double mousePressedX;
    private double mousePressedY;

    private Scene scene;

    private ImageView loadingBar;
    private RoomComponent component;

    @Override
    public void start() {
        this.component = new RoomComponent(this);

        this.pane = new Pane();
        this.room = new Pane();

        this.scene = HabboClient.getInstance().createScene(this.pane);


        this.loadingBar = new ImageView();
        this.loadingBar.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/room/room_test.png"));

        var centerPos = DimensionUtil.getCenterCords(this.loadingBar.getImage().getWidth(), this.loadingBar.getImage().getHeight());
        this.loadingBar.setX(centerPos.getX());
        this.loadingBar.setY(centerPos.getY());

        this.loadingBar.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.loadingBar.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        this.room.getChildren().add(this.loadingBar);
        this.pane.getChildren().add(this.room);

        this.scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("testing 123");
        });

        this.scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("testing 456");
        });

        HabboClient.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        HabboClient.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.room.setTranslateX(this.draggedX + this.room.getTranslateX() - this.mousePressedX);
            this.room.setTranslateY(this.draggedY + this.room.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }

    @Override
    public RoomComponent getComponent() {
        return component;
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
