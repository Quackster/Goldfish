package com.classichabbo.goldfish.client.handlers;

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

public class ClubHandler extends MessageHandler {
    private static void handle_scr_sinfo(Connection conn, Request request) {

    }

    private static void handle_ok(Connection conn, Request request) {
        conn.send("SCR_GET_USER_INFO", "club_habbo");
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(3, ClubHandler::handle_ok);
        listeners.put(7, ClubHandler::handle_scr_sinfo);

        var commands = new HashMap<String, Integer>();
        commands.put("SCR_GET_USER_INFO", 26);

        if (tBool) {
            Connection.get().registerListeners(this, listeners);
            Connection.get().registerCommands(this, commands);
        } else {
            Connection.get().unregisterListeners(this, listeners);
            Connection.get().unregisterCommands(this, commands);
        }
    }
}
