package com.classichabbo.goldfish.client.game.room.model;

public class RoomBaseModel {
    private String heightmap;
    private int[] rect;
    private int[] border;
    private RoomModelData roomData;
    private RoomModelElement[] elements;

    public String getHeightmap() {
        return heightmap;
    }

    public int[] getRect() {
        return rect;
    }

    public int[] getBorder() {
        return border;
    }

    public RoomModelData getRoomData() {
        return roomData;
    }

    public RoomModelElement[] getElements() {
        return elements;
    }
}
