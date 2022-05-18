package com.classichabbo.goldfish.networking.connections;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.views.types.error.ErrorWindow;
import com.classichabbo.goldfish.networking.NettyClient;
import com.classichabbo.goldfish.networking.wrappers.ClientChannel;
import com.classichabbo.goldfish.networking.wrappers.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectionHandler extends SimpleChannelInboundHandler<Request> {
    private NettyClient client;
    private ClientChannel channel;

    public ConnectionHandler(NettyClient server) {
        this.client = server;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (!NettyClient.getInstance().isConnected()) {
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
        this.channel = new ClientChannel(ctx.channel());
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
}