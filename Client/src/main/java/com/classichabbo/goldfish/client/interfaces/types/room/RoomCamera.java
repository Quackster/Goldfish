package com.classichabbo.goldfish.client.interfaces.types.room;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.scheduler.types.InterfaceScheduler;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RoomCamera implements ChangeListener<Number> {
    private static final int MAX_CAMERA_PAN_TIME = 1000;

    private int x;
    private int y;

    private int panTargetX;
    private int panTargetY;

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
            if (this.scrollingX) {
                moveX();
            }

            if (this.scrollingY) {
                moveY();
            }
        }
    }

    private void moveX() {
        var tTimeLeft = ((MAX_CAMERA_PAN_TIME - (System.currentTimeMillis() - (this.timePanX + MAX_CAMERA_PAN_TIME))) / 1000);
        var tmoveLeft = Math.abs(this.x - this.panTargetX);
        var tOffset = 0;

        if (tTimeLeft <= 0)
            tOffset = (int) Math.abs(tmoveLeft);
        else
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / InterfaceScheduler.MAX_FPS);

        if (this.x < this.panTargetX) {
            this.x += tOffset;
        } else {
            this.x -= tOffset;
        }

        if (tOffset == 0) {
            this.scrollingX = false;
        }
    }

    private void moveY() {
        var tTimeLeft = ((MAX_CAMERA_PAN_TIME - (System.currentTimeMillis() - (this.timePanY + MAX_CAMERA_PAN_TIME))) / 1000);
        var tmoveLeft = Math.abs(this.y - this.panTargetY);
        var tOffset = 0;

        if (tTimeLeft <= 0)
            tOffset = (int) Math.abs(tmoveLeft);
        else
            tOffset = (int) (Math.abs((tmoveLeft / tTimeLeft)) / InterfaceScheduler.MAX_FPS);

        if (this.y < this.panTargetY) {
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
     *
     * Pan the room camera back to the start when the window is resized.
     */
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (Movie.getInstance().isInterfaceActive(RoomView.class)) {
            var roomView = Movie.getInstance().getInterfaceByClass(RoomView.class);

            if (roomView == null) {
                return;
            }

            var coords = DimensionUtil.getCenterCoords(roomView.getPane().getWidth(), roomView.getPane().getHeight());
            this.panCameraTo((int) coords.getX(), (int) coords.getY());
        }
    }

    private void panCameraTo(int x, int y) {
        if (this.x != x) {
            this.panTargetX = x;
            this.scrollingX = true;
            this.timePanX = System.currentTimeMillis();
        }

        if (this.y != x) {
            this.panTargetY = y;
            this.scrollingY = true;
            this.timePanY = System.currentTimeMillis();
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
