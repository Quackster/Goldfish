package com.classichabbo.goldfish.client.modules.types.entry;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.scheduler.types.GraphicsScheduler;
import com.classichabbo.goldfish.client.game.values.ValueType;
import com.classichabbo.goldfish.client.game.values.types.VariablesManager;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.types.loader.LoaderView;
import com.classichabbo.goldfish.client.modules.types.room.RoomTransition;
import com.classichabbo.goldfish.client.scripts.Cloud;
import com.classichabbo.goldfish.util.DateUtil;
import com.classichabbo.goldfish.util.DimensionUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class EntryView extends View {
    private final EntryHandler handler;
    private final EntryComponent component;

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

    private static int MAX_VIEW_TIME = 500;
    private long pViewOpenTime = 0;
    private long pViewCloseTime = 0;
    private AtomicInteger requiredSteps;

    public static int CLOUD_Z_INDEX = 5000;

    public boolean queueOpenView = false;
    public boolean queueCloseView = false;
    private boolean queueAnimateSign = false;
    private Runnable runAfterClosing;
    private Runnable runAfterOpening;

    public EntryView() {
        this.clouds = new ArrayList<>();
        this.component = new EntryComponent(this);
        this.handler = new EntryHandler(this);
    }

    @Override
    public void start() {
        this.requiredSteps = new AtomicInteger(3);
        this.cloudTurnPoint = VariablesManager.getInstance().getInt("hotel.view.cloud.turn.point", 330);

        this.topReveal = new Rectangle(1,1);
        this.topReveal.setFill(Color.BLACK);

        this.bottomReveal = new Rectangle(1,1);
        this.bottomReveal.setFill(Color.BLACK);

        this.topRight = new ImageView();
        this.bottomLeft = new ImageView();
        this.bottomRight = new ImageView();

        ResourceManager.getInstance().getWebImage(VariablesManager.getInstance().getString("hotel.view.image.top.right", ""), (x) -> {
            this.topRight.setImage(x);
            this.topRight.setY(this.topRight.getImage().getHeight() * -1);
            this.requiredSteps.decrementAndGet();
        });

        ResourceManager.getInstance().getWebImage(VariablesManager.getInstance().getString("hotel.view.image.bottom.left", ""), (x) -> {
            this.bottomLeft.setImage(x);
            this.requiredSteps.decrementAndGet();
        });

        ResourceManager.getInstance().getWebImage(VariablesManager.getInstance().getString("hotel.view.image.bottom.right", ""), (x) -> {
            this.bottomRight.setImage(x);
            this.requiredSteps.decrementAndGet();
        });

        this.stretchLeft = new Rectangle(1,1);//= new ImageView();
        this.stretchLeft.setFill((Color) VariablesManager.getInstance().getType("hotel.view.left.color", ValueType.COLOR_RGB, Color.rgb(117, 175, 203)));

        this.stretchRight = new Rectangle(1,1);
        this.stretchRight.setFill((Color) VariablesManager.getInstance().getType("hotel.view.right.color", ValueType.COLOR_RGB, Color.rgb(139, 205, 233)));

        this.sun = new ImageView();
        this.sun.setImage(new Image(VariablesManager.getInstance().getString("hotel.view.image.sun")));
        this.sun.setX(DimensionUtil.getCenterCoords(this.sun.getImage().getWidth(), this.sun.getImage().getHeight()).getX());
        this.bottomReveal.setY(Movie.getInstance().getPrimaryStage().getHeight() / 2);

        this.stretchBars();

        this.getChildren().add(this.sun);
        this.getChildren().add(this.stretchLeft);
        this.getChildren().add(this.stretchRight);
        this.getChildren().add(this.topRight);
        this.getChildren().add(this.bottomRight);
        this.getChildren().add(this.bottomLeft);
        this.getChildren().add(this.bottomReveal);
        this.getChildren().add(this.topReveal);

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

        //Movie.getInstance().createObject(new Alert("Users online: 20\nDaily player peak count: 23\nList of users online:\n\nMyetz (Flash), Deku (Flash), Cup-A-Jo (Executable), Rods (Executable),\nRybak (Flash), tracemitch (Flash),\nfaas10 (Executable), kosov (Flash), fishterry (Flash), Freeroam (Flash), Kurt12 (Flash)\nAward (Flash), thom (Flash), Parsnip (Executable), zidro (Executable), Mario (Executable)\n\n"));
        //Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb6101dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));
        //Movie.getInstance().createObject(new Alert("Your friend is offline."));
        //Movie.getInstance().createObject(new Alert("Give your room a name!"));
        //Movie.getInstance().createObject(new Alert("Your verification code is:\nQBqfv9cE"));

        if (Movie.getInstance().isViewActive(LoaderView.class)) {
            var loadingScreen = Movie.getInstance().getViewByClass(LoaderView.class);

            if (loadingScreen != null)
                loadingScreen.toFront();
        }

        if (Movie.getInstance().isViewActive(RoomTransition.class)) {
            var roomTransition = Movie.getInstance().getViewByClass(RoomTransition.class);

            if (roomTransition != null) {
                Movie.getInstance().removeObject(roomTransition);
            }
        }

        // Kickstart some clouds after turn point :^)
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 5) + 1; i++) {
            int initX = this.getCloudTurnPoint() + ThreadLocalRandom.current().nextInt(30, (int) (DimensionUtil.getProgramWidth() * 0.5));
            ;
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight() * 0.66));

            this.addCloud("right", initX, initY);
        }

        // Kickstart some clouds before turn point :^)
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 2) + 1; i++) {
            int initX = this.getCloudTurnPoint() - ThreadLocalRandom.current().nextInt(35, 60);
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight() * 0.66));

            this.addCloud("left", initX, initY);
        }

        // Always make this the backdrop
        this.toBack();

        // Register to update
        this.registerUpdate();
    }

    @Override
    public void stop() {
        this.removeUpdate();
    }

    @Override
    public void registerUpdate() {
        // Queue to receive
        Movie.getInstance().getGameScheduler().receiveUpdate(this);
    }

    @Override
    public void removeUpdate() {
        // Remove from update queue
        Movie.getInstance().getGameScheduler().removeUpdate(this);
    }

    /**
     * Update tick to resize the images and boxes necessary for responsiveness
     */
    public void update() {
        if (this.requiredSteps.get() > 0) {
            return; // images aren't downloaded yet
        }

        if (this.queueOpenView) {
            this.openView();
        }

        if (this.queueCloseView) {
            this.closeView();
        }

        if (this.queueAnimateSign) {
            this.animSign();
        }

        this.updateClouds();

        var mainWidth = DimensionUtil.getProgramWidth();
        var mainHeight = DimensionUtil.getProgramHeight();

        // Center da sun
        this.sun.setX(DimensionUtil.getCenterCoords(this.sun.getImage().getWidth(), this.sun.getImage().getHeight()).getX());
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
                this.getChildren().remove(cloud);
            }
        }

        // Remove old clouds
        this.clouds.removeIf(Cloud::isFinished);

        if (DateUtil.getCurrentTimeSeconds() > this.timeNextCloud && this.clouds.size() < 24) {
            int initX = 0;
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight()*0.66));

            this.addCloud("left", initX, initY);
            this.timeNextCloud = DateUtil.getCurrentTimeSeconds() + ThreadLocalRandom.current().nextInt(1, 10);
        }
    }

    /**
     * Add cloud handler
     */
    public void addCloud(String direction, int initX, int initY) {
        var cloud = new Cloud(this.getCloudTurnPoint(), "cloud_" + ThreadLocalRandom.current().nextInt(4), direction, initX, initY);
        cloud.setViewOrder(this.CLOUD_Z_INDEX);

        this.getClouds().add(cloud);
        this.getChildren().add(cloud);
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
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / GraphicsScheduler.MAX_FPS);

        this.topReveal.setY(this.topReveal.getY() - tOffset);
        this.bottomReveal.setY(this.bottomReveal.getY() + tOffset);

        if (tOffset <= 0) {
            this.queueOpenView = false;
            this.topReveal.setVisible(false);
            this.bottomReveal.setVisible(false);

            if (this.runAfterOpening != null)
                this.runAfterOpening.run();
        }
    }

    /**
     * "openView" the reveal bars when the hotel view first loads
     *
     * source code ported from v14 Habbo client
     */
    public void closeView() {
        if (!this.queueCloseView)
            return;

        this.topReveal.setVisible(true);
        this.bottomReveal.setVisible(true);

        // Magic when resizing window while reloading
        this.stretchBars();

        var tTimeLeft = ((MAX_VIEW_TIME - (System.currentTimeMillis() - this.pViewCloseTime)) / 1000);
        var tmoveLeft = (int) (0 - Math.abs(this.topReveal.getY()));
        var tOffset = 0;

        if (tTimeLeft <= 0)
            tOffset = Math.abs(tmoveLeft);
        else
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / GraphicsScheduler.MAX_FPS);

        this.topReveal.setY(this.topReveal.getY() + tOffset);
        this.bottomReveal.setY(this.bottomReveal.getY() - tOffset);

        if (tOffset <= 0) {
            this.queueCloseView = false;
            this.closeRevealTaskFinished();;
        }
    }

    public void setRunAfterClosing(Runnable run) {
        this.pViewCloseTime = System.currentTimeMillis() + (MAX_VIEW_TIME * 2);
        this.runAfterClosing = run;
        this.queueCloseView = true;

    }

    /**
     * Sets the callback after the entry has finished opening
     * @param runAfterOpening
     */
    public void setRunAfterOpening(Runnable runAfterOpening) {
        this.pViewOpenTime = System.currentTimeMillis() + (MAX_VIEW_TIME * 2);
        this.runAfterOpening = runAfterOpening;
        this.queueOpenView = true;
    }

    /**
     * Stretch the reveal bars across the window
     */
    private void stretchBars() {
        this.topReveal.setWidth(Movie.getInstance().getPrimaryStage().getWidth());
        this.topReveal.setHeight(Movie.getInstance().getPrimaryStage().getHeight() / 2);
        this.bottomReveal.setWidth(Movie.getInstance().getPrimaryStage().getWidth());
        this.bottomReveal.setHeight(Movie.getInstance().getPrimaryStage().getHeight() / 2);
    }

    /**
     * When the reveal task is finished, set these to invisible
     */
    private void closeRevealTaskFinished() {
        this.runAfterClosing.run();
        this.runAfterClosing = null;

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

    /**
     * Entry component
     * @return
     */
    @Override
    public EntryComponent getComponent() {
        return component;
    }

    @Override
    public EntryHandler getHandler() {
        return handler;
    }
}
