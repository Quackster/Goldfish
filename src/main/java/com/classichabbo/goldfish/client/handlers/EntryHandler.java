package com.classichabbo.goldfish.client.handlers;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.entities.user.UserObject;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.loader.LoadingView;
import com.classichabbo.goldfish.networking.wrappers.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;

import java.util.HashMap;

public class EntryHandler extends MessageHandler {
    private final EntryView entryView;

    public EntryHandler(EntryView entryView) {
        this.entryView = entryView;
    }


    private static void handleUserObj(Connection connection, Request request) {
        var userObj = new UserObject(
                Integer.parseInt(request.readClientString()),
                request.readClientString(),
                request.readClientString(),
                request.readClientString(),
                request.readClientString()
        );

        connection.attr(UserObject.ATTRIBUTE_KEY).set(userObj);

        var loader = Movie.getInstance().getViewByClass(LoadingView.class);

        if (loader != null) {
            loader.progressLoader(20);
        }
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(5, EntryHandler::handleUserObj);

        var commands = new HashMap<String, Integer>();
        commands.put("GET_INFO", 7);

        if (tBool) {
            Movie.getInstance().registerListeners(this, listeners);
            Movie.getInstance().registerCommands(this, commands);
        } else {
            Movie.getInstance().unregisterListeners(this, listeners);
            Movie.getInstance().unregisterCommands(this, commands);
        }
    }
}
