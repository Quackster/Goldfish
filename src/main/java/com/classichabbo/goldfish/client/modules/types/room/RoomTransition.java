package com.classichabbo.goldfish.client.modules.types.room;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.util.DimensionUtil;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RoomTransition extends View {
    private final long delaySeconds;
    private Runnable runAfterFinished;
    private Rectangle blackBackground;
    private long timer;

    public RoomTransition() {
        this.delaySeconds = 300;
    }

    public RoomTransition(long delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    @Override
    public void start() {
        this.blackBackground = new Rectangle(DimensionUtil.getProgramWidth(),DimensionUtil.getProgramHeight());
        this.blackBackground.setFill(Color.BLACK);
        this.getChildren().add(this.blackBackground);

        this.timer = System.currentTimeMillis() + delaySeconds;
        this.registerUpdate();
    }

    @Override
    public void stop() {
        super.stop();
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

    @Override
    public void update() {
        this.blackBackground.setWidth(DimensionUtil.getProgramWidth());
        this.blackBackground.setHeight(DimensionUtil.getProgramHeight());
        this.toFront();

        if (System.currentTimeMillis() > this.timer) {
            if (this.runAfterFinished != null) {
                this.runAfterFinished.run();
                this.runAfterFinished = null;
            }
        }
    }

    public void setRunAfterFinished(Runnable runAfterFinished) {
        this.runAfterFinished = runAfterFinished;
    }
}
