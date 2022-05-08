package org.alexdev.krishna.scenes.hotelview;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.alexdev.krishna.Krishna;
import org.alexdev.krishna.game.GameLoop;
import org.alexdev.krishna.scenes.HabboScene;
import org.alexdev.krishna.util.DateUtil;
import org.alexdev.krishna.util.DimensionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HotelViewManager extends HabboScene {
    private Pane pane;
    private Scene scene;
    private boolean isInitialised;
    private long timeSinceStart;

    private ImageView viewLeft;
    private ImageView viewRight;
    private ImageView stretchLeft;
    private ImageView stretchRight;
    private ImageView sun;
    private List<Cloud> clouds;

    private Rectangle topReveal;
    private Rectangle bottomReveal;

    private static int MAX_VIEW_TIME = 1000;
    private long pViewOpenTime = 0;
    private long pViewCloseTime = 0;

    public static int CLOUD_Z_INDEX = 4000;
    private static int TURN_POINT = 330;

    public boolean queueOpenView = false;
    public boolean queueCloseView = false;

    public HotelViewManager() {
        this.timeSinceStart = (System.currentTimeMillis() / 1000L);
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.clouds = new ArrayList<>();

        this.pane = new Pane();
        this.scene = HabboScene.create(this.pane);

        this.topReveal = new Rectangle(1,1);
        this.topReveal.setFill(Color.BLACK);

        this.bottomReveal = new Rectangle(1,1);
        this.bottomReveal.setFill(Color.BLACK);

        this.viewLeft = new ImageView();
        this.viewLeft.setImage(new Image(new File("resources/scenes/hotel_view/countries/au.png").toURI().toString()));

        this.viewRight = new ImageView();
        this.viewRight.setImage(new Image(new File("resources/scenes/hotel_view/us_right.png").toURI().toString()));

        this.stretchLeft = new ImageView();
        this.stretchLeft.setImage(new Image(new File("resources/scenes/hotel_view/stretch_left.png").toURI().toString()));

        this.stretchRight = new ImageView();
        this.stretchRight.setImage(new Image(new File("resources/scenes/hotel_view/stretch_right.png").toURI().toString()));

        this.sun = new ImageView();
        this.sun.setImage(new Image(new File("resources/scenes/hotel_view/sun.png").toURI().toString()));
        this.sun.setX(DimensionUtil.getCenterCords(this.sun.getImage().getWidth(), this.sun.getImage().getHeight()).getX());

        this.pViewOpenTime = System.currentTimeMillis() + MAX_VIEW_TIME;
        this.bottomReveal.setY(Krishna.getClient().getPrimaryStage().getHeight() / 2);

        this.stretchBars();

        this.pane.getChildren().add(this.sun);
        this.pane.getChildren().add(this.stretchLeft);
        this.pane.getChildren().add(this.stretchRight);
        this.pane.getChildren().add(this.viewRight);
        this.pane.getChildren().add(this.viewLeft);
        this.pane.getChildren().add(this.bottomReveal);
        this.pane.getChildren().add(this.topReveal);

        this.stretchLeft.setViewOrder(6000);
        this.stretchRight.setViewOrder(6000);
        this.sun.setViewOrder(6000);
        this.viewLeft.setViewOrder(3000);
        this.viewRight.setViewOrder(2000);
        this.bottomReveal.setViewOrder(-1000);
        this.topReveal.setViewOrder(-1000);

        this.queueOpenView = true;
        this.isInitialised = true;
    }

    public void updateTick() {
        this.updateClouds();
    }

    private void updateClouds() {
        for (var cloud : this.clouds) {
            cloud.update();

            if (cloud.isFinished()) {
                this.pane.getChildren().remove(cloud);
            }
        }

        // Remove old clouds
        this.clouds.removeIf(Cloud::isFinished);

        long timeDifference = DateUtil.getCurrentTimeSeconds() - this.timeSinceStart;
        if (timeDifference % 3 == 0 && this.clouds.size() < 1) {
            addCloud("cloud_0_left", 280, 120);
            addCloud("cloud_1_left", 280, 90);
            addCloud("cloud_2_left", 280, 60);
            addCloud("cloud_3_left", 280, 30);
        }
    }

    public void addCloud(String fileName, int initX, int initY)
    {
        var cloud = new Cloud(TURN_POINT, fileName, initX, initY);
        cloud.setViewOrder(CLOUD_Z_INDEX);
        cloud.init();
        
        this.clouds.add(cloud);
        this.pane.getChildren().add(cloud);
    }

    public void renderTick() {
        if (!this.isInitialised)
            return;

        if (this.queueOpenView) {
            this.openView();
        }

        var mainWidth = DimensionUtil.getProgramWidth();
        var mainHeight = DimensionUtil.getProgramHeight();

        // Center da sun
        this.sun.setX(DimensionUtil.getCenterCords(this.sun.getImage().getWidth(), this.sun.getImage().getHeight()).getX());
        this.sun.setY(0);

        // Stretch the background across entire screen
        if (this.stretchLeft.getFitHeight() != mainWidth) {
            this.stretchLeft.setFitHeight(mainWidth);
        }

        this.stretchRight.setX(DimensionUtil.getTopRight(this.stretchLeft));

        if (this.stretchRight.getFitWidth() != (mainWidth - this.stretchLeft.getImage().getWidth())) {
            this.stretchRight.setFitWidth(mainWidth - this.stretchLeft.getImage().getWidth());
        }

        if (this.stretchRight.getFitHeight() != mainHeight) {
            this.stretchRight.setFitHeight(mainHeight);
        }

        // Set left view to... bottom left
        this.viewLeft.setY(mainHeight - this.viewLeft.getImage().getHeight());

        // Set right view to... bottom right
        this.viewRight.setX(mainWidth - this.viewRight.getImage().getWidth());
        this.viewRight.setY(mainHeight - this.viewRight.getImage().getHeight());
    }

    public void openView() {
        if (!this.queueOpenView)
            return;

        this.topReveal.setVisible(true);
        this.bottomReveal.setVisible(true);

        // Magic when resizing window while reloading
        this.stretchBars();

        var tTimeLeft = ((MAX_VIEW_TIME - (System.currentTimeMillis() - this.pViewOpenTime)) / 1000);
        var tmoveLeft = (int) (this.topReveal.getHeight() - Math.abs(this.topReveal.getY()));
        var tOffset = 0;

        if (tTimeLeft <= 0)
            tOffset = Math.abs(tmoveLeft);
        else
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / GameLoop.MAX_FPS);

        this.topReveal.setY(this.topReveal.getY() - tOffset);
        this.bottomReveal.setY(this.bottomReveal.getY() + tOffset);

        if (tOffset <= 0) {
            this.queueOpenView = false;
            this.viewTaskFinished();
        }
    }

    private void stretchBars() {
        this.topReveal.setWidth(Krishna.getClient().getPrimaryStage().getWidth());
        this.topReveal.setHeight(Krishna.getClient().getPrimaryStage().getHeight() / 2);
        this.bottomReveal.setWidth(Krishna.getClient().getPrimaryStage().getWidth());
        this.bottomReveal.setHeight(Krishna.getClient().getPrimaryStage().getHeight() / 2);
    }

    private void viewTaskFinished() {
        this.topReveal.setVisible(false);
        this.bottomReveal.setVisible(false);
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
