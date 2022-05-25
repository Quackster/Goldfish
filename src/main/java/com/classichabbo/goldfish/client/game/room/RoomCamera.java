package com.classichabbo.goldfish.client.game.room;

import com.classichabbo.goldfish.util.DimensionUtil;

public class RoomCamera {
    private static RoomCamera instance;
    private int x;
    private int y;

    public RoomCamera() {
        this.reset();
    }

    public void reset() {
        this.x = (int) (DimensionUtil.getProgramWidth() / 2);
        this.y = (int) (DimensionUtil.getProgramHeight() / 2);
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public static RoomCamera getInstance() {
        if (instance == null) {
            instance = new RoomCamera();
        }

        return instance;
    }
}
