package com.classichabbo.goldfish.client.interfaces.types.room;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RoomTransition extends Interface {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void update() {

    }
    /*
    private VisualiserType visualiserType;
    private Rectangle blackBackground;
    private long timer;

    public RoomTransition(VisualiserType visualiserType) {
        this.visualiserType = visualiserType;
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
            if (this.visualiserType != null) {
                Movie.getInstance().showVisualiser(this.visualiserType);
                this.visualiserType = null;
            }
        }
    }

    @Override
    public void visualiserChanged(Visualiser previousVisualiser, Visualiser currentVisualiser) {
       if (previousVisualiser != null)
            this.remove();
    }

     */
}
