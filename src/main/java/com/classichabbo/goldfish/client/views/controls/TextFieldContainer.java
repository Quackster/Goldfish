package com.classichabbo.goldfish.client.views.controls;

import com.classichabbo.goldfish.client.Movie;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TextFieldContainer extends Pane {
    private Pane caret;
    private int caretAnimation;
    protected TextField text;

    public TextFieldContainer() {
        this.caretAnimation = 0;

        this.caret = new Pane();
        this.caret.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.caret.setPrefSize(1, 9);//isBold ? 11 : 10);
        this.caret.setVisible(false);

        this.getChildren().add(this.caret);
    }

    public void initCaret(double x, double y) {
        this.caret.setLayoutX(x);
        this.caret.setLayoutY(y);
        this.caret.toFront();
    }

    public void moveCaret(double x, double y) {
        this.caret.setTranslateX(x);
        this.caret.setTranslateY(y);
    }

    public void update() {
        if (Movie.getInstance().getCurrentTextField() == this.text) {
            this.getParent().setCursor(Cursor.TEXT);

            if (this.caretAnimation == 0) {
                this.caret.setVisible(true);
            }
            if (this.caretAnimation == 10) {
                this.caret.setVisible(false);
            }
            if (this.caretAnimation == 20) {
                this.caretAnimation = -1;
            }

            this.caretAnimation++;
        }
        else {
            this.getParent().setCursor(Cursor.DEFAULT);
            this.caret.setVisible(false);
        }
    }
}
