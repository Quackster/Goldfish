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

public class ButtonLarge extends HBox {
    private Label center;

    public ButtonLarge(String text) {
        var leftBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button_large/left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var centerBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button_large/center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var rightBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button_large/right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var leftPressedBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button_large/left_pressed.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var centerPressedBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button_large/center_pressed.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        var rightPressedBackground = new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/button_large/right_pressed.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

        var left = new Pane();
        left.setPrefSize(5, 23);
        left.setBackground(leftBackground);

        this.center = new Label(text, true);
        this.center.setBackground(centerBackground);
        this.center.setPadding(new Insets(6, 4, 6, 6));

        var right = new Pane();
        right.setPrefSize(6, 23);
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
}
