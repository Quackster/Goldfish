package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.Attributes;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
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

        tNodeInfo.setHideFull(tNodeMask == 1);
        var tCategoryId = tNodeInfo.getId();

        while (request.remainingBytes().length > 0) {
            var tNode = parseNode(request);

            if (tNode == null)
                continue;

            var tNodeId = tNodeInfo.getId();
            var tParentId = tNode.getParentid();

            tNodeInfo.getChildren().add(tNode);

            if (tParentId == tCategoryId) {
                tNodeInfo.getChildren().forEach(x -> x.setParentid(tNodeId));
            }
        }

        var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

        if (navigatorView != null)
            navigatorView.getComponent().processNavigatorData(tNodeInfo);
    }

    private static void noflatsforuser(Connection connection, Request request) {
        var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

        if (navigatorView != null)
            navigatorView.getComponent().processFlatData(List.of()); // force send no flats!
    }

    private static void noflats(Connection connection, Request request) {
        noflatsforuser(connection, request);
    }

    private static void search_flat_results(Connection connection, Request request) {
        userflatcats(connection, request);
    }

    private static void userflatcats(Connection connection, Request request) {
        var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

        if (navigatorView != null)
            navigatorView.getComponent().processFlatData(parseFlatCategoryNode(request)); // force send no flats!
    }

    private static void favouriteroomresults(Connection connection, Request request) {
        var tNodeMask = request.readInt();
        var tNodeInfo = parseNode(request);

        if (tNodeInfo == null) {
            return;
        }

        while (request.remainingBytes().length > 0) {
            var tNode = parseNode(request);

            if (tNode == null)
                continue;

            tNodeInfo.getChildren().add(tNode);
        }

        var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

        if (navigatorView != null)
            navigatorView.getComponent().processFavouriteRooms(tNodeInfo);
    }

    private static void recommended_room_list(Connection connection, Request request) {
        var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

        if (navigatorView != null)
            navigatorView.getComponent().processRecommendedRooms(parseFlatCategoryNode(request)); // force send no flats!
    }

    private static NavigatorNode parseNode(Request request) {
        var tNodeId = request.readInt();

        if (tNodeId < 0)
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

        switch (tNodeType) {
            case 1:
                node.setUnitStrId(request.readClientString());
                node.setPort(request.readInt());
                node.setDoor(String.valueOf(request.readInt()));
                node.setCasts(request.readClientString());
                node.setUsersInQueue(request.readInt());
                node.setVisible(request.readBool());
                break;
            case 2:
                node.setNodeType(0); // we is a category instead >:)
                node.getChildren().addAll(parseFlatCategoryNode(request));
                break;
        }

        return node;
    }

    private static List<NavigatorNode> parseFlatCategoryNode(Request request) {
        var tFlatList = new ArrayList<NavigatorNode>();
        var tFlatCount = request.readInt();

        for (int i = 0; i < tFlatCount; i++) {
            var tFlatInfo = new NavigatorNode();
            tFlatInfo.setFlatId(request.readInt());
            tFlatInfo.setName(request.readClientString());
            tFlatInfo.setOwner(request.readClientString());
            tFlatInfo.setDoor(request.readClientString());
            tFlatInfo.setUsercount(request.readInt());
            tFlatInfo.setMaxUsers(request.readInt());
            tFlatInfo.setDescription(request.readClientString());
            tFlatInfo.setNodeType(2);
            tFlatList.add(tFlatInfo);
        }

        return tFlatList;
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(220, NavigatorHandler::navnodeinfo);
        listeners.put(57, NavigatorHandler::noflatsforuser);
        listeners.put(58, NavigatorHandler::noflats);
        listeners.put(55, NavigatorHandler::search_flat_results);
        listeners.put(16, NavigatorHandler::userflatcats);
        listeners.put(61, NavigatorHandler::favouriteroomresults);
        listeners.put(351, NavigatorHandler::recommended_room_list);

        var commands = new HashMap<String, Integer>();
        commands.put("NAVIGATE", 150);
        commands.put("SUSERF", 16);
        commands.put("SRCHF", 17);
        commands.put("GETFVRF", 18);
        commands.put("GET_RECOMMENDED_ROOMS", 264);

        if (tBool) {
            Connection.get().registerListeners(this, listeners);
            Connection.get().registerCommands(this, commands);
        } else {
            Connection.get().unregisterListeners(this, listeners);
            Connection.get().unregisterCommands(this, commands);
        }
    }
}
