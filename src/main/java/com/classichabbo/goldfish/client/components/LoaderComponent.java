package com.classichabbo.goldfish.client.components;

import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.networking.NettyClient;

import java.awt.*;
import java.util.concurrent.Future;

public class LoaderComponent extends Component {
    private long connectionTimer = 0;

    private Future<Boolean> clientConfigTask;
    private Future<Boolean> externalTextsTask;
    private Future<Boolean> externalVariablesTask;
    private Future<Boolean> connectServerTask;

    public LoaderComponent() {
        this.clientConfigTask = null;
        this.externalTextsTask = null;
        this.externalVariablesTask = null;
    }

    public boolean loadClientConfig() {
        try {
            PropertiesManager.getInstance().loadConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return PropertiesManager.getInstance().isFinished();
    }

    public boolean loadExternalVariables() {
        try {
            PropertiesManager.getInstance().loadVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return PropertiesManager.getInstance().isFinished();
    }

    public boolean loadExternalTexts() {
        try {
            TextsManager.getInstance().loadTexts();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return TextsManager.getInstance().isFinished();
    }


    public void connectServer() {
        if (NettyClient.getInstance().getConnectionAttempts().get() >= PropertiesManager.getInstance().getInt("connection.max.attempts", 5)) {
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

    public Future<Boolean> getClientConfigTask() {
        return clientConfigTask;
    }

    public void setClientConfigTask(Future<Boolean> clientConfigTask) {
        this.clientConfigTask = clientConfigTask;
    }

    public Future<Boolean> getExternalVariablesTask() {
        return externalVariablesTask;
    }

    public void setExternalVariablesTask(Future<Boolean> externalVariablesTask) {
        this.externalVariablesTask = externalVariablesTask;
    }

    public Future<Boolean> getExternalTextsTask() {
        return externalTextsTask;
    }

    public void setExternalTextsTask(Future<Boolean> externalTextsTask) {
        this.externalTextsTask = externalTextsTask;
    }

    public Future<Boolean> getConnectServerTask() {
        return connectServerTask;
    }

    public void setConnectServerTask(Future<Boolean> connectServerTask) {
        this.connectServerTask = connectServerTask;
    }
}
