package com.classichabbo.goldfish.client.interfaces.types.error;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.controls.Label;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ErrorWindow extends Interface {
    private boolean centerWindow;

    private Rectangle borderTop;
    private Rectangle borderBottom;
    private Rectangle borderLeft;
    private Rectangle borderRight;

    private Rectangle mainWindow;
    private Label errorTitleLabel;
    private Label errorMessageLabel;

    private String errorTitle;
    private String errorMessage;

    public ErrorWindow(String errorTitle, String errorMessage, boolean centerWindow) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.centerWindow = centerWindow;
    }

    @Override
    public void start() {
        this.borderTop = new Rectangle(340, 1);
        this.borderTop.setFill(Color.BLACK);
        this.getChildren().add(this.borderTop);

        this.borderBottom = new Rectangle(340, 1);
        this.borderBottom.setFill(Color.BLACK);
        this.borderBottom.setY(this.borderTop.getY() + 139);
        this.getChildren().add(this.borderBottom);

        this.borderLeft = new Rectangle(1, 140);
        this.borderLeft.setFill(Color.BLACK);
        this.getChildren().add(this.borderLeft);

        this.borderRight = new Rectangle(1, 140);
        this.borderRight.setFill(Color.BLACK);
        this.borderRight.setX(this.borderTop.getX() + 339);
        this.getChildren().add(this.borderRight);

        this.mainWindow = new Rectangle(338, 138);
        this.mainWindow.setFill(Color.WHITE);
        this.mainWindow.setOpacity(0.75f);
        this.getChildren().add(this.mainWindow);

        this.errorTitleLabel = new Label(this.errorTitle, true);
        this.errorTitleLabel.setAlignment(Pos.CENTER);
        this.errorTitleLabel.setWrapText(true);
        this.errorTitleLabel.setLineSpacing(3);
        this.errorTitleLabel.setPadding(new Insets(14, 10, 0, 10));
        this.errorTitleLabel.setMaxWidth(this.borderTop.getWidth() - 10);
        this.getChildren().add(this.errorTitleLabel);

        this.errorMessageLabel = new Label(this.errorMessage);
        this.errorMessageLabel.setAlignment(Pos.CENTER);
        this.errorMessageLabel.setWrapText(true);
        this.errorMessageLabel.setLineSpacing(3);
        this.errorMessageLabel.setPadding(new Insets(30, 10, 0, 10));
        this.errorMessageLabel.setMaxWidth(this.borderTop.getWidth() - 10);
        this.getChildren().add(this.errorMessageLabel);

        this.mainWindow.setX((int) this.borderTop.getX() + 1);
        this.mainWindow.setY((int) this.borderTop.getY() + 1);

        if (this.centerWindow) {
            Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
        } else {
            this.setTranslateX(10);
            this.setTranslateY(10);
        }
    }

    @Override
    public void stop() {
        if (this.centerWindow) {
            Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
        }
    }

    @Override
    public void update() {
        var centerCoords = DimensionUtil.getCenterCoords(this.getWidth(), this.getHeight());
        this.setTranslateX(centerCoords.getX());
        this.setTranslateY(centerCoords.getY());
    }
}
