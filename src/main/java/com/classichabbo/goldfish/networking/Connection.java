package com.classichabbo.goldfish.networking;

import com.classichabbo.goldfish.client.Goldfish;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.networking.netty.NettyClientConnection;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.classichabbo.goldfish.networking.wrappers.messages.types.MessageCommand;
import com.classichabbo.goldfish.networking.wrappers.messages.types.MessageListener;
import com.classichabbo.goldfish.networking.wrappers.Command;
import io.netty.channel.Channel;
import io.netty.util.DefaultAttributeMap;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Connection extends DefaultAttributeMap {
    private Channel channel;
    private List<MessageListener> listeners;
    private List<MessageCommand> commands;

    public static final List<Integer> LISTENER_LOG_BLACKLIST = List.of(50); // hide ping from logs
    public static final List<Integer> COMMAND_LOG_BLACKLIST = List.of(196); // hide ping from logs

    public Connection() {
        this.listeners = new CopyOnWriteArrayList<>();
        this.commands = new CopyOnWriteArrayList<>();
    }

    /**
     * Send command to server.
     */
    public void send(String tCmd, Object... data) {
        var messageCommand = this.commands.stream()
                .filter(x -> x.getHeaderName().equals(tCmd))
                .findFirst().orElse(null);//.map(x -> x.getHeader()).findFirst().orElse(null);

        if (messageCommand == null) {
            System.out.println("Command '" + tCmd + "' not found");
            return;
        }

        var header = messageCommand.getHeader();
        this.channel.writeAndFlush(new Command(header, data));
    }

    /**
     * Register listener handler for views.
     */
    public void registerListeners(MessageHandler messageHandler, HashMap<Integer, MessageRequest> listeners) {
        for (var x : listeners.entrySet()) {
            this.listeners.add(new MessageListener(messageHandler.getClass(), x.getKey(), x.getValue()));
        }
    }

    /**
     * Unregister listener handler for views.
     */
    public void unregisterListeners(MessageHandler messageHandler, HashMap<Integer, MessageRequest> listeners) {
        listeners.forEach((key, value) -> this.listeners.removeIf(message ->
                message.getHandlerClass() == messageHandler.getClass() && message.getHeader() == key.intValue()));
    }

    /**
     * Register command handler for views.
     */
    public void registerCommands(MessageHandler messageHandler, HashMap<String, Integer> listeners) {
        for (var x : listeners.entrySet()) {
            this.commands.add(new MessageCommand(messageHandler.getClass(), x.getValue(), x.getKey()));
        }
    }
    /**
     * Unegister command handler for views.
     */
    public void unregisterCommands(MessageHandler messageHandler, HashMap<String, Integer> commands) {
        commands.forEach((key, value) -> this.commands.removeIf(message ->
                message.getHandlerClass() == messageHandler.getClass() && message.getHeader() == value.intValue()));
    }

    /**
     * Get listeners registered by views.
     */
    public List<MessageListener> getListeners() {
        return listeners;
    }

    /**
     * Get commands registered by views.
     */
    public List<MessageCommand> getCommands() {
        return commands;
    }

    /**
     * Get the netty channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Set the netty channel
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Get connection
     */
    public static Connection get() {
        return NettyClientConnection.getInstance().getConnection();
    }

    /**
     * Close the connection
     */
    public void close() {
        this.channel.close();
    }
}
