package org.alexdev.krishna.controls;

import org.alexdev.krishna.HabboClient;

import javafx.scene.paint.Color;

public class Label extends javafx.scene.control.Label {
    public Label(String text) {
        setTextFill(Color.web("#000000"));
        setFont(HabboClient.volter);
        setText(text);
    }

    public Label(String text, boolean isBold) {
        setTextFill(Color.web("#000000"));
        if (isBold)
            setFont(HabboClient.volterBold);
        else
            setFont(HabboClient.volter);
        setText(text);
    }

    public Label(String text, String color) {
        setTextFill(Color.web(color));
        setFont(HabboClient.volter);
        setText(text);
    }

    public Label(String text, boolean isBold, String color) {
        setTextFill(Color.web(color));
        if (isBold)
            setFont(HabboClient.volterBold);
        else
            setFont(HabboClient.volter);
        setText(text);
    }
}
