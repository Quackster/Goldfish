package com.classichabbo.goldfish.client.handlers;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.VariablesManager;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.loader.LoadingScreen;
import com.classichabbo.goldfish.client.views.types.toolbars.EntryToolbar;
import com.classichabbo.goldfish.client.views.types.widgets.Navigator;
import com.classichabbo.goldfish.networking.util.NetworkUtil;
import com.classichabbo.goldfish.networking.wrappers.ClientChannel;
import com.classichabbo.goldfish.networking.wrappers.Request;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;

import java.util.HashMap;

public class GlobalHandler extends MessageHandler {
    private static void handleHello(ClientChannel channel, Request request) {
        channel.send("INIT_CRYPTO", 0);

        var loader = Movie.getInstance().getInterfaceByClass(LoadingScreen.class);

        if (loader != null) {
            loader.progressLoader(5);
            loader.getComponent().showEntryView();
        }
    }

    private static void handleCryptoParameters(ClientChannel channel, Request request) {
        channel.send("GENERATE_KEY", "0");

        var loader = Movie.getInstance().getInterfaceByClass(LoadingScreen.class);

        if (loader != null) {
            loader.progressLoader(10);
        }
    }

    private static void handleServerKey(ClientChannel channel, Request request) {
        channel.send("VERSIONCHECK", "Goldfish1", VariablesManager.getInstance().getString("external.variables"));
        channel.send("UNIQUEID", NetworkUtil.getUniqueIdentifier());
        channel.send("GET_SESSION_PARAMETERS");

        var loader = Movie.getInstance().getInterfaceByClass(LoadingScreen.class);

        if (loader != null) {
            loader.progressLoader(20);
        }

        var entryView = Movie.getInstance().getInterfaceByClass(EntryView.class);

        if (entryView != null) {
            if (loader != null) {
                Movie.getInstance().removeObject(loader);
            }
        }
        //entryView.getComponent().initLoginProcess();

    }

    @Override
    public void regMsgList(boolean tBool) {
        var listeners = new HashMap<Integer, MessageRequest>();
        listeners.put(0, GlobalHandler::handleHello);
        listeners.put(1, GlobalHandler::handleServerKey);
        listeners.put(277, GlobalHandler::handleCryptoParameters);

        var commands = new HashMap<String, Integer>();
        commands.put("INIT_CRYPTO", 206);
        commands.put("GENERATE_KEY", 2002);
        commands.put("VERSIONCHECK", 1170);
        commands.put("UNIQUEID", 813);
        commands.put("GET_SESSION_PARAMETERS", 1817);

        if (tBool) {
            Movie.getInstance().registerListeners(this, listeners);
            Movie.getInstance().registerCommands(this, commands);
        } else {
            Movie.getInstance().unregisterListeners(this, listeners);
            Movie.getInstance().unregisterCommands(this, commands);
        }
    }
}
