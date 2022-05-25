package com.classichabbo.goldfish.networking.netty;

import com.classichabbo.goldfish.networking.codec.NetworkDecoder;
import com.classichabbo.goldfish.networking.codec.NetworkEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final NettyClientConnection nettyServer;
    private NettyConnectionHandler connectionHandler;
    //private final long readLimit = 40*1024;
    //private final long writeLimit = 25*1024;

    public NettyChannelInitializer(NettyClientConnection nettyServer) {
        this.nettyServer = nettyServer;
        this.connectionHandler = new NettyConnectionHandler();
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("gameEncoder", new NetworkEncoder());
        pipeline.addLast("gameDecoder", new NetworkDecoder());
        pipeline.addLast("handler", this.connectionHandler);
    }

    public NettyConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
