package com.classichabbo.goldfish.networking.netty;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.modules.types.error.ErrorWindow;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.networking.wrappers.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyConnectionHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Connection.get().setChannel(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (!NettyClientConnection.getInstance().isConnected()) {
            return;
        }

        Movie.getInstance().createObject(new ErrorWindow(
                TextsManager.getInstance().getString("Alert_ConnectionFailure"),
                TextsManager.getInstance().getString("Alert_ConnectionDisconnected"),
                false
        ));

        Connection.get().setChannel(null);
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request message) {
        // this.channel = new ChannelConnection(ctx.channel()); - wtf is this? my fault anyways for putting that here :^) - Avery
        if (!Connection.LISTENER_LOG_BLACKLIST.contains(message.getHeaderId()))
            System.out.println("[" + message.getHeaderId() + " / " + message.getHeader() + "] - " + message.getMessageBody());

        var conn = Connection.get();

       conn.getListeners().forEach(x -> {
            if (x.getHeader() == message.getHeaderId()) {
                x.getMessage().received(conn, message);
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