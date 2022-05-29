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
    private NavigatorNode node;
                    /*
                        public NavigatorRoom(int roomId, String name, String description, String infoImg, int visitors, int maxVisitors) {
    public NavigatorRoom(int id, String name, String owner, String description, int visitors, int maxVisitors, Doorbell doorbell) {
                     */

    public NavigatorItem(NavigatorNode node) {
        this.node = node;
        //if (room.getNodeType() == 2) { // rooms?
        if (node.isRoom()) {
            this.applyRoom();
        } else {
            this.applyCategory();
        }
    }

    private void applyRoom() {
        if (this.node.getNodeType() == NavigatorNodeType.PUBLIC_ROOM) {
            this.node.setDescription(TextsManager.getInstance().getString("nav_venue_" + this.node.getUnitStrId() + "/" + this.node.getDoor() + "_desc", ""));
        }

        this.setMinHeight(16);
        this.setMaxWidth(311);
        this.calculatePercentageFull(this.node.getUsercount(), this.node.getMaxUsers());

        this.nameButton = new Pane();
        this.nameButton.setPrefSize(251, 16);
        this.nameButton.setCursor(Cursor.HAND);
        this.nameButton.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/views/navigator", "room_doorbell_" + NavigatorView.getBackgroundByDoorbell(this.node.getDoorbell()) + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        var nameLabel = new Label(this.node.getName());
        nameLabel.setLayoutX(17);
        nameLabel.setLayoutY(2);
        this.nameButton.getChildren().add(nameLabel);

        this.goButton = new Pane();
        this.goButton.setPrefSize(58, 16);
        this.goButton.setLayoutX(253);
        this.goButton.setCursor(Cursor.HAND);
        this.goButton.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/views/navigator", "go_" + this.backgroundImg + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

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

    public void applyCategory() {
        this.setMinHeight(16);
        this.setMaxWidth(311);
        this.calculatePercentageFull(this.node.getUsercount(), this.node.getMaxUsers());
        
        var nameLabel = new Label(this.node.getName());
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
        this.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/views/navigator", "category_" + this.backgroundImg + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    private void calculatePercentageFull(int visitors, int maxVisitors) {
        if (maxVisitors <= 0) {
            this.backgroundImg = "0";
            this.percentageFull = 0d;
            return; // divide by 0 bullshit
        }

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

    public NavigatorNode getNode() {
        return node;
    }
}
