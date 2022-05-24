package com.classichabbo.goldfish.client.views.controls;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TextFieldSquare extends Pane {
    private Pane background;
    private TextField text;

    public TextFieldSquare(String text) {
        this.background = new Pane();
        this.background.setPrefHeight(26);
        this.background.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        
        this.text = new TextField(text, true, true);
        this.text.setPrefHeight(26);
        this.text.setOnWidth(() -> this.text.setLayoutX((this.background.getWidth() / 2) - (this.text.getTextWidth() / 2)));
        //this.text.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

        this.getChildren().addAll(this.background, this.text);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setWidth(int width) {
        this.background.setPrefWidth(width);
        this.text.setLayoutX(width / 2);
        this.text.setSize(width - 10, 26, 0, 0, 8);
    }

    public String getText() {
        return this.text.getText();
    }

    public void update() {
        this.text.update();
    }
}
