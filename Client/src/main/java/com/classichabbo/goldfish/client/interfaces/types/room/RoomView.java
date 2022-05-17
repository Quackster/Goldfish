package com.classichabbo.goldfish.client.interfaces.types.room;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.interfaces.types.toolbars.RoomToolbar;

import javafx.beans.InvalidationListener;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.util.DimensionUtil;

public class RoomView extends Interface {
    private Pane room;

    private double draggedX;
    private double draggedY;

    private double mousePressedX;
    private double mousePressedY;

    private ImageView roomLayout;
    private RoomViewComponent component;
    private InvalidationListener resizeListener;

    @Override
    public void start() {
        this.component = new RoomViewComponent();
        this.room = new Pane();

        this.roomLayout = new ImageView();
        this.roomLayout.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/room/room_test.png"));

        var centerPos = DimensionUtil.getCenterCoords(this.roomLayout.getImage().getWidth(), this.roomLayout.getImage().getHeight());
        this.roomLayout.setX(centerPos.getX());
        this.roomLayout.setY(centerPos.getY());

        this.roomLayout.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.roomLayout.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        this.room.getChildren().add(this.roomLayout);
        this.getChildren().add(this.room);

        /*
        this.room.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("testing 123");
        });

        this.room.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("testing 456");
        });
         */

        // Add loader bar to the interfaces, make it transition from loading to hotel view easily
        //Movie.getInstance().createObject(new Alert("Users online: 20\nDaily player peak count: 23\nList of users online:\n\nMyetz (Flash), Deku (Flash), Cup-A-Jo (Executable), Rods (Executable),\nRybak (Flash), tracemitch (Flash),\nfaas10 (Executable), kosov (Flash), fishterry (Flash), Freeroam (Flash), Kurt12 (Flash)\nAward (Flash), thom (Flash), Parsnip (Executable), zidro (Executable), Mario (Executable)\n\n"));
        //Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));
        //Movie.getInstance().createObject(new Alert("Give your room a name!"));

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
        Movie.getInstance().createObject(new RoomToolbar());
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.setTranslateX(this.draggedX + this.getTranslateX() - this.mousePressedX);
            this.setTranslateY(this.draggedY + this.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }

    public RoomViewComponent getComponent() {
        return component;
    }

}
