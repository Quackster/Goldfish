package com.classichabbo.goldfish.client.modules.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BorderPane extends Pane {
    VBox background;
    Pane backgroundTopCenter;
    Pane backgroundCenterLeft;
    Pane backgroundCenterCenter;
    Pane backgroundCenterRight;
    Pane backgroundBottomCenter;

    Pane content;

    public BorderPane() {
        this.background = new VBox();
        this.content = new Pane();

		// Top

        var backgroundTop = new HBox();

        var backgroundTopLeft = new Pane();
        backgroundTopLeft.setPrefSize(5, 5);
        backgroundTopLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "top_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.backgroundTopCenter = new Pane();
        backgroundTopCenter.setMinSize(1, 5);
        backgroundTopCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "top_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        var backgroundTopRight = new Pane();
        backgroundTopRight.setPrefSize(5, 5);
        backgroundTopRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "top_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        backgroundTop.getChildren().addAll(backgroundTopLeft, this.backgroundTopCenter, backgroundTopRight);

        // Center

        var backgroundCenter = new HBox();

        this.backgroundCenterLeft = new Pane();
        this.backgroundCenterLeft.setPrefWidth(5);
        this.backgroundCenterLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "center_left.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.backgroundCenterCenter = new Pane();
        this.backgroundCenterCenter.setMinSize(1, 1);
        this.backgroundCenterCenter.setBackground(new Background(new BackgroundFill(Color.web("#EFEFEF"), null, null)));

        this.backgroundCenterRight = new Pane();
        this.backgroundCenterRight.setPrefWidth(5);
        this.backgroundCenterRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "center_right.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        backgroundCenter.getChildren().addAll(this.backgroundCenterLeft, this.backgroundCenterCenter, this.backgroundCenterRight);

        // Bottom

        var backgroundBottom = new HBox();

        var backgroundBottomLeft = new Pane();
        backgroundBottomLeft.setPrefSize(5, 5);
        backgroundBottomLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "bottom_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.backgroundBottomCenter = new Pane();
        backgroundBottomCenter.setMinSize(1, 5);
        backgroundBottomCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "bottom_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        var backgroundBottomRight = new Pane();
        backgroundBottomRight.setPrefSize(5, 5);
        backgroundBottomRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/controls/border_pane", "bottom_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        backgroundBottom.getChildren().addAll(backgroundBottomLeft, this.backgroundBottomCenter, backgroundBottomRight);

        this.background.getChildren().addAll(backgroundTop, backgroundCenter, backgroundBottom);

        this.getChildren().addAll(this.background, this.content);
    }

    public void setSize(int width, int height) {
        this.content.setPrefSize(width, height);

        width -= 10;
        height -= 10;
        
        this.backgroundTopCenter.setPrefWidth(width);
        this.backgroundCenterLeft.setPrefHeight(height);
        this.backgroundCenterCenter.setPrefSize(width, height);
        this.backgroundCenterRight.setPrefHeight(height);
        this.backgroundBottomCenter.setPrefWidth(width);
    }

    public void addContent(Node... elements) {
        this.content.getChildren().addAll(elements);
    }

    public void clearContent() {
        this.content.getChildren().clear();
    }
}
