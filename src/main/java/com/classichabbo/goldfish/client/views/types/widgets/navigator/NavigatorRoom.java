package com.classichabbo.goldfish.client.views.types.widgets.navigator;

public class NavigatorRoom {
    public int roomId;
    public String name;
    public String owner;
    public String description;
    public String infoImg;
    public int visitors;
    public int maxVisitors;
    public Doorbell doorbell;
    public Boolean isPublic;

    public NavigatorRoom(int roomId, String name, String description, String infoImg, int visitors, int maxVisitors) {
        this.roomId = roomId;
        this.name = name;
        this.description = description;
        this.infoImg = infoImg;
        this.visitors = visitors;
        this.maxVisitors = maxVisitors;
        this.isPublic = true;
    }

    public NavigatorRoom(int id, String name, String owner, String description, int visitors, int maxVisitors, Doorbell doorbell) {
        this.roomId = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.visitors = visitors;
        this.maxVisitors = maxVisitors;
        this.doorbell = doorbell;
        this.isPublic = false;
    }
}
