package com.classichabbo.goldfish.networking;

import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.networking.wrappers.Connection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private static Client instance;
    private AtomicInteger connectionAttempts;

    private DefaultChannelGroup channels;
    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;

    private boolean isConnected;
    private boolean isConnecting;
    private NettyChannelInitializer nettyChannelInitializer;

    public Client() {
        this.channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.connectionAttempts = new AtomicInteger(0);
    }

    /**
     * Create the Netty sockets.
     * @return
     */
    public ChannelFuture createSocket() {
        this.isConnected = false;
        this.isConnecting = true;

        try {
            this.bootstrap = new Bootstrap();
            this.workerGroup = new NioEventLoopGroup();
            this.nettyChannelInitializer = new NettyChannelInitializer(this);

            this.bootstrap
                    .group(this.workerGroup)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(this.nettyChannelInitializer);

            return this.bootstrap.connect(
                    PropertiesManager.getInstance().getString("connection.info.host"),
                    PropertiesManager.getInstance().getInt("connection.info.port")
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void dispose() {
        try {
            this.workerGroup.shutdownGracefully().sync();
        } catch (Exception ex) {

        }
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }

        return instance;
    }

    public static Connection getConnection() {
        return instance.nettyChannelInitializer.getConnectionHandler().getConnection();
    }


    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }

    public AtomicInteger getConnectionAttempts() {
        return connectionAttempts;
    }
}
