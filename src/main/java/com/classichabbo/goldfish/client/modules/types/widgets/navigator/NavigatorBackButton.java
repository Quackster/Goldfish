package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.modules.controls.Label;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class NavigatorBackButton extends Pane {
    public NavigatorBackButton(String name, int startY, int index, EventHandler<MouseEvent> event) {
        this.setPrefSize(340, 22);
        this.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/views/navigator", "back_bottom.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.setLayoutX(1);
        this.setLayoutY(startY + (18 * (index + 1)));
        this.setPickOnBounds(false);
        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked(event);
        
        var backLabel = new Label(name, true, "#336666");
        backLabel.setLayoutX(39);
        backLabel.setLayoutY(3);
        this.getChildren().add(backLabel);
    }
}
