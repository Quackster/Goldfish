package com.classichabbo.goldfish.client.handlers;

import com.classichabbo.goldfish.client.Goldfish;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.entities.user.UserObject;
import com.classichabbo.goldfish.client.views.types.alerts.Alert;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.loader.LoadingView;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;

import java.util.HashMap;

public class EntryHandler extends MessageHandler {
    private final EntryView entryView;

    public EntryHandler(EntryView entryView) {
        this.entryView = entryView;
    }

    private static void handleSystemBroadcast(Connection conn, Request request) {
        Movie.getInstance().createObject(new Alert(request.readClientString()));
    }

    private static void handleUserObj(Connection conn, Request request) {
        var userObj = new UserObject(
                Integer.parseInt(request.readClientString()),
                request.readClientString(),
                request.readClientString(),
                request.readClientString(),
                request.readClientString()
        );

        conn.attr(UserObject.ATTRIBUTE_KEY).set(userObj);

        var loader = Movie.getInstance().getViewByClass(LoadingView.class);

        if (loader != null) {
            loader.progressLoader(20);
        }
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(5, EntryHandler::handleUserObj);
        listeners.put(139, EntryHandler::handleSystemBroadcast);

        var commands = new HashMap<String, Integer>();
        commands.put("GET_INFO", 7);

        if (tBool) {
            Connection.get().registerListeners(this, listeners);
            Connection.get().registerCommands(this, commands);
        } else {
            Connection.get().unregisterListeners(this, listeners);
            Connection.get().unregisterCommands(this, commands);
        }
    }
}
