package org.alexdev.krishna.interfaces;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Alert extends Dialog {
    private String text;

    private boolean isInitialised;

    public Alert(String text) {
        this.text = text;
    }

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        super.init();

        var e = new Pane();
        //e.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
        e.setMinSize(250, 250);
        
        var e1 = new Pane();
        e1.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        e1.setMinSize(50, 50);

        e.getChildren().add(e1);
        
        setPadding(9, 10, 11, 10);
        setTitle("Notice");
        setContent(e);
        initInnerBackground();

        this.isInitialised = true;
    }

    /**
     * Update tick to resize the images and boxes necessary for responsiveness
     */
    public void update() {
        if (!this.isInitialised)
            return;

        // do
    }

    @Override
    public boolean isReady() {
        return isInitialised;
    }
}
