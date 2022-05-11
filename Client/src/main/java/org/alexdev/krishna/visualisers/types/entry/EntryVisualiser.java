package org.alexdev.krishna.visualisers.types.entry;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.values.ValueType;
import org.alexdev.krishna.game.values.types.PropertiesManager;
import org.alexdev.krishna.game.GameUpdateLoop;
import org.alexdev.krishna.scripts.Cloud;
import org.alexdev.krishna.util.DateUtil;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.Component;
import org.alexdev.krishna.visualisers.Visualiser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EntryVisualiser extends Visualiser {
    private Pane pane;
    private Scene scene;
    private EntryComponent component;

    private boolean isInitialised;
    private long timeSinceStart;
    private long timeNextCloud;
    private int cloudTurnPoint = 330;

    private ImageView topRight;
    private ImageView bottomLeft;
    private ImageView bottomRight;
    private Rectangle stretchLeft;
    private Rectangle stretchRight;
    private ImageView sun;
    private List<Cloud> clouds;

    private Rectangle topReveal;
    private Rectangle bottomReveal;

    private static int MAX_VIEW_TIME = 1000;
    private long pViewOpenTime = 0;
    private long pViewCloseTime = 0;

    public static int CLOUD_Z_INDEX = 5000;

    public boolean queueOpenView = false;
    public boolean queueCloseView = false;
    private boolean queueAnimateSign = false;

    public EntryVisualiser() {
        this.timeSinceStart = (System.currentTimeMillis() / 1000L);
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.component = new EntryComponent(this);

        this.clouds = new ArrayList<>();
        this.cloudTurnPoint = PropertiesManager.getInstance().getInt("hotel.view.cloud.turn.point", 330);

        this.pane = new Pane();
        this.scene = HabboClient.getInstance().createScene(this.pane);

        this.topReveal = new Rectangle(1,1);
        this.topReveal.setFill(Color.BLACK);

        this.bottomReveal = new Rectangle(1,1);
        this.bottomReveal.setFill(Color.BLACK);

        this.topRight = new ImageView();
        this.topRight.setImage(new Image(PropertiesManager.getInstance().getString("hotel.view.image.top.right", ""), true));
        this.topRight.setY(this.topRight.getImage().getHeight() * -1);

        this.bottomLeft = new ImageView();
        this.bottomLeft.setImage(new Image(PropertiesManager.getInstance().getString("hotel.view.image.bottom.left", ""), true));

        this.bottomRight = new ImageView();
        this.bottomRight.setImage(new Image(PropertiesManager.getInstance().getString("hotel.view.image.bottom.right", ""), true));

        this.stretchLeft = new Rectangle(1,1);//= new ImageView();
        this.stretchLeft.setFill((Color) PropertiesManager.getInstance().getType("hotel.view.left.color", ValueType.COLOR_RGB, Color.rgb(117, 175, 203)));
        //this.stretchLeft.setImage(new Image(new File("resources/scenes/hotel_view/stretch_left.png").toURI().toString()));

        this.stretchRight = new Rectangle(1,1);
        this.stretchRight.setFill((Color) PropertiesManager.getInstance().getType("hotel.view.right.color", ValueType.COLOR_RGB, Color.rgb(139, 205, 233)));
        //this.stretchRight.setImage(new Image(new File("resources/scenes/hotel_view/stretch_right.png").toURI().toString()));

        this.sun = new ImageView();
        this.sun.setImage(new Image(PropertiesManager.getInstance().getString("hotel.view.image.sun")));
        this.sun.setX(DimensionUtil.getCenterCords(this.sun.getImage().getWidth(), this.sun.getImage().getHeight()).getX());

        this.pViewOpenTime = System.currentTimeMillis() + MAX_VIEW_TIME;
        this.bottomReveal.setY(HabboClient.getInstance().getPrimaryStage().getHeight() / 2);

        this.stretchBars();

        this.pane.getChildren().add(this.sun);
        this.pane.getChildren().add(this.stretchLeft);
        this.pane.getChildren().add(this.stretchRight);
        this.pane.getChildren().add(this.topRight);
        this.pane.getChildren().add(this.bottomRight);
        this.pane.getChildren().add(this.bottomLeft);
        this.pane.getChildren().add(this.bottomReveal);
        this.pane.getChildren().add(this.topReveal);

        this.stretchLeft.setViewOrder(7000);
        this.stretchRight.setViewOrder(7000);
        this.sun.setViewOrder(6000);
        this.bottomLeft.setViewOrder(4000);
        this.bottomRight.setViewOrder(3000);
        this.topRight.setViewOrder(2000);
        this.bottomReveal.setViewOrder(-1000);
        this.topReveal.setViewOrder(-1000);

        this.queueOpenView = true;
        this.queueAnimateSign = true;
        this.isInitialised = true;
    }

    /**
     * Update tick to resize the images and boxes necessary for responsiveness
     */
    public void update() {
        if (!this.isInitialised)
            return;

        if (this.queueOpenView) {
            this.openView();
        }

        if (this.queueAnimateSign) {
            this.animSign();
        }

        this.updateClouds();

        var mainWidth = DimensionUtil.getProgramWidth();
        var mainHeight = DimensionUtil.getProgramHeight();

        // Center da sun
        this.sun.setX(DimensionUtil.getCenterCords(this.sun.getImage().getWidth(), this.sun.getImage().getHeight()).getX());
        this.sun.setY(0);

        // Stretch the background - START
        this.stretchLeft.setHeight(mainHeight);
        this.stretchLeft.setWidth(this.cloudTurnPoint);

        this.stretchRight.setX(this.stretchLeft.getWidth());
        this.stretchRight.setWidth(mainWidth - this.stretchLeft.getWidth());
        this.stretchRight.setHeight(mainHeight);
        // Stretch the background - END

        // Set left view to... bottom left
        this.bottomLeft.setY(mainHeight - this.bottomLeft.getImage().getHeight());

        // Set right view to... bottom right
        this.bottomRight.setX(mainWidth - this.bottomRight.getImage().getWidth());
        this.bottomRight.setY(mainHeight - this.bottomRight.getImage().getHeight());

    }

    /**
     * Tick method for cloud animations
     */
    private void updateClouds() {
        for (var cloud : this.clouds) {
            cloud.update();

            if (cloud.isFinished()) {
                this.pane.getChildren().remove(cloud);
            }
        }

        // Remove old clouds
        this.clouds.removeIf(Cloud::isFinished);

        if (DateUtil.getCurrentTimeSeconds() > this.timeNextCloud && this.clouds.size() < 24) {
            int initX = 0;
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight()*0.66));

            this.component.addCloud("left", initX, initY);
            this.timeNextCloud = DateUtil.getCurrentTimeSeconds() + ThreadLocalRandom.current().nextInt(1, 10);
        }
    }

    /**
     * Animate Habbo Hotel sign
     */
    public void animSign() {
        // Only animate after the reveal bars have finished animating
        if (this.queueOpenView) {
            return;
        }

        if (!this.queueAnimateSign) {
            return;
        }

        if (this.topRight.getY() < 0) {
            var verticalPosition = 30;

            if (this.topRight.getY() == ((verticalPosition - 1) * -1)) {
                verticalPosition--; // Gotta get pixel perfect
            }

            this.topRight.setY(this.topRight.getY() + verticalPosition);
        } else {
            this.queueAnimateSign = false;
        }
    }

    /**
     * "openView" the reveal bars when the hotel view first loads
     *
     * source code ported from v14 Habbo client
     */
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
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / GameUpdateLoop.MAX_FPS);

        this.topReveal.setY(this.topReveal.getY() - tOffset);
        this.bottomReveal.setY(this.bottomReveal.getY() + tOffset);

        if (tOffset <= 0) {
            this.queueOpenView = false;
            this.viewTaskFinished();
        }
    }

    /**
     * Stretch the reveal bars across the window
     */
    private void stretchBars() {
        this.topReveal.setWidth(HabboClient.getInstance().getPrimaryStage().getWidth());
        this.topReveal.setHeight(HabboClient.getInstance().getPrimaryStage().getHeight() / 2);
        this.bottomReveal.setWidth(HabboClient.getInstance().getPrimaryStage().getWidth());
        this.bottomReveal.setHeight(HabboClient.getInstance().getPrimaryStage().getHeight() / 2);
    }

    /**
     * When the reveal task is finished, set these to invisible
     */
    private void viewTaskFinished() {
        this.topReveal.setVisible(false);
        this.bottomReveal.setVisible(false);
    }

    /**
     * Turn point for when the cloud turns
     */
    public int getCloudTurnPoint() {
        return cloudTurnPoint;
    }

    /**
     * List of active clouds
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public Pane getPane() {
        return pane;
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
