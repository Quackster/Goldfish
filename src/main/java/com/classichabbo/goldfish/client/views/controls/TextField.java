package com.classichabbo.goldfish.client.views.controls;

import java.util.Arrays;
import java.util.List;

import com.classichabbo.goldfish.client.Movie;

import javafx.scene.Cursor;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TextField extends Pane {
    // TODO - highlighting, mouse move caret
    private Pane container;
    private Label text;
    private Label beforeRender;
    private Label beforeCaret;
    private Pane caret;
    private Boolean isPassword;
    private int caretPosition;
    private int caretAnimation;
    private int right;
    private String value;
    private String beforeRenderValue;

    private final List<String> volter = Arrays.asList(" ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", ">", "=", "<", "?", "@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~", " ", "¡", "¢", "£", "¤", "¥", "§", "¨", "©", "ª", "«", "¬", "®", "¯", "°", "±", "´", "µ", "¶", "·", "¸", "º", "»", "¿", "À", "Á", "Â", "Ã", "Ä", "Å", "Æ", "Ç", "È", "É", "Ê", "Ë", "Ì", "Í", "Î", "Ï", "Ñ", "Ò", "Ó", "Ô", "Õ", "Ö", "Ø", "Ù", "Ú", "Û", "Ü", "ß", "à", "á", "â", "ã", "ä", "å", "æ", "ç", "è", "é", "ê", "ë", "ì", "í", "î", "ï", "ñ", "ò", "ó", "ô", "õ", "ö", "÷", "ø", "ù", "ú", "û", "ü", "ÿ", "ı", "Œ", "œ", "Ÿ", "ƒ", "ˆ", "ˇ", "˘", "˙", "˚", "˛", "˜", "˝", "π", "–", "—", "‘", "’", "‚", "“", "”", "„", "†", "‡", "•", "…", "‰", "‹", "›", "⁄", "€", "™", "Ω", "∂", "∆", "∏", "∑", "√", "∞", "∫", "≈", "≠", "≤", "≥", "◊", "", "ﬁ", "ﬂ");

    public TextField(String text, Boolean isBold, Boolean isPassword) {
        this.isPassword = isPassword;
        this.caretAnimation = 0;
        this.beforeRenderValue = text;
        this.value = "";

        this.caret = new Pane();
        this.caret.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.caret.setPrefSize(1, isBold ? 11 : 10);
        this.caret.setLayoutY(1);

        this.container = new Pane();

        this.text = new Label("", isBold);
        this.container.getChildren().add(this.text);

        this.beforeRender = new Label(text, isBold);
        this.beforeRender.setVisible(false);
        this.beforeRender.setOnWidth(() -> {
            if (this.beforeRender.getWidth() < this.container.getMaxWidth() - this.right && this.beforeRender.getText() != null) {
                this.text.setText(this.beforeRender.getText());
                this.value = this.beforeRenderValue;
                this.updateCaret(true);
            }

            this.beforeRender.setText(null);
            this.beforeRenderValue = null;
        });

        this.beforeCaret = new Label("", isBold);
        this.beforeCaret.setVisible(false);
        this.beforeCaret.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            this.caret.setTranslateX(this.beforeCaret.getWidth());
        });

        this.getChildren().addAll(this.container, this.beforeRender, this.beforeCaret, this.caret);
        this.setOnMouseClicked(e -> Movie.getInstance().setCurrentTextField(this));
    }

    public void update() {
        if (Movie.getInstance().getCurrentTextField() == this) {
            this.setCursor(Cursor.TEXT);

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
            this.setCursor(Cursor.DEFAULT);
            this.caret.setVisible(false);
        }
    }

    public String getText() {
        System.out.println(this.value);
        return this.value;
    }

    public int getTextWidth() {
        return (int)Math.round(this.text.getWidth());
    }

    public void setText(String text) {
        this.value = text;
        this.text.setText(text);
        this.caretPosition = 0;
        this.beforeCaret.setText("");
    }

    public void setSize(int width, int height, int left, int right, int topBottom) {
        this.setPrefSize(width, height);

        this.right = right;

        this.container.setLayoutX(left);
        this.container.setLayoutY(topBottom);
        this.container.setMaxSize(width - left - right, height - (topBottom * 2));
        this.container.setClip(new Rectangle(width - left - right, height - (topBottom * 2) + 2));

        this.caret.setLayoutX(left);
        this.caret.setTranslateY(topBottom);
    }

    public void setOnWidth(Runnable runnable) {
        this.text.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            runnable.run();
        });
    }

    public void sendKeyPressed(KeyEvent e) {
        switch(e.getCode()) {
            case BACK_SPACE:
                if (this.caretPosition != 0) {
                    this.value = this.value.substring(0, this.caretPosition - 1) + this.value.substring(this.caretPosition);

                    if (this.isPassword) {
                        this.text.setText(this.text.getText().substring(1));
                    }
                    else {
                        this.text.setText(this.value);
                    }
                    this.updateCaret(false);
                }
                break;
            case DELETE:
                if (this.caretPosition != this.value.length()) {
                    this.value = this.value.substring(0, this.caretPosition) + this.value.substring(this.caretPosition + 1);

                    if (this.isPassword) {
                        this.text.setText(this.text.getText().substring(1));
                    }
                    else {
                        this.text.setText(this.value);
                    }
                }
                break;
            case LEFT:
                this.updateCaret(false);
                break;
            case RIGHT:
                this.updateCaret(true);
                break;
            default:
                break;
        }
    }

    public void sendKeyTyped(KeyEvent e) {
        if (volter.contains(e.getCharacter())) {
            this.beforeRenderValue = this.value.substring(0, this.caretPosition) + e.getCharacter() + this.value.substring(this.caretPosition);
            if (this.isPassword) {
                this.beforeRender.setText(this.text.getText() + "*");
            }
            else {
                this.beforeRender.setText(this.value.substring(0, this.caretPosition) + e.getCharacter() + this.value.substring(this.caretPosition));
            }
        }
    }

    private void updateCaret(Boolean increment) {
        if ((increment && this.caretPosition == this.value.length()) || (!increment && this.caretPosition == 0)) {
            return;
        }

        this.caretPosition = increment ? this.caretPosition + 1 : this.caretPosition - 1;
        
        this.beforeCaret.setText(this.text.getText().substring(0, this.caretPosition));
    }
}