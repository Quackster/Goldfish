package com.classichabbo.goldfish.client.interfaces.types.room;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.interfaces.types.alerts.Dialog;
import com.classichabbo.goldfish.client.interfaces.types.widgets.Navigator;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import com.classichabbo.goldfish.client.visualisers.Visualiser;
import com.classichabbo.goldfish.client.visualisers.VisualiserType;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Collections;
import java.util.stream.Collectors;

public class RoomTransition extends Interface {
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

        this.timer = System.currentTimeMillis() + 500;

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
}
