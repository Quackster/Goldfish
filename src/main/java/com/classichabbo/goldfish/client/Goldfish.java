package com.classichabbo.goldfish.client;

import com.classichabbo.goldfish.networking.wrappers.messages.MessageHandler;
import com.classichabbo.goldfish.networking.wrappers.messages.MessageRequest;
import com.classichabbo.goldfish.networking.wrappers.messages.types.MessageCommand;
import com.classichabbo.goldfish.networking.wrappers.messages.types.MessageListener;
import javafx.application.Application;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Goldfish {
    public static final String VERSION = "0.0001a";
    private static Goldfish instance;

    /**
     * Main call of Java application
     *
     * @param args System arguments
     */
    public static void main(String[] args) {
        Application.launch(Movie.class);
    }
    /**
     * Goldfish singleton
     */
    public static Goldfish getInstance() {
        if (instance == null) {
            instance = new Goldfish();
        }

        return instance;
    }
}