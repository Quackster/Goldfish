package com.classichabbo.goldfish.client;

import javafx.application.Application;

public class Goldfish {
    private static Movie client;

    /**
     * Main call of Java application
     *
     * @param args System arguments
     */
    public static void main(String[] args) {
        Application.launch(Movie.class);
    }
}