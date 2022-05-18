package com.classichabbo.goldfish.client.views.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class ButtonLarge extends HBox {
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

        var center = new Label(text, true);
        center.setBackground(centerBackground);
        center.setPadding(new Insets(6, 4, 6, 6));

        var right = new Pane();
        right.setPrefSize(6, 23);
        right.setBackground(rightBackground);

        this.getChildren().addAll(left, center, right);
        this.setCursor(Cursor.HAND);
        this.setPickOnBounds(false);

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                left.setBackground(leftPressedBackground);
                center.setBackground(centerPressedBackground);
                right.setBackground(rightPressedBackground);
            }
        });

        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                left.setBackground(leftBackground);
                center.setBackground(centerBackground);
                right.setBackground(rightBackground);
            }
        });
    }
}
