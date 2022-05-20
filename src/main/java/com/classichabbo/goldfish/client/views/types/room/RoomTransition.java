package com.classichabbo.goldfish.client.views.types.room;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RoomTransition extends View {
    private Runnable delegate;
    private Rectangle blackBackground;
    private long timer;

    public RoomTransition(Runnable delegate) {
        this.delegate = delegate;
    }

    @Override
    public void start() {
        this.blackBackground = new Rectangle(DimensionUtil.getProgramWidth(),DimensionUtil.getProgramHeight());
        this.blackBackground.setFill(Color.BLACK);
        this.getChildren().add(this.blackBackground);

        this.timer = System.currentTimeMillis() + 300;

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        this.blackBackground.setWidth(DimensionUtil.getProgramWidth());
        this.blackBackground.setHeight(DimensionUtil.getProgramHeight());
        this.toFront();

        if (System.currentTimeMillis() > this.timer) {
            if (this.delegate != null) {
                this.delegate.run();
                this.delegate = null;
            }
        }
    }
}
