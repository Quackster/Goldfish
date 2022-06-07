package com.classichabbo.goldfish.client.game.room.model;

public enum TileAttributes {
    BASIC_TILE("tile.png"),
    LOWER_STAIRS("stair_lower.png"),
    UPPER_STAIRS("stair_upper.png");

    private String fileName;

    TileAttributes(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}