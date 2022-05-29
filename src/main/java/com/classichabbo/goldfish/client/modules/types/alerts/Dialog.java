package com.classichabbo.goldfish.client.modules.types.alerts;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.util.DimensionUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Dialog extends View {
    private HBox top;
    private Pane topLeft;
    private Pane topCenter;
    private Pane topRight;
    private Pane centerLeft;
    private Pane centerCenter;
    private Pane centerRight;
    private Pane bottomCenter;

    private VBox innerBackground;
    private Pane innerTopCenter;
    private Pane innerCenterLeft;
    private Pane innerCenterCenter;
    private Pane innerCenterRight;
    private Pane innerBottomCenter;
    
    private Label title;
    private Pane dragArea;
    private Pane closeButton;
    private Pane content;
    private Pane newContent;

    private Runnable callBeforeFinish;
    private Runnable callAfterFinish;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    private boolean isSized;
    private boolean sizedOnce;
    private boolean showAfterSize;

    private double mousePressedX;
    private double mousePressedY;

    private boolean clicked;
    private double draggedX;
    private double draggedY;

    // TO-DO
    // - pick on bounds (clicking corner outside of background moves to front)
    // - font slightly off on smaller alerts (x is -1px)

    @Override
    public void start() {
        this.setVisible(false);
        this.setOnMousePressed(e -> this.clicked = true);
        this.initBackground();
    }

    @Override
    public void stop() {
        System.out.println("Called");
        this.clicked = false;
        this.isSized = true;
    }

    @Override
    public void update() {
        this.adjustSize();

        // Click bring-to-front handler
        if (this.clicked) {
            this.toFront();
            this.clicked = false;
        }

        // Dragging
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.setTranslateX(this.draggedX + this.getTranslateX() - this.mousePressedX);
            this.setTranslateY(this.draggedY + this.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }

    private void adjustSize() {
        if (!this.isSized) {
            if (this.callBeforeFinish != null) {
                this.callBeforeFinish.run();
                this.callBeforeFinish = null;
            }

            if (this.newContent != null && this.newContent.getWidth() > 0) {
                this.newContent.setLayoutX(this.paddingLeft);
                this.newContent.setLayoutY(this.paddingTop + (this.title != null ? 20 : 0));

                var contentWidth = this.newContent != null ? (this.newContent.getWidth() + this.paddingLeft + this.paddingRight) : 0;
                var contentHeight = this.newContent != null ? (this.newContent.getHeight() + this.paddingTop + this.paddingBottom) : 0;

                var dialogWidth = this.title != null ? Math.max(this.title.getWidth(), contentWidth) : contentWidth;
                var dialogHeight = this.title != null ? contentHeight + 15 : contentHeight;

                this.setSize(dialogWidth, dialogHeight);

                // Only do this the first time
                if (!this.sizedOnce) {
                    var coords = DimensionUtil.getCenterCoords(dialogWidth, dialogHeight);
                    this.setLayoutX(coords.getX());
                    this.setLayoutY(coords.getY());
                }

                if (this.title != null) {
                    this.title.setLayoutX(Math.round((dialogWidth / 2) - (this.title.getWidth() / 2)) - 2);
                    this.dragArea.setPrefSize(dialogWidth, 31);
                    this.closeButton.setLayoutX(dialogWidth - 25);
                }

                if (this.innerBackground != null) {
                    this.innerBackground.setLayoutX(paddingLeft);
                    this.innerBackground.setLayoutY(paddingTop + (title != null ? 20 : 0));
                    setInnerSize(this.newContent.getWidth(), this.newContent.getHeight());
                }

                this.newContent.setVisible(true);
                this.getChildren().remove(this.content);
                this.content = this.newContent;
                this.newContent = null;

                if (this.callAfterFinish != null) {
                    this.callAfterFinish.run();
                    this.callAfterFinish = null;
                }

                if (this.showAfterSize) {
                    this.setHidden(false);
                }

                // Alert was finished sizing, don't size again!
                this.isSized = true;
                this.sizedOnce = true;
            }
        }
    }

    private void initBackground() {
        VBox background = new VBox();

        // Top

        this.top = new HBox();
        this.top.setMinHeight(16);

        this.topLeft = new Pane();
        this.topLeft.setPrefSize(16, 16);
        this.topLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "top_left_notitle.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.topCenter = new Pane();
        this.topCenter.setMinSize(1, 16);
        this.topCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "top_center_notitle.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.topRight = new Pane();
        this.topRight.setPrefSize(16, 16);
        this.topRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "top_right_notitle.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.top.getChildren().addAll(this.topLeft, this.topCenter, this.topRight);

        // Center

        HBox center = new HBox();

        this.centerLeft = new Pane();
        this.centerLeft.setPrefWidth(16);
        this.centerLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "center_left.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.centerCenter = new Pane();
        this.centerCenter.setMinSize(1, 1);
        this.centerCenter.setBackground(new Background(new BackgroundFill(Color.web("#6794A7"), null, null)));

        this.centerRight = new Pane();
        this.centerRight.setPrefWidth(16);
        this.centerRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "center_right.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        center.getChildren().addAll(this.centerLeft, this.centerCenter, this.centerRight);

        // Bottom

        HBox bottom = new HBox();

        Pane bottomLeft = new Pane();
        bottomLeft.setPrefSize(16, 16);
        bottomLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "bottom_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.bottomCenter = new Pane();
        bottomCenter.setMinSize(1, 19);
        bottomCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "bottom_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane bottomRight = new Pane();
        bottomRight.setPrefSize(16, 16);
        bottomRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "bottom_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        bottom.getChildren().addAll(bottomLeft, this.bottomCenter, bottomRight);

        background.getChildren().addAll(this.top, center, bottom);

        this.getChildren().add(background);
    }

    protected void initInnerBackground() {
        this.innerBackground = new VBox();

		// Top

        HBox innerTop = new HBox();

        Pane innerTopLeft = new Pane();
        innerTopLeft.setPrefSize(5, 5);
        innerTopLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/top_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.innerTopCenter = new Pane();
        innerTopCenter.setMinSize(1, 5);
        innerTopCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/top_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane innerTopRight = new Pane();
        innerTopRight.setPrefSize(6, 5);
        innerTopRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/top_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerTop.getChildren().addAll(innerTopLeft, this.innerTopCenter, innerTopRight);

        // Center

        HBox innerCenter = new HBox();

        this.innerCenterLeft = new Pane();
        this.innerCenterLeft.setPrefWidth(5);
        this.innerCenterLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/center_left.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.innerCenterCenter = new Pane();
        this.innerCenterCenter.setMinSize(1, 1);
        this.innerCenterCenter.setBackground(new Background(new BackgroundFill(Color.web("#EFEFEF"), null, null)));

        this.innerCenterRight = new Pane();
        this.innerCenterRight.setPrefWidth(6);
        this.innerCenterRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/center_right.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerCenter.getChildren().addAll(this.innerCenterLeft, this.innerCenterCenter, this.innerCenterRight);

        // Bottom

        HBox innerBottom = new HBox();

        Pane innerBottomLeft = new Pane();
        innerBottomLeft.setPrefSize(5, 6);
        innerBottomLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/bottom_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.innerBottomCenter = new Pane();
        innerBottomCenter.setMinSize(1, 6);
        innerBottomCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/bottom_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane innerBottomRight = new Pane();
        innerBottomRight.setPrefSize(6, 6);
        innerBottomRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "inner/bottom_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerBottom.getChildren().addAll(innerBottomLeft, this.innerBottomCenter, innerBottomRight);

        this.innerBackground.getChildren().addAll(innerTop, innerCenter, innerBottom);

        this.getChildren().add(this.innerBackground);
    }

    private void setSize(double width, double height) {
        // 29 is the width of the 'borders' not including the shadow
        width -= 29;
        height -= 29;

        this.topCenter.setPrefWidth(width - (this.title != null ? 8 : 0));
        this.centerLeft.setPrefHeight(height);
        this.centerCenter.setPrefSize(width, height);
        this.centerRight.setPrefHeight(height);
        this.bottomCenter.setPrefWidth(width);
    }

    private void setInnerSize(double width, double height) {
        // 11 is the width of the 'borders' (same as above :P)
        width -= 11;
        height -= 11;

        this.innerTopCenter.setPrefWidth(width);
        this.innerCenterLeft.setPrefHeight(height);
        this.innerCenterCenter.setPrefSize(width, height);
        this.innerCenterRight.setPrefHeight(height);
        this.innerBottomCenter.setPrefWidth(width);
    }

    protected void setTitle(String title) {
        this.top.setMinHeight(21);

        this.topLeft.setPrefSize(16, 21);
        this.topLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "top_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.topCenter.setMinSize(1, 21);
        this.topCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "top_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.topRight.setPrefSize(24, 21);
        this.topRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "top_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.title = new Label(title);
        this.title.setBackground(new Background(new BackgroundFill(Color.web("#6794A7"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.title.setPadding(new Insets(4, 9, 0, 9));
        this.title.setLayoutY(5);
        this.title.setAlignment(Pos.CENTER);
        this.title.setFont(ResourceManager.getInstance().getVolterBold());
        this.title.setTextFill(Color.web("#EEEEEE"));

        this.dragArea = new Pane();

        this.dragArea.setOnMousePressed(e -> {
            this.mousePressedX = e.getX();
            this.mousePressedY = e.getY();
        });

        this.dragArea.setOnMouseDragged(e -> {
            this.draggedX = e.getX();
            this.draggedY = e.getY();
        });

        this.closeButton = new Pane();
        this.closeButton.setCursor(Cursor.HAND);
        this.closeButton.setPrefSize(13, 13);
        this.closeButton.setLayoutY(6);
        this.closeButton.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/controls/dialog/", "close_button.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.closeButton.setOnMouseClicked(e -> this.closeButtonClicked());

        this.getChildren().addAll(this.title, this.dragArea, this.closeButton);
    }

    protected void setContent(Pane content, Insets padding, boolean showAfter) {
        this.callBeforeFinish = () -> {
            this.newContent = content;
            this.newContent.setVisible(false);
            this.paddingTop = (int)padding.getTop();
            this.paddingRight = (int)padding.getRight();
            this.paddingBottom = (int)padding.getBottom();
            this.paddingLeft = (int)padding.getLeft();

            this.getChildren().add(this.newContent);
        };

        this.isSized = false;
        this.showAfterSize = showAfter;
    }

    protected void closeButtonClicked() {
        this.remove();
    }

    /**
     * Used to move the dialog around after it has finished resizing. Useful for getting height/width as it's not fully determined until initialisation is complete.
     * @param callAfterFinish
     */
    public void setCallAfterFinish(Runnable callAfterFinish) {
        this.callAfterFinish = callAfterFinish;
    }

    /**
     * Get if this widget has finished initialising and properly sized itself.
     */
    public boolean isCreated() {
        return isSized;
    }
}
