package com.classichabbo.goldfish.networking;

import io.netty.channel.Channel;

public class NettyPlayerNetwork {
    private Channel channel;
    private int connectionId;
    private boolean isFlashConnected;
    private boolean isBetaConnected;

    private boolean saveMachineId;
    private String clientMachineId;

    public NettyPlayerNetwork(Channel channel, int connectionId) {
        this.channel = channel;
        this.connectionId = connectionId;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void send(Object response) {
        this.channel.writeAndFlush(response);
    }

    public void disconnect() {
        this.channel.close();
    }
}
