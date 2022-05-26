package com.classichabbo.goldfish.client.views.controls;

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

public class TextAreaRound extends TextFieldContainer {
    private Pane center;

    public TextAreaRound(String text) {
        super();
        var background = new HBox();
        
        var left = new Pane();
        left.setPrefSize(5, 44);
        left.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_area_round/left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.center = new Pane();
        this.center.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_area_round/center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.center.setPrefHeight(44);

        var right = new Pane();
        right.setPrefSize(5, 44);
        right.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_area_round/right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.text = new TextField(text, false, Color.BLACK, false, 3, false, this);
        this.text.setLayoutX(6);
        this.text.setLayoutY(6);

        background.getChildren().addAll(left, this.center, right);
        this.getChildren().addAll(background, this.text);
        this.setOnMouseClicked(e -> Movie.getInstance().setCurrentTextField(this.text));
        this.initCaret(6, 6, 11);
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
