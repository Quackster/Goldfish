package com.classichabbo.goldfish.client.views.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TextFieldRound extends HBox {
    private TextField text;

    public TextFieldRound(String text) {
        var left = new Pane();
        left.setPrefSize(5, 17);
        left.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field/left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.text = new TextField(text, false, false);
        this.text.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field/center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.text.setPrefHeight(17);

        var right = new Pane();
        right.setPrefSize(5, 18);
        right.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field/right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.getChildren().addAll(left, this.text, right);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setWidth(int width) {
        this.text.setSize(width - 10, 17, 2, 4, 3);
    }

    public String getText() {
        return this.text.getText();
    }

    public void update() {
        this.text.update();
    }
}
