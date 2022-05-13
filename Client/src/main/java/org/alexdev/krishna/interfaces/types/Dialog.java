package org.alexdev.krishna.interfaces.types;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.resources.ResourceManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.interfaces.InterfaceType;
import org.alexdev.krishna.util.DimensionUtil;

public class Dialog extends Interface {
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
    private Pane content;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    private boolean isSized;
    private boolean isInitialised;

    private double mousePressedX;
    private double mousePressedY;

    private boolean clicked;
    private double draggedX;
    private double draggedY;

    // TO-DO -
    // pick on bounds (clicking corner outside of background moves to front)

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        this.initBackground();

        this.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> this.clicked = true);

        // Base initialisation done, override when extending if necessary
        this.isInitialised = true;
    }

    @Override
    public void sceneChanged() {
    }

    private void initBackground() {
        VBox background = new VBox();

        // Top

        top = new HBox();
        top.setMinHeight(16);

        topLeft = new Pane();
        topLeft.setPrefSize(16, 16);
        topLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/top_left_notitle.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        topCenter = new Pane();
        topCenter.setMinSize(1, 16);
        topCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/top_center_notitle.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        topRight = new Pane();
        topRight.setPrefSize(16, 16);
        topRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/top_right_notitle.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        top.getChildren().addAll(topLeft, topCenter, topRight);

        // Center

        HBox center = new HBox();

        centerLeft = new Pane();
        centerLeft.setPrefWidth(16);
        centerLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/center_left.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        centerCenter = new Pane();
        centerCenter.setMinSize(1, 1);
        centerCenter.setBackground(new Background(new BackgroundFill(Color.web("#6794A7"), null, null)));

        centerRight = new Pane();
        centerRight.setPrefWidth(16);
        centerRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/center_right.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        center.getChildren().addAll(centerLeft, centerCenter, centerRight);

        // Bottom

        HBox bottom = new HBox();

        Pane bottomLeft = new Pane();
        bottomLeft.setPrefSize(16, 16);
        bottomLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/bottom_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        bottomCenter = new Pane();
        bottomCenter.setMinSize(1, 19);
        bottomCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/bottom_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane bottomRight = new Pane();
        bottomRight.setPrefSize(16, 16);
        bottomRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/bottom_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        bottom.getChildren().addAll(bottomLeft, bottomCenter, bottomRight);

        background.getChildren().addAll(top, center, bottom);

        getChildren().add(background);
    }

    private void setSize(double width, double height) {
        // 29 is the width of the 'borders' not including the shadow
        width -= 29;
        height -= 29;
        
        topCenter.setPrefWidth(width);
        centerLeft.setPrefHeight(height);
        centerCenter.setPrefSize(width, height);
        centerRight.setPrefHeight(height);
        bottomCenter.setPrefWidth(width);
    }

    private void setInnerSize(double width, double height) {
        // 11 is the width of the 'borders' (same as above :P)
        width -= 11;
        height -= 11;

        innerTopCenter.setPrefWidth(width);
        innerCenterLeft.setPrefHeight(height);
        innerCenterCenter.setPrefSize(width, height);
        innerCenterRight.setPrefHeight(height);
        innerBottomCenter.setPrefWidth(width);
    }

    protected void initInnerBackground() {
        innerBackground = new VBox();

		// Top

        HBox innerTop = new HBox();

        Pane innerTopLeft = new Pane();
        innerTopLeft.setPrefSize(5, 5);
        innerTopLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/top_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerTopCenter = new Pane();
        innerTopCenter.setMinSize(1, 5);
        innerTopCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/top_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane innerTopRight = new Pane();
        innerTopRight.setPrefSize(6, 5);
        innerTopRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/top_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerTop.getChildren().addAll(innerTopLeft, innerTopCenter, innerTopRight);

        // Center

        HBox innerCenter = new HBox();

        innerCenterLeft = new Pane();
        innerCenterLeft.setPrefWidth(5);
        innerCenterLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/center_left.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerCenterCenter = new Pane();
        innerCenterCenter.setMinSize(1, 1);
        innerCenterCenter.setBackground(new Background(new BackgroundFill(Color.web("#EFEFEF"), null, null)));

        innerCenterRight = new Pane();
        innerCenterRight.setPrefWidth(6);
        innerCenterRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/center_right.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerCenter.getChildren().addAll(innerCenterLeft, innerCenterCenter, innerCenterRight);

        // Bottom

        HBox innerBottom = new HBox();

        Pane innerBottomLeft = new Pane();
        innerBottomLeft.setPrefSize(5, 6);
        innerBottomLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/bottom_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerBottomCenter = new Pane();
        innerBottomCenter.setMinSize(1, 6);
        innerBottomCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/bottom_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane innerBottomRight = new Pane();
        innerBottomRight.setPrefSize(6, 6);
        innerBottomRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/inner/bottom_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        innerBottom.getChildren().addAll(innerBottomLeft, innerBottomCenter, innerBottomRight);

        innerBackground.getChildren().addAll(innerTop, innerCenter, innerBottom);

        getChildren().add(innerBackground);
        content.toFront();
    }

    protected void setTitle(String title) {
        top.setMinHeight(21);

        topLeft.setPrefSize(16, 21);
        topLeft.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/top_left.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        topCenter.setMinSize(1, 21);
        topCenter.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/top_center.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        topRight.setPrefSize(16, 21);
        topRight.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/dialog/top_right.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.title = new Label(title);
        this.title.setBackground(new Background(new BackgroundFill(Color.web("#6794A7"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.title.setPadding(new Insets(4, 9, 0, 9));
        this.title.setLayoutY(5);
        this.title.setAlignment(Pos.CENTER);
        this.title.setFont(ResourceManager.getInstance().getVolterBold());
        this.title.setTextFill(Color.web("#EEEEEE"));

        dragArea = new Pane();

        dragArea.setOnMousePressed(event -> {
            this.mousePressedX = event.getX();
            this.mousePressedY = event.getY();
        });

        dragArea.setOnMouseDragged(event -> {
            this.draggedX = event.getX();
            this.draggedY = event.getY();
            /*
            setManaged(false);
            this.setTranslateX(event.getX() + this.getTranslateX() - x);
            this.setTranslateY(event.getY() + this.getTranslateY() - y);
            event.consume();
             */
        });

        getChildren().addAll(this.title, dragArea);
    }

    protected void setContent(Pane content) {
        this.content = content;
        getChildren().add(content);
    }

    protected void setPadding(int paddingTop, int paddingRight, int paddingBottom, int paddingLeft) {
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
    }

    @Override
    public void update() {
        if (!this.isInitialised)
            return;

        if (!this.isSized && content.getWidth() > 0) {
            content.setLayoutX(paddingLeft);
            content.setLayoutY(paddingTop + (title != null ? 20 : 0));
    
            var contentWidth = content != null ? (content.getWidth() + paddingLeft + paddingRight) : 0;
            var contentHeight = content != null ? (content.getHeight() + paddingTop + paddingBottom) : 0;
    
            var width = title != null ? Math.max(title.getWidth(), contentWidth) : contentWidth;
            var height = title != null ? contentHeight + 15 : contentHeight;
    
            setSize(width, height);
            setLayoutX(Math.ceil((HabboClient.getInstance().getPrimaryStage().getWidth() - this.getWidth()) / 2));
            setLayoutY(Math.ceil((HabboClient.getInstance().getPrimaryStage().getHeight() - this.getHeight()) / 2));

            var coords = DimensionUtil.getCenterCords(width, height);
            this.setLayoutX(coords.getX());
            this.setLayoutY(coords.getY());
    
            if (title != null)
                title.setLayoutX(Math.round((width / 2) - (title.getWidth() / 2)));
    
            if (dragArea != null)
                dragArea.setPrefSize(width, 31);
    
            if (innerBackground != null) {
                innerBackground.setLayoutX(paddingLeft);
                innerBackground.setLayoutY(paddingTop + (title != null ? 20 : 0));
                setInnerSize(content.getWidth(), content.getHeight());
            }

            this.isSized = true;
        }

        if (this.clicked) {
            this.toFront();
            this.clicked = false;
        }

        if (this.draggedX != -1 && this.draggedY != -1) {
            this.setTranslateX(this.draggedX + this.getTranslateX() - this.mousePressedX);
            this.setTranslateY(this.draggedY + this.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }

    @Override
    public boolean isReady() {
        return isInitialised;
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.DIALOG;
    }
}
