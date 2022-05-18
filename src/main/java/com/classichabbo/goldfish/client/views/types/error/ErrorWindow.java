package com.classichabbo.goldfish.client.views.types.error;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.views.controls.Label;
import com.classichabbo.goldfish.client.views.View;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ErrorWindow extends View {
    private boolean centerWindow;
    private Pane window;

    private Rectangle borderTop;
    private Rectangle borderBottom;
    private Rectangle borderLeft;
    private Rectangle borderRight;
    private Rectangle background;
    private Rectangle mainWindow;

    private Label errorTitleLabel;
    private Label errorMessageLabel;

    private String errorTitle;
    private String errorMessage;

    // Always set these as -1 initially
    private double draggedX = -1;
    private double draggedY = -1;

    private double mousePressedX = -1;
    private double mousePressedY = -1;

    public ErrorWindow(String errorTitle, String errorMessage, boolean centerWindow) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.centerWindow = centerWindow;
    }

    @Override
    public void start() {
        // this.setPickOnBounds(false);
        this.window = new Pane();

        this.background = new Rectangle(DimensionUtil.getProgramWidth(), DimensionUtil.getProgramHeight());
        this.background.setFill(Color.BLACK);
        this.background.setOpacity(0.5f);
        this.background.setTranslateX(0);
        this.background.setTranslateY(0);
        this.getChildren().add(this.background);

        this.borderTop = new Rectangle(340, 1);
        this.borderTop.setFill(Color.BLACK);
        this.window.getChildren().add(this.borderTop);

        this.borderBottom = new Rectangle(340, 1);
        this.borderBottom.setFill(Color.BLACK);
        this.borderBottom.setY(this.borderTop.getY() + 139);
        this.window.getChildren().add(this.borderBottom);

        this.borderLeft = new Rectangle(1, 140);
        this.borderLeft.setFill(Color.BLACK);
        this.window.getChildren().add(this.borderLeft);

        this.borderRight = new Rectangle(1, 140);
        this.borderRight.setFill(Color.BLACK);
        this.borderRight.setX(this.borderTop.getX() + 339);
        this.window.getChildren().add(this.borderRight);

        this.mainWindow = new Rectangle(338, 138);
        this.mainWindow.setFill(Color.WHITE);
        this.mainWindow.setOpacity(0.75f);
        this.window.getChildren().add(this.mainWindow);

        this.errorTitleLabel = new Label(this.errorTitle, true);
        this.errorTitleLabel.setAlignment(Pos.CENTER);
        this.errorTitleLabel.setWrapText(true);
        this.errorTitleLabel.setLineSpacing(3);
        this.errorTitleLabel.setPadding(new Insets(14, 10, 0, 10));
        this.errorTitleLabel.setMaxWidth(this.borderTop.getWidth() - 10);
        this.window.getChildren().add(this.errorTitleLabel);

        this.errorMessageLabel = new Label(this.errorMessage);
        this.errorMessageLabel.setAlignment(Pos.CENTER);
        this.errorMessageLabel.setWrapText(true);
        this.errorMessageLabel.setLineSpacing(3);
        this.errorMessageLabel.setPadding(new Insets(30, 10, 0, 10));
        this.errorMessageLabel.setMaxWidth(this.borderTop.getWidth() - 10);
        this.window.getChildren().add(this.errorMessageLabel);

        this.mainWindow.setX((int) this.borderTop.getX() + 1);
        this.mainWindow.setY((int) this.borderTop.getY() + 1);

        this.getChildren().add(this.window);

        if (!this.centerWindow) {
            this.window.setTranslateX(10);
            this.window.setTranslateY(10);
        }

        this.toFront();
        
        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        if (this.centerWindow) {
            var centerCoords = DimensionUtil.getCenterCoords(this.window.getWidth(), this.window.getHeight());
            this.window.setTranslateX(centerCoords.getX());
            this.window.setTranslateY(centerCoords.getY());
        }

        this.background.setHeight(DimensionUtil.getProgramHeight());
        this.background.setWidth(DimensionUtil.getProgramWidth());
        //this.dragging();
    }

    private void dragging() {
        if (this.draggedX != -1 && this.draggedY != -1) {
            this.window.setTranslateX(this.draggedX + this.window.getTranslateX() - this.mousePressedX);
            this.window.setTranslateY(this.draggedY + this.window.getTranslateY() - this.mousePressedY);

            this.draggedX = -1;
            this.draggedY = -1;
        }
    }
}
