package com.classichabbo.goldfish.client.modules.types.room;

import com.classichabbo.goldfish.client.Goldfish;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.room.model.RoomModel;
import com.classichabbo.goldfish.client.game.room.model.Tile;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.controls.OffsetImageView;
import com.classichabbo.goldfish.client.modules.types.toolbars.RoomToolbar;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.util.DimensionUtil;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomView extends View {
    private View room;
    private RoomModel roomModel;

    private RoomCamera roomCamera;

    private ImageView roomLayout;
    private RoomComponent component;

    // Always set these as -1 initially
    private double draggedX = -1;
    private double draggedY = -1;

    private double mousePressedX = -1;
    private double mousePressedY = -1;
    private OffsetImageView doorMask;
    private OffsetImageView wallMask;
    private OffsetImageView door;
    private int modelWidth;
    private int modelHeight;

    public Pane getPane() {
        return room;
    }


    @Override
    public void start() {
        this.component = new RoomComponent();
        this.room = new View();

        //this.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

        /*this.roomLayout = new ImageView();
        this.roomLayout.setImage(ResourceManager.getInstance().getFxImage("assets/views/room", "room_test.png"));

        this.room.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.room.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        this.room.getChildren().add(this.roomLayout);*/
        this.getChildren().add(this.room);

        //var centerPos = DimensionUtil.getCenterCoords(this.roomLayout.getImage().getWidth(), this.roomLayout.getImage().getHeight());

        // Add loader bar to the interfaces, make it transition from loading to hotel view easily
        //Movie.getInstance().createObject(new Alert("Users online: 20\nDaily player peak count: 23\nList of users online:\n\nMyetz (Flash), Deku (Flash), Cup-A-Jo (Executable), Rods (Executable),\nRybak (Flash), tracemitch (Flash),\nfaas10 (Executable), kosov (Flash), fishterry (Flash), Freeroam (Flash), Kurt12 (Flash)\nAward (Flash), thom (Flash), Parsnip (Executable), zidro (Executable), Mario (Executable)\n\n"));
        //Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));
        //Movie.getInstance().createObject(new Alert("Give your room a name!"));

        Movie.getInstance().createObject(new RoomToolbar(), this);

        //this.room.setTranslateX(this.roomCamera.getX());
        //this.room.setTranslateY(this.roomCamera.getY());

        this.createRoomCamera();
        this.drawModel();

        this.registerUpdate();
        //this.addChild(new RoomToolbar());
    }

    private void createRoomCamera() {
        this.room.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        this.room.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
        });

        // var centerPos = DimensionUtil.getCenterCoords(DimensionUtil.getProgramWidth(), DimensionUtil.getProgramHeight());
        this.roomCamera = new RoomCamera(0, 0);

        Movie.getInstance().getPane().heightProperty().addListener(this.roomCamera);
        Movie.getInstance().getPane().widthProperty().addListener(this.roomCamera);
    }

    public void drawModel() {
        this.roomModel = new RoomModel("xxxxxxxxxxxx xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxx00000000 xxxxxxxxxxxx xxxxxxxxxxxx".split(" "), this.roomCamera);
        this.roomModel.setVisible(false);


        //this.roomModel.setPickOnBounds(false);
        this.roomModel.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

        Movie.getInstance().createObject(this.roomModel, this.room, false);
    }

    @Override
    public void stop() {
        super.stop();
        this.removeUpdate();

        Movie.getInstance().getPane().heightProperty().removeListener(this.roomCamera);
        Movie.getInstance().getPane().widthProperty().removeListener(this.roomCamera);
    }

    @Override
    public void registerUpdate() {
        // Queue to receive
        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void removeUpdate() {
        // Remove from update queue
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        this.dragging();
        this.roomCamera.update();

        this.room.setTranslateX(this.roomCamera.getX());
        this.room.setTranslateY(this.roomCamera.getY());
    }

    private void dragging() {
        this.roomModelCenter();

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

    private void roomModelCenter() {
        if (!this.roomModel.isVisible()) {
            var width = this.room.getWidth();
            var height = this.room.getHeight();

            if (width > 0 && height > 0) {
                var centerPos = DimensionUtil.getCenterCoords(width, height);

                this.roomCamera.setX((int) centerPos.getX());
                this.roomCamera.setY((int) centerPos.getY());

                this.room.setTranslateX(this.roomCamera.getX());
                this.room.setTranslateY(this.roomCamera.getY());

                this.roomModel.setVisible(true);
            }
        }
    }

}
