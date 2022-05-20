package com.classichabbo.goldfish.networking;

import com.classichabbo.goldfish.networking.codec.NetworkDecoder;
import com.classichabbo.goldfish.networking.codec.NetworkEncoder;
import com.classichabbo.goldfish.networking.connections.ConnectionHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final Client nettyServer;
    private ConnectionHandler connectionHandler;
    //private final long readLimit = 40*1024;
    //private final long writeLimit = 25*1024;

    public NettyChannelInitializer(Client nettyServer) {
        this.nettyServer = nettyServer;
        this.connectionHandler = new ConnectionHandler(this.nettyServer);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("gameEncoder", new NetworkEncoder());
        pipeline.addLast("gameDecoder", new NetworkDecoder());
        pipeline.addLast("handler", this.connectionHandler);
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
