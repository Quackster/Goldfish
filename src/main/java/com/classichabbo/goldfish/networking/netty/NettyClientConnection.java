package com.classichabbo.goldfish.networking.netty;

import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.networking.Connection;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.atomic.AtomicInteger;

public class NettyClientConnection {
    private static final int BUFFER_SIZE = 2048;
    private static NettyClientConnection instance;

    private Connection connection;
    private AtomicInteger connectionAttempts;
    private DefaultChannelGroup channels;
    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;
    private NettyChannelInitializer channelInitializer;

    private boolean isConnected;
    private boolean isConnecting;

    public NettyClientConnection() {
        this.channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.connectionAttempts = new AtomicInteger(0);
        this.connection = new Connection();
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
            this.channelInitializer = new NettyChannelInitializer(this);

            this.bootstrap
                    .group(this.workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_RCVBUF, BUFFER_SIZE)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(BUFFER_SIZE))
                    .option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
                    .handler(this.channelInitializer);

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

    public static NettyClientConnection getInstance() {
        if (instance == null) {
            instance = new NettyClientConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
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
