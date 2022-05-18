package com.classichabbo.goldfish.client.views.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TextField extends HBox {
    // TODO - build custom control as there are rendering issues with the caret using javafx.scene.control.TextField
    private javafx.scene.control.TextField center;

    public TextField(String text) {
        var left = new Pane();
        left.setPrefSize(5, 17);
        left.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field/left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.center = new javafx.scene.control.TextField();
        this.center.setStyle("-fx-text-inner-color: #000000;");
        this.setFont(false);
        this.center.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field/center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.center.setPrefHeight(17);

        var right = new Pane();
        right.setPrefSize(5, 18);
        right.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/text_field/right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.getChildren().addAll(left, this.center, right);
    }

    public void setFont(Boolean isBold) {
        this.center.setFont(isBold ? ResourceManager.getInstance().getVolterBold() : ResourceManager.getInstance().getVolter());
        this.center.setPadding(isBold ? new Insets(-2, 0, 0, 3) : new Insets(-1, 0, 0, 1));
    }

    public void setText(String text) {
        this.center.setText(text);
    }

    public void setWidth(int width) {
        this.center.setPrefWidth(width - 10);
    }

    public String getText() {
        return this.center.getText();
    }
}
