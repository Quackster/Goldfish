package com.classichabbo.goldfish.networking.connections;

import com.classichabbo.goldfish.networking.NettyClient;
import com.classichabbo.goldfish.networking.streams.NettyRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

public class ConnectionHandler extends SimpleChannelInboundHandler<NettyRequest> {
    final private NettyClient server;

    public ConnectionHandler(NettyClient server) {
        this.server = server;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, NettyRequest message) {
        System.out.println(message.getHeaderId() + " // " + message.getMessageBody());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!(cause instanceof IOException)) {
            cause.printStackTrace();
        }
    }
}