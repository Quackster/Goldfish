package com.classichabbo.goldfish.client.game.room.model;

public enum WallType {
    LEFT("wall_left.png"),
    RIGHT("wall_right.png"),
    LEFT_AND_RIGHT(""),
    /*DOOR_LEFT("door_left.png"),
    DOOR_RIGHT("door_right.png"),*/
    NONE("");

    private String fileName;

    WallType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}