package com.classichabbo.goldfish.client.components;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.client.game.values.types.VariablesManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.views.GlobalView;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.loader.LoadingScreen;
import com.classichabbo.goldfish.networking.Client;
import javafx.application.Platform;

import java.awt.*;

public class LoaderComponent extends Component {
    private long connectionTimer = 0;

    private Thread clientConfigTask;
    private Thread externalTextsTask;
    private Thread externalVariablesTask;
    private Thread connectServerTask;

    public LoaderComponent() {
        this.clientConfigTask = null;
        this.externalTextsTask = null;
        this.externalVariablesTask = null;
    }

    /**
     * Load client config task
     */
    public boolean loadClientConfig() {
        try {
            PropertiesManager.getInstance().loadConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return PropertiesManager.getInstance().isFinished();
    }

    /**
     * Load external variables task
     */
    public boolean loadExternalVariables() {
        try {
            VariablesManager.getInstance().loadVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return VariablesManager.getInstance().isFinished();
    }

    /**
     * Load external texts task
     */
    public boolean loadExternalTexts() {
        try {
            TextsManager.getInstance().loadTexts();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return TextsManager.getInstance().isFinished();
    }

    /**
     * Connect server task
     */
    public void connectServer() {
        if (Client.getInstance().getConnectionAttempts().get() >= VariablesManager.getInstance().getInt("connection.max.attempts", 5)) {
            return;
        }

        if (!(System.currentTimeMillis() > this.connectionTimer)) {
            return;
        }

        Client.getInstance().getConnectionAttempts().incrementAndGet();
        this.connectionTimer = System.currentTimeMillis() + 5000; // Retry once every second for a max of 5 times

        var channelFuture = Client.getInstance().createSocket();

        channelFuture.addListener(listener -> {
            Client.getInstance().setConnecting(false);

            if (listener.isSuccess()) {
                System.out.println("Successful connection...");
                Client.getInstance().setConnected(true);
            } else {
                System.out.println("Connection failure...");
                Client.getInstance().setConnected(false);
            }
        });
    }

    public void showHotel() {
        Platform.runLater(() -> {
            var global = Movie.getInstance().getInterfaceByClass(GlobalView.class);
            var loader = Movie.getInstance().getInterfaceByClass(LoadingScreen.class);

            if (loader != null) {
                loader.getLoadingLogoImage().setVisible(false);
                loader.getLoaderBar().toFront();

                var entryView = new EntryView();

                entryView.setRunAfterOpening(() -> {
                    loader.progressLoader(15);
                    global.getHandler().beginLoginSequence();
                });

                Movie.getInstance().createObject(entryView);
            }
        });
    }



    public Thread getClientConfigTask() {
        return clientConfigTask;
    }

    public void setClientConfigTask(Thread clientConfigTask) {
        this.clientConfigTask = clientConfigTask;
    }

    public Thread getExternalVariablesTask() {
        return externalVariablesTask;
    }

    public void setExternalVariablesTask(Thread externalVariablesTask) {
        this.externalVariablesTask = externalVariablesTask;
    }

    public Thread getExternalTextsTask() {
        return externalTextsTask;
    }

    public void setExternalTextsTask(Thread externalTextsTask) {
        this.externalTextsTask = externalTextsTask;
    }

    public Thread getConnectServerTask() {
        return connectServerTask;
    }

    public void setConnectServerTask(Thread connectServerTask) {
        this.connectServerTask = connectServerTask;
    }
}
