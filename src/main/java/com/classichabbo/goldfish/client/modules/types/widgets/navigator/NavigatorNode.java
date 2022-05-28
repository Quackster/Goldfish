package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import java.util.ArrayList;
import java.util.List;

public class NavigatorNode {
    private int id;
    private int nodeMask;
    private int nodeType;
    private String name;
    private int usercount;
    private int maxUsers;
    private int parentid;
    private String unitStrId;
    private int port;
    private int door;
    private String casts;
    private int usersInQueue;
    private boolean isVisible;
    private List<NavigatorFlatNode> children;

    public NavigatorNode() {
        this.children = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNodeMask() {
        return nodeMask;
    }

    public void setNodeMask(int nodeMask) {
        this.nodeMask = nodeMask;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
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

    public List<NavigatorFlatNode> getChildren() {
        return children;
    }
}
