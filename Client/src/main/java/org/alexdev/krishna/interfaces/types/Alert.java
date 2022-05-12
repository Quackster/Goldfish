package org.alexdev.krishna.interfaces.types;

import org.alexdev.krishna.controls.ButtonLarge;
import org.alexdev.krishna.controls.Label;
import org.alexdev.krishna.interfaces.InterfaceType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

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

        var content = new VBox();
        content.setMaxWidth(500);
        
        var text = new Label(this.text);
        text.setWrapText(true);
        text.setLineSpacing(3);
        text.setPadding(new Insets(29, 24, 34, 12));

        var ok = new ButtonLarge("OK");
        ok.setAlignment(Pos.CENTER);
        ok.setPadding(new Insets(0, 0, 13, -5));
        
        content.getChildren().addAll(text, ok);
        
        setPadding(9, 10, 11, 10);
        setTitle("Notice!");
        setContent(content);
        initInnerBackground();

        this.isInitialised = true;
    }

    @Override
    public void sceneChanged() {
    }

    /**
     * Update tick to resize the images and boxes necessary for responsiveness
     */
    @Override
    public void update() {
        if (!this.isInitialised)
            return;

        // Drag stuff
        super.update();
    }

    @Override
    public boolean isReady() {
        return isInitialised;
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.ALERT;
    }
}
