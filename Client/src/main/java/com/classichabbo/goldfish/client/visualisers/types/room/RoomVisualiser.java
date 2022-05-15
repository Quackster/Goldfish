package com.classichabbo.goldfish.client.visualisers.types.room;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.visualisers.Visualiser;
import com.classichabbo.goldfish.client.visualisers.VisualiserType;
import com.classichabbo.goldfish.client.interfaces.types.Alert;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.util.DimensionUtil;

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

        this.scene = Movie.getInstance().createScene(this.pane);


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

        // Add loader bar to the interfaces, make it transition from loading to hotel view easily
        Movie.getInstance().createObject(new Alert("Users online: 20\nDaily player peak count: 23\nList of users online:\n\nMyetz (Flash), Deku (Flash), Cup-A-Jo (Executable), Rods (Executable),\nRybak (Flash), tracemitch (Flash),\nfaas10 (Executable), kosov (Flash), fishterry (Flash), Freeroam (Flash), Kurt12 (Flash)\nAward (Flash), thom (Flash), Parsnip (Executable), zidro (Executable), Mario (Executable)\n\n"));
        Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));
        Movie.getInstance().createObject(new Alert("Give your room a name!"));

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
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

    @Override
    public VisualiserType getType() {
        return VisualiserType.ROOM;
    }
}
