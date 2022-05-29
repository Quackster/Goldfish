package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;

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

            if (tParentId == tCategoryId) {
                tNodeInfo.getChildren().forEach(x -> x.setParentid(tNodeId));
            }

            tNodeInfo.getChildren().add(tNode);
        }

        var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

        if (navigatorView != null)
            navigatorView.getComponent().processNavigatorData(tNodeInfo);
    }

    private static NavigatorNode parseNode(Request request) {
        var tNodeId = request.readInt();

        if (tNodeId <= 0)
            return null;

        //System.out.println(new String(request.readBytes(request.remainingBytes().length)));

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
            node.setDoor(String.valueOf(request.readInt()));
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
            tFlatList.add(tFlatInfo);
        }

        return tFlatList;
    }

    public void sendNavigate(int categoryId) {
        var conn = Connection.get();

        if (conn == null)
            return;

        conn.send("NAVIGATE", this.isHideFull(), categoryId, 1);
    }

    private boolean isHideFull() {
        return this.getView().isHideFullRooms();
    }

    @Override
    public NavigatorView getView() {
        return ((NavigatorView)super.getView());
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(220, NavigatorHandler::navnodeinfo);

        var commands = new HashMap<String, Integer>();
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
