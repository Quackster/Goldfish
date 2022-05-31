package com.classichabbo.goldfish.client.game.room.model;

public class RoomModelElement {
    private String id;
    private String type;
    private String member;
    private String media;
    private int locH;
    private int locV;
    private int ink;
    private int blend;
    private int width;
    private int height;
    private String palette;
    private String wrapperID;
    private int flipH;
    private int catchEvents;
    private int locX;
    private int locY;
    private int locZ = 1;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getMember() {
        return member;
    }

    public String getMedia() {
        return media;
    }

    public int getLocH() {
        return locH;
    }

    public int getLocV() {
        return locV;
    }

    public int getFlipH() {
        return flipH;
    }

    public int getInk() {
        return ink;
    }

    public int getBlend() {
        return blend;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getPalette() {
        return palette;
    }

    public String getWrapperID() {
        return wrapperID;
    }

    public int getCatchEvents() {
        return catchEvents;
    }

    public int getLocX() {
        return locX;
    }

    public int getLocY() {
        return locY;
    }

    public int getLocZ() {
        return locZ;
    }
}
