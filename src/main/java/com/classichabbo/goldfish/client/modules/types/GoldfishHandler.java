package com.classichabbo.goldfish.client.modules.types;

import com.classichabbo.goldfish.client.Goldfish;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.client.modules.types.error.ErrorWindow;
import com.classichabbo.goldfish.client.modules.types.loader.LoaderView;
import com.classichabbo.goldfish.util.NetworkUtil;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class GoldfishHandler extends MessageHandler {
    public GoldfishHandler(GoldfishView goldfishView) {
        super(goldfishView);
    }

    private static void handleHello(Connection conn, Request request) {
        conn.send("INIT_CRYPTO", 0);

        var loader = Movie.getInstance().getViewByClass(LoaderView.class);

        if (loader != null) {
            loader.progressLoader(5);
            loader.getComponent().showHotel();
        }
    }

    private static void handleCryptoParameters(Connection conn, Request request) {
        conn.send("GENERATE_KEY", "0");

        var loader = Movie.getInstance().getViewByClass(LoaderView.class);

        if (loader != null) {
            loader.progressLoader(10);
        }
    }

    private static void handleServerKey(Connection conn, Request request) {
        conn.send("VERSIONCHECK", "Goldfish1", PropertiesManager.getInstance().getString("external.variables"));
        conn.send("UNIQUEID", NetworkUtil.getUniqueIdentifier());
        conn.send("GET_SESSION_PARAMETERS");

        var loader = Movie.getInstance().getViewByClass(LoaderView.class);

        if (loader != null) {
            loader.progressLoader(20);
        }
    }

    public void beginLoginSequence() {
        var conn = Connection.get();

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

        // Ask server to authenticate us
        conn.send("SSO_TICKET", ssoTicket, Goldfish.VERSION);

        // Prepare login modules
        ((GoldfishView)this.getView()).getComponent().loadModules();
    }

    private static void authenticationOK(Connection conn, Request request) {
        conn.send("GET_INFO");
    }

    private static void ping(Connection conn, Request request) {
        conn.send("PONG");
    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(0, GoldfishHandler::handleHello);
        listeners.put(1, GoldfishHandler::handleServerKey);
        listeners.put(277, GoldfishHandler::handleCryptoParameters);
        listeners.put(3, GoldfishHandler::authenticationOK);
        listeners.put(50, GoldfishHandler::ping);

        var commands = new HashMap<String, Integer>();
        commands.put("INIT_CRYPTO", 206);
        commands.put("GENERATE_KEY", 2002);
        commands.put("VERSIONCHECK", 1170);
        commands.put("UNIQUEID", 813);
        commands.put("GET_SESSION_PARAMETERS", 1817);
        commands.put("SSO_TICKET", 415);
        commands.put("PONG", 196);

        if (tBool) {
            Connection.get().registerListeners(this, listeners);
            Connection.get().registerCommands(this, commands);
        } else {
            Connection.get().unregisterListeners(this, listeners);
            Connection.get().unregisterCommands(this, commands);
        }
    }
}
