package com.classichabbo.goldfish.client.views.controls;

import com.classichabbo.goldfish.client.Movie;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TextFieldSquare extends TextFieldContainer {
    private Pane background;

    public TextFieldSquare(String text) {
        super();
        this.background = new Pane();
        this.background.setPrefHeight(26);
        this.background.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        
        this.text = new TextField(text, true, Color.BLACK, true, 1, true, this);
        this.text.setLayoutX(7);
        this.text.setLayoutY(9);

        this.getChildren().addAll(this.background, this.text);
        this.setOnMouseClicked(e -> Movie.getInstance().setCurrentTextField(this.text));
        this.initCaret(7, 8, 11);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setWidth(int width) {
        this.background.setPrefWidth(width);
        this.text.setWidth(width - 12);
    }

    public String getText() {
        return this.text.getText();
    }
}
