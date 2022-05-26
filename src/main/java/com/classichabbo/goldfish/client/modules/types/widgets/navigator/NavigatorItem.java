package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.modules.controls.Label;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class NavigatorItem extends Pane {
    private Pane nameButton;
    private Pane goButton;

    private String backgroundImg;
    private Double percentageFull;

    public NavigatorItem(NavigatorRoom room) {
        this.setMinHeight(16);
        this.setMaxWidth(311);
        this.calculatePercentageFull(room.visitors, room.maxVisitors);
        
        this.nameButton = new Pane();
        this.nameButton.setPrefSize(251, 16);
        this.nameButton.setCursor(Cursor.HAND);
        this.nameButton.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/room_doorbell_" + NavigatorView.getBackgroundByDoorbell(room.doorbell) + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        
        var nameLabel = new Label(room.name);
        nameLabel.setLayoutX(17);
        nameLabel.setLayoutY(2);
        this.nameButton.getChildren().add(nameLabel);

        this.goButton = new Pane();
        this.goButton.setPrefSize(58, 16);
        this.goButton.setLayoutX(253);
        this.goButton.setCursor(Cursor.HAND);
        this.goButton.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/go_" + this.backgroundImg + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        var goLabel = new Label(TextsManager.getInstance().getString(percentageFull == 1 ? "nav_fullbutton" : "nav_gobutton"));
        goLabel.setLayoutX(18);
        goLabel.setLayoutY(2);
        goLabel.setAlignment(Pos.TOP_RIGHT);
        goLabel.setMinWidth(24);
        goLabel.setUnderline(true);
        goLabel.setTextFill(this.percentageFull == 1 ? Color.web("#D47979") : Color.BLACK);
        this.goButton.getChildren().add(goLabel);

        this.getChildren().addAll(this.nameButton, this.goButton);
    }

    public NavigatorItem(Category category) {
        this.setMinHeight(16);
        this.setMaxWidth(311);
        this.calculatePercentageFull(category.getVisitors(), category.getMaxVisitors());
        
        var nameLabel = new Label(category.name);
        nameLabel.setLayoutX(17);
        nameLabel.setLayoutY(2);
        nameLabel.setTextFill(this.percentageFull == 1 ? Color.web("#D47979") : Color.BLACK);

        var openLabel = new Label(TextsManager.getInstance().getString("nav_openbutton"));
        openLabel.setLayoutX(233);
        openLabel.setLayoutY(2);
        openLabel.setAlignment(Pos.TOP_RIGHT);
        openLabel.setMinWidth(48);
        openLabel.setUnderline(true);
        openLabel.setTextFill(this.percentageFull == 1 ? Color.web("#D47979") : Color.BLACK);

        this.getChildren().addAll(nameLabel, openLabel);
        this.setCursor(Cursor.HAND);
        this.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/category_" + this.backgroundImg + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    private void calculatePercentageFull(int visitors, int maxVisitors) {
        this.percentageFull = (1.0 / maxVisitors) * visitors;

        if (percentageFull == 0) {
            this.backgroundImg = "0";
        } else if (percentageFull < 0.34) {
            this.backgroundImg = "lt_34";
        } else if (percentageFull < 0.76) {
            this.backgroundImg = "lt_76";
        } else if (percentageFull < 1) {
            this.backgroundImg = "lt_100";
        } else if (percentageFull == 1) {
            this.backgroundImg = "100";
        }
    }

    public void setNameButtonOnMouseClicked(EventHandler<MouseEvent> value) {
        this.nameButton.setOnMouseClicked(value);
    }

    public void setGoButtonOnMouseClicked(EventHandler<MouseEvent> value) {
        this.goButton.setOnMouseClicked(value);
    }
}
