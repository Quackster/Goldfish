package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import java.util.ArrayList;
import java.util.List;

public class NavigatorNode {
    private int id;
    private boolean hideFull;
    private int nodeType;
    private String name;
    private int usercount;
    private int maxUsers;
    private int parentid;
    private String unitStrId;
    private int port;
    private String door;
    private String casts;
    private int usersInQueue;
    private boolean isVisible;
    private int flatId;
    private String owner;
    private String description;

    private List<NavigatorNode> children;

    public NavigatorNode() {
        this.children = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getHideFull() {
        return hideFull;
    }

    public void setHideFull(boolean hideFull) {
        this.hideFull = hideFull;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUsercount() {
        return usercount;
    }

    public void setUsercount(int usercount) {
        this.usercount = usercount;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getUnitStrId() {
        return unitStrId;
    }

    public void setUnitStrId(String unitStrId) {
        this.unitStrId = unitStrId;
    }

    public boolean isPublicRoom() {
        return this.unitStrId != null;
    }

    public boolean isRoom() {
        return this.unitStrId != null || this.owner != null;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getCasts() {
        return casts;
    }

    public void setCasts(String casts) {
        this.casts = casts;
    }

    public int getUsersInQueue() {
        return usersInQueue;
    }

    public void setUsersInQueue(int usersInQueue) {
        this.usersInQueue = usersInQueue;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getFlatId() {
        return flatId;
    }

    public void setFlatId(int flatId) {
        this.flatId = flatId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<NavigatorNode> getChildren() {
        return children;
    }
}
