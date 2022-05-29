package com.classichabbo.goldfish.client.modules.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Button extends HBox {
    private Label center;

    public Button(String text) {
        var leftBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button", "left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var centerBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button", "center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var rightBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button", "right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var leftPressedBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button", "left_pressed.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var centerPressedBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button", "center_pressed.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var rightPressedBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button", "right_pressed.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

        var left = new Pane();
        left.setPrefSize(4, 18);
        left.setBackground(leftBackground);

        this.center = new Label(text, true);
        this.center.setBackground(centerBackground);
        this.center.setPadding(new Insets(3, 0, 5, 2));

        var right = new Pane();
        right.setPrefSize(5, 18);
        right.setBackground(rightBackground);

        this.getChildren().addAll(left, this.center, right);
        this.setCursor(Cursor.HAND);
        this.setPickOnBounds(false);

        this.setOnMousePressed(e -> {
            left.setBackground(leftPressedBackground);
            this.center.setBackground(centerPressedBackground);
            right.setBackground(rightPressedBackground);
        });

        this.setOnMouseReleased(e -> {
            left.setBackground(leftBackground);
            this.center.setBackground(centerBackground);
            right.setBackground(rightBackground);
        });
    }

    public void setText(String text) {
        this.center.setText(text);
    }

    public void setOnWidth(Runnable runnable) {
        this.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            runnable.run();
        });
    }
}
