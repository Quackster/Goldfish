package com.classichabbo.goldfish.client.components;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.client.game.values.types.VariablesManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.loader.LoadingScreen;
import com.classichabbo.goldfish.networking.NettyClient;
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
        if (NettyClient.getInstance().getConnectionAttempts().get() >= VariablesManager.getInstance().getInt("connection.max.attempts", 5)) {
            return;
        }

        if (!(System.currentTimeMillis() > this.connectionTimer)) {
            return;
        }

        NettyClient.getInstance().getConnectionAttempts().incrementAndGet();
        this.connectionTimer = System.currentTimeMillis() + 5000; // Retry once every second for a max of 5 times

        var channelFuture = NettyClient.getInstance().createSocket();

        channelFuture.addListener(listener -> {
            NettyClient.getInstance().setConnecting(false);

            if (listener.isSuccess()) {
                System.out.println("Successful connection...");
                NettyClient.getInstance().setConnected(true);
            } else {
                System.out.println("Connection failure...");
                NettyClient.getInstance().setConnected(false);
            }
        });
    }

    public void showEntryView() {
        Platform.runLater(() -> {
            var loader = Movie.getInstance().getInterfaceByClass(LoadingScreen.class);

            if (loader != null) {
                loader.getLoadingLogoImage().setVisible(false);
                Movie.getInstance().createObject(new EntryView());
                loader.getLoaderBar().toFront();
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
