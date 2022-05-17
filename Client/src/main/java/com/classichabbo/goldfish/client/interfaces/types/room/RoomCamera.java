package com.classichabbo.goldfish.client.interfaces.types.room;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.scheduler.types.GraphicsScheduler;
import com.classichabbo.goldfish.client.game.scheduler.types.InterfaceScheduler;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import io.netty.handler.ssl.SslCloseCompletionEvent;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;

public class RoomCamera implements ChangeListener<Number> {
    private static final int MAX_VIEW_TIME = 1000;
    private int x;
    private int y;

    private boolean scrollingY;
    private boolean scrollingX;
    private long timePanX;
    private long timePanY;

    public RoomCamera(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (this.scrollingX || this.scrollingY) {
            var roomView = Movie.getInstance().getInterfaceByClass(RoomView.class);
            var coords = DimensionUtil.getCenterCoords(roomView.getPane().getWidth(), roomView.getPane().getHeight());

            if (this.scrollingX) {
                moveX(coords);
            }

            if (this.scrollingY) {
                moveY(coords);
            }
        }
    }

    private void moveX(Point2D coords) {
        var tTimeLeft = ((MAX_VIEW_TIME - (System.currentTimeMillis() - (this.timePanX + MAX_VIEW_TIME))) / 1000);
        var tmoveLeft = Math.abs(this.x - coords.getX());
        var tOffset = 0;

        if (tTimeLeft <= 0)
            tOffset = (int) Math.abs(tmoveLeft);
        else
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / InterfaceScheduler.MAX_FPS);

        if (this.x < coords.getX()) {
            this.x += tOffset;
        } else {
            this.x -= tOffset;
        }

        if (tOffset == 0) {
            this.scrollingX = false;
        }
    }

    private void moveY(Point2D coords) {
        var tTimeLeft = ((MAX_VIEW_TIME - (System.currentTimeMillis() - (this.timePanY + MAX_VIEW_TIME))) / 1000);
        var tmoveLeft = Math.abs(this.y - coords.getY());
        var tOffset = 0;

        if (tTimeLeft <= 0)
            tOffset = (int) Math.abs(tmoveLeft);
        else
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / InterfaceScheduler.MAX_FPS);

        if (this.y < coords.getY()) {
            this.y += tOffset;
        } else {
            this.y -= tOffset;
        }

        if (tOffset == 0) {
            this.scrollingY = false;
        }
    }

    /**
     * Handler for when window resizing.
     */
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (this.scrollingX || this.scrollingY) {
            return;
        }

        if (Movie.getInstance().isInterfaceActive(RoomView.class)) {
            var roomView = Movie.getInstance().getInterfaceByClass(RoomView.class);

            if (roomView == null) {
                return;
            }

            var coords = DimensionUtil.getCenterCoords(roomView.getPane().getWidth(), roomView.getPane().getHeight());

            if (this.x != coords.getX()) {
                this.scrollingX = true;
                this.timePanX = System.currentTimeMillis();
            }

            if (this.y != coords.getY()) {
                this.scrollingY = true;
                this.timePanY = System.currentTimeMillis();
            }

            //this.x = (int) coords.getX();
            //this.y = (int) coords.getY();
        }
    }

    public boolean isScrolling() {
        return this.scrollingX || this.scrollingY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
