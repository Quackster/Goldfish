package com.classichabbo.goldfish.client.modules.controls;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TextFieldRound extends TextFieldContainer {
    private Pane center;

    public TextFieldRound(String text) {
        super();
        var background = new HBox();

        var left = new Pane();
        left.setPrefSize(5, 17);
        left.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field_round/left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.center = new Pane();
        this.center.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field_round/center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.center.setPrefHeight(17);

        var right = new Pane();
        right.setPrefSize(5, 17);
        right.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field_round/right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.text = new TextField(text, false, Color.BLACK, false, 1, false, this);
        this.text.setLayoutX(7);
        this.text.setLayoutY(4);

        background.getChildren().addAll(left, this.center, right);
        this.getChildren().addAll(background, this.text);
        this.setOnMouseClicked(e -> Movie.getInstance().setCurrentTextField(this.text));
        this.initCaret(7, 4, 10);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setWidth(int width) {
        this.center.setPrefWidth(width - 10);
        this.text.setWidth(width - 10);
    }

    public String getText() {
        return this.text.getText();
    }
}
