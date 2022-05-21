package com.classichabbo.goldfish.client.views.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import javafx.scene.paint.Color;

public class Label extends javafx.scene.control.Label {
    public Label(String text) {
        this(text, false, "#000000");
    }

    public Label(String text, boolean isBold) {
        this(text, isBold, "#000000");
    }

    public Label(String text, String color) {
        this(text, false, color);
    }

    public Label(String text, boolean isBold, String color) {
        text = convertLineSeparators(text); // TODO: Move this shit somewhere else idk

        this.setTextFill(Color.web(color));
        this.setFont(isBold ? ResourceManager.getInstance().getVolterBold() : ResourceManager.getInstance().getVolter());
        this.setText(text);
    }

    private String convertLineSeparators(String text) {
        var newText = text;
        newText = newText.replace("\\r", "\r");
        newText = newText.replace("\\n", "\n");
        return newText;
    }

    public void setOnWidth(Runnable runnable) {
        this.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            runnable.run();
        });
    }
}
