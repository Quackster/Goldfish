package com.classichabbo.goldfish.client.components;

import javafx.application.Platform;

public abstract class Component {

    /**
     * Components affect the UI, so run these on the UI thread...
     *
     * Thread safety!!
     */
    protected void invoke(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
