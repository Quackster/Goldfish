package com.classichabbo.goldfish.networking.netty;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.views.types.error.ErrorWindow;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.ChannelConnection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyConnectionHandler extends SimpleChannelInboundHandler<Request> {
    private Connection connection;
    private ChannelConnection channel;

    public NettyConnectionHandler(Connection server) {
        this.connection = server;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.channel = new ChannelConnection(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (!Connection.getInstance().isConnected()) {
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
        this.channel = new ChannelConnection(ctx.channel());
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

    public ChannelConnection getConnection() {
        return channel;
    }
}