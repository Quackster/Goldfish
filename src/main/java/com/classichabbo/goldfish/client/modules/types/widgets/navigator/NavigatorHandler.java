package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.game.values.types.VariablesManager;
import com.classichabbo.goldfish.client.modules.Component;
import com.classichabbo.goldfish.client.modules.types.GoldfishHandler;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;

import java.util.HashMap;

public class NavigatorHandler extends MessageHandler {
    public NavigatorHandler(NavigatorView navigatorView) {
        super(navigatorView);
    }

    private static void navnodeinfo(Connection connection, Request request) {
        boolean hideFull = request.readBool();
        int categoryId = request.readInt();
        boolean isPublicSpaces = request.readInt() == 0;
        String categoryName = request.readClientString();
        int categoryCurrentVisitors = request.readInt();
        int categoryMaxVisitors = request.readInt();
        int categoryParentId = request.readInt();

        /*
        if (!isPublicSpaces) {
            System.out.println("room size: " + request.readInt());
            return;
        } else {
            while (request.remainingBytes().length > 0) {
                System.out.println(new String(request.readBytes(request.remainingBytes().length)));
            }
        }*/
    }

    public void sendNavigate(int categoryId) {
        var conn = Connection.get();

        if (conn == null)
            return;

        conn.send("NAVIGATE", this.isHideFull(), categoryId, 1);
    }

    private boolean isHideFull() {
        return ((NavigatorView)this.getView()).isHideFullRooms();
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
