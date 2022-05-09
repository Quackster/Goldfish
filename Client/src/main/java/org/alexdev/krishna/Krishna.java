package org.alexdev.krishna;

import javafx.application.Application;

public class Krishna {
    private static HabboClient client;

    /**
     * Main call of Java application
     *
     * @param args System arguments
     */
    public static void main(String[] args) {
        Application.launch(HabboClient.class);
    }
}