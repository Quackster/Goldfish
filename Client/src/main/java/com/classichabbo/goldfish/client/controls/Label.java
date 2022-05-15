package com.classichabbo.goldfish.client.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import javafx.scene.paint.Color;

public class Label extends javafx.scene.control.Label {
    public Label(String text) {
        this.setTextFill(Color.web("#000000"));
        this.setFont(ResourceManager.getInstance().getVolter());
        this.setText(text);
    }

    public Label(String text, boolean isBold) {
        this.setTextFill(Color.web("#000000"));
        this.setFont(isBold ? ResourceManager.getInstance().getVolterBold() : ResourceManager.getInstance().getVolter());
        this.setText(text);
    }

    public Label(String text, String color) {
        this.setTextFill(Color.web(color));
        this.setFont(ResourceManager.getInstance().getVolter());
        this.setText(text);
    }

    public Label(String text, boolean isBold, String color) {
        this.setTextFill(Color.web(color));
        this.setFont(isBold ? ResourceManager.getInstance().getVolterBold() : ResourceManager.getInstance().getVolter());
        this.setText(text);
    }
}
