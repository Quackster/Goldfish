package org.alexdev.krishna.controls;

import org.alexdev.krishna.HabboClient;

import javafx.scene.paint.Color;
import org.alexdev.krishna.game.resources.ResourceManager;

public class Label extends javafx.scene.control.Label {
    public Label(String text) {
        setTextFill(Color.web("#000000"));
        setFont(ResourceManager.getInstance().getVolter());
        setText(text);
    }

    public Label(String text, boolean isBold) {
        setTextFill(Color.web("#000000"));
        if (isBold)
            setFont(ResourceManager.getInstance().getVolterBold());
        else
            setFont(ResourceManager.getInstance().getVolter());
        setText(text);
    }

    public Label(String text, String color) {
        setTextFill(Color.web(color));
        setFont(ResourceManager.getInstance().getVolter());
        setText(text);
    }

    public Label(String text, boolean isBold, String color) {
        setTextFill(Color.web(color));
        if (isBold)
            setFont(ResourceManager.getInstance().getVolterBold());
        else
            setFont(ResourceManager.getInstance().getVolter());
        setText(text);
    }
}
