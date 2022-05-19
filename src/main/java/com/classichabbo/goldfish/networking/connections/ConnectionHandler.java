package com.classichabbo.goldfish.networking.connections;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.views.types.error.ErrorWindow;
import com.classichabbo.goldfish.networking.Client;
import com.classichabbo.goldfish.networking.wrappers.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectionHandler extends SimpleChannelInboundHandler<Request> {
    private Client client;
    private Connection channel;

    public ConnectionHandler(Client server) {
        this.client = server;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.channel = new Connection(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (!Client.getInstance().isConnected()) {
            return;
        }

        Movie.getInstance().createObject(new ErrorWindow(
                TextsManager.getInstance().getString("Alert_ConnectionFailure"),
                TextsManager.getInstance().getString("Alert_ConnectionDisconnected"),
                false
        ));

        this.channel = null;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request message) {
        this.channel = new Connection(ctx.channel());
        System.out.println("[" + message.getHeaderId() + " / " + message.getHeader() + "] - " + message.getMessageBody());

        Movie.getInstance().getListeners().forEach(x -> {
            if (x.getHeader() == message.getHeaderId()) {
                x.getMessage().received(this.channel, message);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        // if (!(cause instanceof IOException)) {
        //    cause.printStackTrace();
        // }
    }

    public Connection getConnection() {
        return channel;
    }
}