package com.classichabbo.goldfish.client.views.types.room;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.client.components.RoomViewComponent;
import com.classichabbo.goldfish.client.views.types.toolbars.RoomToolbar;

import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.util.DimensionUtil;

public class RoomView extends View {
    private Pane room;
    private RoomCamera roomCamera;

    private ImageView roomLayout;
    private RoomViewComponent component;

    // Always set these as -1 initially
    private double draggedX = -1;
    private double draggedY = -1;

    private double mousePressedX = -1;
    private double mousePressedY = -1;

    public Pane getPane() {
        return room;
    }

    private static void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
        System.out.println("testing 123");
    }

    @Override
    public void start() {
        this.component = new RoomViewComponent();
        this.room = new Pane();

        //this.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.roomLayout = new ImageView();
        this.roomLayout.setImage(ResourceManager.getInstance().getFxImage("sprites/scenes/room/room_test.png"));

        this.room.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.room.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        this.room.getChildren().add(this.roomLayout);
        this.getChildren().add(this.room);

        var centerPos = DimensionUtil.getCenterCoords(this.roomLayout.getImage().getWidth(), this.roomLayout.getImage().getHeight());
        this.roomCamera = new RoomCamera((int) centerPos.getX(), (int) centerPos.getY());

        this.room.setTranslateX(this.roomCamera.getX());
        this.room.setTranslateY(this.roomCamera.getY());

        Movie.getInstance().getPane().heightProperty().addListener(this.roomCamera);
        Movie.getInstance().getPane().widthProperty().addListener(this.roomCamera);

        // Add loader bar to the interfaces, make it transition from loading to hotel view easily
        //Movie.getInstance().createObject(new Alert("Users online: 20\nDaily player peak count: 23\nList of users online:\n\nMyetz (Flash), Deku (Flash), Cup-A-Jo (Executable), Rods (Executable),\nRybak (Flash), tracemitch (Flash),\nfaas10 (Executable), kosov (Flash), fishterry (Flash), Freeroam (Flash), Kurt12 (Flash)\nAward (Flash), thom (Flash), Parsnip (Executable), zidro (Executable), Mario (Executable)\n\n"));
        //Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));
        //Movie.getInstance().createObject(new Alert("Give your room a name!"));

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
        Movie.getInstance().createObject(new RoomToolbar(), this);
        //this.addChild(new RoomToolbar());
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);

        Movie.getInstance().getPane().heightProperty().removeListener(this.roomCamera);
        Movie.getInstance().getPane().widthProperty().removeListener(this.roomCamera);
    }

    @Override
    public void update() {
        this.dragging();
        this.roomCamera.update();

        this.room.setTranslateX(this.roomCamera.getX());
        this.room.setTranslateY(this.roomCamera.getY());
    }

    private void dragging() {
        if (this.roomCamera.isScrolling()) {
            this.draggedX = -1;
            this.draggedY = -1;
            return;
        }

        if (this.draggedX != -1 && this.draggedY != -1) {
            this.roomCamera.setX((int) (this.draggedX + this.room.getTranslateX() - this.mousePressedX));
            this.roomCamera.setY((int) (this.draggedY + this.room.getTranslateY() - this.mousePressedY));

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }

    public RoomViewComponent getComponent() {
        return component;
    }

}
