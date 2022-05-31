package com.classichabbo.goldfish.client.modules.types.room;

import com.classichabbo.goldfish.client.Goldfish;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.room.model.RoomBaseModel;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.controls.OffsetImageView;
import com.classichabbo.goldfish.client.modules.types.toolbars.RoomToolbar;

import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.util.DimensionUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class RoomView extends View {
    private Pane room;
    private Pane roomModel;

    private RoomCamera roomCamera;

    private ImageView roomLayout;
    private RoomComponent component;

    // Always set these as -1 initially
    private double draggedX = -1;
    private double draggedY = -1;

    private double mousePressedX = -1;
    private double mousePressedY = -1;
    private RoomBaseModel baseModel;

    public Pane getPane() {
        return room;
    }


    @Override
    public void start() {
        this.component = new RoomComponent();
        this.room = new Pane();

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

        this.drawModel();
        this.createRoomCamera();

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
        try {
            this.roomModel = new Pane();
            this.roomModel.setVisible(false);

            this.baseModel = Goldfish.getInstance().getGson().fromJson(
                    new String(ResourceManager.getInstance().getResource("assets/views/room/models/model_g.json").openStream().readAllBytes()),
                    RoomBaseModel.class
            );

            var hiliter = Arrays.stream(baseModel.getElements()).filter(x -> Objects.equals(
                    x.getType(), "hiliter")).findFirst().orElse(null);

            if (hiliter != null) {
                var highlighter = new ImageView(ResourceManager.getInstance().getFxImage("assets/views/room/parts/", hiliter.getMember() + ".png"));
                highlighter.setLayoutX(hiliter.getLocH());
                highlighter.setLayoutY(hiliter.getLocV());
                highlighter.setVisible(true);
                this.roomModel.getChildren().add(highlighter);
            }

            for (var element : baseModel.getElements()) {
                if (Objects.equals(element.getType(), "hiliter")) {
                    continue;
                }

                var obj = new OffsetImageView("assets/views/room/parts/", element.getMember(),
                element.getWidth(), element.getHeight(), element.getFlipH() == 1);
                obj.setLayoutX(element.getLocH());
                obj.setLayoutY(element.getLocV());

                /*if (element.getLocZ() < 0)
                    obj.setViewOrder(element.getLocZ());*/

                this.roomModel.getChildren().add(obj);

            }

            this.room.getChildren().add(this.roomModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (this.room.getWidth() > 0 && this.room.getHeight() > 0) {
                var centerPos = DimensionUtil.getCenterCoords(this.room.getWidth(), this.room.getHeight());

                roomCamera.setX((int) centerPos.getX());
                roomCamera.setY((int) centerPos.getY());

                this.room.setTranslateX(this.roomCamera.getX());
                this.room.setTranslateY(this.roomCamera.getY());

                this.roomModel.setVisible(true);
            }
        }
    }

}
