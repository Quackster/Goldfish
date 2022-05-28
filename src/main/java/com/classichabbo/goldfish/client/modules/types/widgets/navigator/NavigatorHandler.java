package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.VariablesManager;
import com.classichabbo.goldfish.client.modules.Component;
import com.classichabbo.goldfish.client.modules.types.GoldfishHandler;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.google.gson.Gson;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NavigatorHandler extends MessageHandler {
    // private static final Gson gson = new Gson();

    public NavigatorHandler(NavigatorView navigatorView) {
        super(navigatorView);
    }

    private static void navnodeinfo(Connection connection, Request request) {
        var tNodeMask = request.readInt();
        var tNodeInfo = parseNode(request);

        if (tNodeInfo == null) {
            return;
        }

        tNodeInfo.setNodeMask(tNodeMask);
        var tCategoryId = tNodeInfo.getId();

        var parentCategory = Movie.getInstance().getViewByClass(NavigatorView.class).getCurrentCategory();

        if ((parentCategory != null && parentCategory.categoryId == tNodeInfo.getId()) || tNodeInfo.getParentid() == 0) {
            parentCategory = null;
        }

        var category = new Category(tNodeInfo.getId(), tNodeInfo.getName(), parentCategory, tNodeInfo.getUsercount(), tNodeInfo.getMaxUsers());

        while (request.remainingBytes().length > 0) {
            var tNode = parseNode(request);

            if (tNode == null)
                continue;

            var tNodeId = tNode.getParentid();
            var tParentId = tNode.getParentid();

            if (tParentId == tCategoryId) {
                tNodeInfo.getChildren().forEach(x -> x.setNodeId(tNodeId));
            }

            if (tNode.getNodeType() == 0) {
                category.categories.add(new Category(tNode.getId(), tNode.getName(), category, tNode.getUsercount(), tNode.getMaxUsers() == 0 ? 1 : tNode.getMaxUsers()));
            }
            if (tNode.getNodeType() == 1) {
                category.rooms.add(new NavigatorRoom(tNode.getId(), tNode.getName(), tNode.getCasts(), "", tNode.getUsercount(), tNode.getMaxUsers()));
            }
        }

        for (var child : tNodeInfo.getChildren()) {
            category.rooms.add(new NavigatorRoom(child.getFlatId(), child.getName(), child.getOwner(), child.getDescription(), child.getUsercount(), child.getMaxUsers(), getDoorbellFromString(child.getDoor())));
        }
        
        Platform.runLater(() -> Movie.getInstance().getViewByClass(NavigatorView.class).updateRoomList(category));
    }

    private static void getOwnRooms(Connection connection, Request request) {
        var list = parseFlatCategoryNode(request);
        var navRooms = new ArrayList<NavigatorRoom>();

        for (var item : list) {
            navRooms.add(new NavigatorRoom(item.getFlatId(), item.getName(), item.getOwner(), item.getDescription(), item.getUsercount(), item.getMaxUsers(), getDoorbellFromString(item.getDoor())));
        }

        Platform.runLater(() -> Movie.getInstance().getViewByClass(NavigatorView.class).updateOwnRooms(navRooms));
    }

    private static void getFavouriteRooms(Connection connection, Request request) {
        var navRooms = new ArrayList<NavigatorRoom>();

        // TODO fix public rooms in favourites
        // and figure out what all these unused values are for
        var mask = request.readInt();
        var nodeid = request.readInt();
        var nodeType = request.readInt();
        var id = request.readInt();
        var name = request.readClientString();
        var usercount = request.readInt();
        var maxUsers = request.readInt();
        var parentId = request.readInt();

        while (request.remainingBytes().length > 0) {
            var tFlatInfo = parseNavigatorFlatNode(request);

            navRooms.add(new NavigatorRoom(tFlatInfo.getFlatId(), tFlatInfo.getName(), tFlatInfo.getOwner(), tFlatInfo.getDescription(), tFlatInfo.getUsercount(), tFlatInfo.getMaxUsers(), getDoorbellFromString(tFlatInfo.getDoor())));
        }

        Platform.runLater(() -> Movie.getInstance().getViewByClass(NavigatorView.class).updateFavouriteRooms(navRooms));
    }

    private static Doorbell getDoorbellFromString(String str) {
        if (str.equals("password")) {
            return Doorbell.PASSWORD;
        }
        if (str.equals("closed")) {
            return Doorbell.RING;
        }
        return Doorbell.OPEN;
    }

    private static NavigatorNode parseNode(Request request) {
        var tNodeId = request.readInt();

        if (tNodeId <= 0)
            return null;

        var node = new NavigatorNode();

        var tNodeType = request.readInt();
        var name = request.readClientString();
        var usercount = request.readInt();
        var maxUsers = request.readInt();
        var parentid = request.readInt();

        node.setId(tNodeId);
        node.setNodeType(tNodeType);
        node.setName(name);
        node.setUsercount(usercount);
        node.setMaxUsers(maxUsers);
        node.setParentid(parentid);

        if (tNodeType == 1) {
            node.setUnitStrId(request.readClientString());
            node.setPort(request.readInt());
            node.setDoor(request.readInt());
            node.setCasts(request.readClientString());
            node.setUsersInQueue(request.readInt());
            node.setVisible(request.readBool());
        }

        if (tNodeType == 2) {
            node.setNodeType(0);
            node.getChildren().addAll(parseFlatCategoryNode(request));
        }

        return node;
    }

    private static List<NavigatorFlatNode> parseFlatCategoryNode(Request request) {
        var tFlatList = new ArrayList<NavigatorFlatNode>();
        var tFlatCount = request.readInt();
        
        for (int i =0; i < tFlatCount; i++) {
            tFlatList.add(parseNavigatorFlatNode(request));
        }

        return tFlatList;
    }

    private static NavigatorFlatNode parseNavigatorFlatNode(Request request) {
        var tFlatInfo = new NavigatorFlatNode();
        tFlatInfo.setFlatId(request.readInt());
        tFlatInfo.setName(request.readClientString());
        tFlatInfo.setOwner(request.readClientString());
        tFlatInfo.setDoor(request.readClientString());
        tFlatInfo.setUsercount(request.readInt());
        tFlatInfo.setMaxUsers(request.readInt());
        tFlatInfo.setDescription(request.readClientString());
        return tFlatInfo;
    }

    public void sendNavigate(int categoryId) {
        var conn = Connection.get();

        if (conn == null)
            return;

        conn.send("NAVIGATE", this.isHideFull(), categoryId, 1);
    }

    public void sendGetFvrf() {
        var conn = Connection.get();

        if (conn == null)
            return;

        conn.send("GETFVRF");
    }

    public void sendSUserF() {
        var conn = Connection.get();

        if (conn == null)
            return;

        conn.send("SUSERF");
    }

    private boolean isHideFull() {
        return ((NavigatorView)this.getView()).isHideFullRooms();
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(16, NavigatorHandler::getOwnRooms);
        listeners.put(61, NavigatorHandler::getFavouriteRooms);
        listeners.put(220, NavigatorHandler::navnodeinfo);

        var commands = new HashMap<String, Integer>();
        commands.put("SUSERF", 16);
        commands.put("GETFVRF", 18);
        commands.put("NAVIGATE", 150);

        if (tBool) {
            Connection.get().registerListeners(this, listeners);
            Connection.get().registerCommands(this, commands);
        } else {
            Connection.get().unregisterListeners(this, listeners);
            Connection.get().unregisterCommands(this, commands);
        }
    }
}
