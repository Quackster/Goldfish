package com.classichabbo.goldfish.client.handlers;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.Attributes;
import com.classichabbo.goldfish.client.game.club.HabboClubObject;
import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.client.views.types.club.ClubView;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;

import java.util.HashMap;

public class ClubHandler extends MessageHandler {
    public ClubHandler(ClubView clubView) {
        super(clubView);
    }

    private static void handle_scr_sinfo(Connection conn, Request request) {
        var tProdName = request.readClientString();
        var tDaysLeft = request.readInt();
        var tElapsedPeriods = request.readInt();
        var tPrepaidPeriods = request.readInt();
        var tResponseFlag = request.readInt();

        var habboClubObject = new HabboClubObject();
        habboClubObject.setProdName(tProdName);
        habboClubObject.setDaysLeft(tDaysLeft);
        habboClubObject.setElapsedPeriods(tElapsedPeriods);
        habboClubObject.setPrepaidPeriods(tPrepaidPeriods);
        habboClubObject.setResponseFlag(tResponseFlag);

        conn.attr(Attributes.HABBO_CLUB_OBJECT).set(habboClubObject);

        var clubView = Movie.getInstance().getViewByClass(ClubView.class);

        if (clubView != null)
            clubView.getComponent().setStatus(habboClubObject);
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
