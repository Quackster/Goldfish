package com.classichabbo.goldfish.client.handlers;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.views.types.error.ErrorWindow;
import com.classichabbo.goldfish.client.views.types.loader.LoadingView;
import com.classichabbo.goldfish.networking.Client;
import com.classichabbo.goldfish.networking.util.NetworkUtil;
import com.classichabbo.goldfish.networking.wrappers.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class GlobalHandler extends MessageHandler {
    private static void handleHello(Connection conn, Request request) {
        conn.send("INIT_CRYPTO", 0);

        var loader = Movie.getInstance().getViewByClass(LoadingView.class);

        if (loader != null) {
            loader.progressLoader(5);
            loader.getComponent().showHotel();
        }
    }

    private static void handleCryptoParameters(Connection conn, Request request) {
        conn.send("GENERATE_KEY", "0");

        var loader = Movie.getInstance().getViewByClass(LoadingView.class);

        if (loader != null) {
            loader.progressLoader(10);
        }
    }

    private static void handleServerKey(Connection conn, Request request) {
        conn.send("VERSIONCHECK", "Goldfish1", PropertiesManager.getInstance().getString("external.variables"));
        conn.send("UNIQUEID", NetworkUtil.getUniqueIdentifier());
        conn.send("GET_SESSION_PARAMETERS");

        var loader = Movie.getInstance().getViewByClass(LoadingView.class);

        if (loader != null) {
            loader.progressLoader(20);
        }
    }

    public void beginLoginSequence() {
        var conn = Client.getConnection();

        if (conn == null) {
            Movie.getInstance().createObject(new ErrorWindow());
            return;
        }

        String ssoTicket = null;

        try {
            ssoTicket = Files.readString(Path.of("auth_ticket"));

            if (ssoTicket.length() > 255) {
                ssoTicket = ssoTicket.substring(0, 255);
            }
        } catch (Exception ex) {

        }

        if (ssoTicket == null) {
            conn.close();
            return;
        }

        conn.send("SSO_TICKET", ssoTicket);
    }

    private static void authenticationOK(Connection conn, Request request) {
        conn.send("GET_INFO");

        /*var loader = Movie.getInstance().getViewByClass(LoadingView.class);

        if (loader != null) {
            //loader.progressLoader(20);
        }*/
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(0, GlobalHandler::handleHello);
        listeners.put(1, GlobalHandler::handleServerKey);
        listeners.put(277, GlobalHandler::handleCryptoParameters);
        listeners.put(3, GlobalHandler::authenticationOK);

        var commands = new HashMap<String, Integer>();
        commands.put("INIT_CRYPTO", 206);
        commands.put("GENERATE_KEY", 2002);
        commands.put("VERSIONCHECK", 1170);
        commands.put("UNIQUEID", 813);
        commands.put("GET_SESSION_PARAMETERS", 1817);
        commands.put("SSO_TICKET", 415);

        if (tBool) {
            Movie.getInstance().registerListeners(this, listeners);
            Movie.getInstance().registerCommands(this, commands);
        } else {
            Movie.getInstance().unregisterListeners(this, listeners);
            Movie.getInstance().unregisterCommands(this, commands);
        }
    }
}
