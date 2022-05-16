package com.classichabbo.goldfish.client.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ScrollPane extends HBox {
    private VBox content;
    private ImageButton up;
    private Pane track;
    private ImageButton thumb;
    private ImageButton down;

    private boolean upPressed;
    private boolean thumbPressed;
    private boolean downPressed;
    private double mousePressedY;
    private double draggedY;

    public ScrollPane(int width, int height) {
        this.setPrefSize(width, height);
        this.draggedY = -1;
        this.setClip(new Rectangle(width, height));

        content = new VBox();
        content.setPrefWidth(width - 15);

        var scrollBar = new Pane();
        scrollBar.setPrefSize(15, height);
        
        this.up = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/up.png"));
        this.up.setOnMousePressed(e -> this.upPressed = true);
        this.up.setOnMouseReleased(e -> this.upPressed = false);
        
        this.track = new Pane();
        this.track.setLayoutY(14);
        this.track.setPrefSize(15, height - 28);
        this.track.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/track.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.thumb = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/thumb.png"));

        this.thumb.setOnMouseDragged(e -> {
            this.draggedY = e.getY();
        });
        
        this.thumb.setOnMousePressed(e -> {
            this.mousePressedY = e.getY();
            this.thumbPressed = true;
        });

        this.thumb.setOnMouseReleased(e -> this.thumbPressed = false);

        this.track.getChildren().add(this.thumb);

        this.down = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/down.png"));
        this.down.setLayoutY(height - 14);
        this.down.setOnMousePressed(e -> this.downPressed = true);
        this.down.setOnMouseReleased(e -> this.downPressed = false);

        scrollBar.getChildren().addAll(this.up, this.track, this.down);

        this.getChildren().addAll(content, scrollBar);
    }

    public void addContent(Node... elements) {
        this.content.getChildren().addAll(elements);
    }

    public void update() {
        if (this.draggedY != -1) {
            var y = this.draggedY + thumb.getTranslateY() - this.mousePressedY;
            y = y < 0 ? 0 : y;
            y = y > this.track.getHeight() - 15 ? this.track.getHeight() - 15 : y;

            this.thumb.setTranslateY(y);

            this.draggedY = -1;
        }

        var atTop = this.thumb.getTranslateY() == 0;
        var atBottom = this.thumb.getTranslateY() == track.getHeight() - 15;

        // Disable/enable up button if at top
        if (atTop) {
            this.up.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/up_disabled.png"));
            this.up.setCursor(Cursor.DEFAULT);
        }
        else {
            this.up.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/up.png"));
            this.up.setCursor(Cursor.HAND);
        }

        // Disable/enable down button if at bottom
        if (atBottom) {
            this.down.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/down_disabled.png"));
            this.down.setCursor(Cursor.DEFAULT);
        }
        else {
            this.down.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/down.png"));
            this.down.setCursor(Cursor.HAND);
        }

        // Thumb pressed highlight
        if (this.thumbPressed) {
            this.thumb.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/thumb_pressed.png"));
        }
        else {
            this.thumb.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/thumb.png"));
        }

        // Up pressed
        if (this.upPressed && !atTop) {
            this.up.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/up_pressed.png"));

            var newY = this.thumb.getTranslateY() - 10;
            newY = newY < 0 ? 0 : newY;

            this.thumb.setTranslateY(newY);
        }
        if (!this.upPressed && !atTop) {
            this.up.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/up.png"));
        }

        // Down pressed
        if (this.downPressed && !atBottom) {
            this.down.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/down_pressed.png"));

            var newY = this.thumb.getTranslateY() + 10;
            newY = newY > this.track.getHeight() - 15 ? this.track.getHeight() - 15 : newY;

            this.thumb.setTranslateY(newY);
        }
        if (!this.downPressed && !atBottom) {
            this.down.setImage(ResourceManager.getInstance().getFxImage("sprites/controls/scrollbar/down.png"));
        }

        // Scroll the VBox
        var bottomHeight = this.track.getHeight() - 15;
        var scrolledAmount = (this.thumb.getTranslateY() / bottomHeight);
        System.out.println(this.content.getHeight());
        this.content.setTranslateY(-((this.content.getHeight() - this.getPrefHeight()) * scrolledAmount));
    }
}
