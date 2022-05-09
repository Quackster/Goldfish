package org.alexdev.krishna.rendering.interfaces;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;

import org.alexdev.krishna.HabboClient;

public class Dialog extends Interface {
    public VBox content;

    private Pane topCenter;
    private Pane centerLeft;
    private Pane centerCenter;
    private Pane centerRight;
    private Pane bottomCenter;

    private double x;
    private double y;

    private boolean isInitialised;

    @Override
    public void init() {
        if (this.isInitialised)
            return;

        setOnMouseClicked(event -> {
            toFront();
        });

        VBox background = new VBox();

        // Top

        HBox top = new HBox();
        top.setMinHeight(21);

        Pane topLeft = new Pane();
        topLeft.setPrefSize(16, 21);
        topLeft.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/top_left.png").toURI().toString(), 16, 21, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        topCenter = new Pane();
        topCenter.setMinSize(1, 21);
        topCenter.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/top_center.png").toURI().toString(), 2, 21, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane topRight = new Pane();
        topRight.setPrefSize(16, 21);
        topRight.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/top_right.png").toURI().toString(), 16, 21, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        top.getChildren().addAll(topLeft, topCenter, topRight);

        // Center

        HBox center = new HBox();

        centerLeft = new Pane();
        centerLeft.setPrefWidth(16);
        centerLeft.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/center_left.png").toURI().toString(), 16, 1, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        centerCenter = new Pane();
        centerCenter.setMinSize(1, 1);
        centerCenter.setBackground(new Background(new BackgroundFill(Color.web("#6794A7"), null, null)));

        centerRight = new Pane();
        centerRight.setPrefWidth(16);
        centerRight.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/center_right.png").toURI().toString(), 16, 1, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        center.getChildren().addAll(centerLeft, centerCenter, centerRight);

        // Bottom

        HBox bottom = new HBox();

        Pane bottomLeft = new Pane();
        bottomLeft.setPrefSize(16, 16);
        bottomLeft.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/bottom_left.png").toURI().toString(), 16, 16, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        bottomCenter = new Pane();
        bottomCenter.setMinSize(1, 19);
        bottomCenter.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/bottom_center.png").toURI().toString(), 1, 16, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Pane bottomRight = new Pane();
        bottomRight.setPrefSize(16, 16);
        bottomRight.setBackground(new Background(new BackgroundImage(new Image(new File("resources/dialog/bottom_right.png").toURI().toString(), 16, 16, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        bottom.getChildren().addAll(bottomLeft, bottomCenter, bottomRight);

        background.getChildren().addAll(top, center, bottom);
        
        // testing only
        //centerCenter.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF"), null, null)));

        var title = new Label("Test");
        title.setBackground(new Background(new BackgroundFill(Color.web("#6794A7"), CornerRadii.EMPTY, Insets.EMPTY)));
        title.setPadding(new Insets(4, 9, 0, 9));
        title.setLayoutY(5);
        title.setAlignment(Pos.CENTER);
        title.setFont(HabboClient.volterBold);
        title.setTextFill(Color.web("#EEEEEE"));

        content = new VBox();

        content.setMinSize(0, 0);
        content.setPadding(new Insets(16, 16, 16, 16));
        //content.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setLayoutX(16);
        content.setLayoutY(21);

        System.out.println(content.getWidth() + " " + content.getHeight());

        var topArea = new Pane();
        //topArea.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        topArea.setOnMousePressed((event) -> {
            x = event.getX();
            y = event.getY();
        });

        topArea.setOnMouseDragged(event -> {
            setManaged(false);
            this.setTranslateX(event.getX() + this.getTranslateX() - x);
            this.setTranslateY(event.getY() + this.getTranslateY() - y);
            event.consume();
        });

        getChildren().addAll(background, title, content, topArea);

        Platform.runLater(() -> {
            var width = Math.max(title.getWidth(), content.getWidth());

            setSize(width, content.getHeight());

            topArea.setPrefWidth(width + 32);
            topArea.setPrefHeight(21);

            title.setLayoutX(((width + 32) / 2) - (title.getWidth() / 2));
        });

        this.isInitialised = true;
    }

    public void setSize(double width, double height) {
        topCenter.setPrefWidth(width);
        centerLeft.setPrefHeight(height);
        centerCenter.setPrefSize(width, height);
        centerRight.setPrefHeight(height);
        bottomCenter.setPrefWidth(width);
    }

    /**
     * Update tick to resize the images and boxes necessary for responsiveness
     */
    public void update() {
        if (!this.isInitialised)
            return;

        // do
    }

    @Override
    public boolean isReady() {
        return isInitialised;
    }
}
