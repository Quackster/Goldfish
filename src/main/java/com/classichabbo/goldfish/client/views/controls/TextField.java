package com.classichabbo.goldfish.client.views.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextField extends FlowPane {
    private Color color;
    private Font font;
    private String text;
    private boolean isPassword;
    private int lines;
    private boolean center;
    private TextFieldContainer parent;

    private boolean full;
    private int caretPosition = 0;

    private final List<String> volter = Arrays.asList(" ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", ">", "=", "<", "?", "@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~", " ", "¡", "¢", "£", "¤", "¥", "§", "¨", "©", "ª", "«", "¬", "®", "¯", "°", "±", "´", "µ", "¶", "·", "¸", "º", "»", "¿", "À", "Á", "Â", "Ã", "Ä", "Å", "Æ", "Ç", "È", "É", "Ê", "Ë", "Ì", "Í", "Î", "Ï", "Ñ", "Ò", "Ó", "Ô", "Õ", "Ö", "Ø", "Ù", "Ú", "Û", "Ü", "ß", "à", "á", "â", "ã", "ä", "å", "æ", "ç", "è", "é", "ê", "ë", "ì", "í", "î", "ï", "ñ", "ò", "ó", "ô", "õ", "ö", "÷", "ø", "ù", "ú", "û", "ü", "ÿ", "ı", "Œ", "œ", "Ÿ", "ƒ", "ˆ", "ˇ", "˘", "˙", "˚", "˛", "˜", "˝", "π", "–", "—", "‘", "’", "‚", "“", "”", "„", "†", "‡", "•", "…", "‰", "‹", "›", "⁄", "€", "™", "Ω", "∂", "∆", "∏", "∑", "√", "∞", "∫", "≈", "≠", "≤", "≥", "◊", "", "ﬁ", "ﬂ");

    public TextField(String text, boolean isBold, Color color, boolean isPassword, int lines, boolean center, TextFieldContainer parent) {
        this.text = text;
        this.font = isBold ? ResourceManager.getInstance().getVolterBold() : ResourceManager.getInstance().getVolter();
        this.color = color;
        this.isPassword = isPassword;
        this.lines = lines;
        this.center = center;
        this.parent = parent;

        if (this.center) {
            this.setAlignment(Pos.CENTER);
            var _this = this;

            var listener = new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    _this.moveCaret(0);
                    _this.widthProperty().removeListener(this);
                }
            };

            this.widthProperty().addListener(listener);
        }

        this.setMinHeight(11);
        this.setText(text);
        this.setVgap(1);
        this.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            var totalLines = (this.getHeight() == 11 ? this.getHeight() : this.getHeight() + 1) / 11;

            if (totalLines > this.lines) {
                this.text = this.text.substring(0, this.text.length() - 1);
                this.renderText();
                this.moveCaret(this.caretPosition - 1);
                this.full = true;
            }
        });
    }

    private void moveCaret(int caretPosition) {
        this.caretPosition = caretPosition;

        if (this.getChildren().size() == 0 || caretPosition - 1 == -1) {
            var x = this.center ? (this.getWidth() / 2) - 3 : 0;
            this.parent.moveCaret(x, 0);
        }
        else {
            var character = this.getChildren().get(caretPosition - 1);
            var x = character.getBoundsInParent().getMaxX();
            var y = character.getBoundsInParent().getMinY();

            if (y < 0) {
                var _this = this;
                var listener = new ChangeListener<Bounds>() {
                    @Override
                    public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                        var x = newValue.getMaxX();
                        var y = newValue.getMinY();

                        if (y < 0) {
                            return;
                        }
                        
                        _this.outputCaret(caretPosition, ((Text)character).getText(), x, y);
                        _this.parent.moveCaret(x, y);
                        character.boundsInParentProperty().removeListener(this);
                    }
                };

                character.boundsInParentProperty().addListener(listener);
            }
            else {
                this.outputCaret(caretPosition, ((Text)character).getText(), x, y);
                this.parent.moveCaret(x, y);
            }
        }
    }

    private void outputCaret(int position, String character, double x, double y) {
        System.out.println("caretPosition = " + caretPosition);
        System.out.println("character = " + character);
        System.out.println("x = " + x);
        System.out.println("y = " + y);
    }

    public void setWidth(int width) {
        this.setMaxWidth(width);
    }

    public void setText(String text) {
        this.text = text;
        this.full = false;
        this.renderText();
        this.moveCaret(0);
    }

    private void renderText() {
        if (this.text != null) {
            var texts = new ArrayList<Text>();

            for (var c : this.text.toCharArray()) {
                var t = new Text(this.isPassword ? "*" : String.valueOf(c));
                t.setFont(this.font);
                t.setFill(this.color);
                texts.add(t);
            }

            this.getChildren().setAll(texts);
        }
        else {
            this.getChildren().clear();
        }
    }

    public String getText() {
        System.out.println("this.text = " + this.text);
        return this.text;
    }

    public void sendKeyPressed(KeyEvent e) {
        switch(e.getCode()) {
            case BACK_SPACE:
                if (this.caretPosition != 0) {
                    this.text = this.text.substring(0, this.caretPosition - 1) + this.text.substring(this.caretPosition);
                    this.renderText();
                    this.moveCaret(this.caretPosition - 1);
                    this.full = false;
                }
                break;
            case DELETE:
                if (this.caretPosition != this.text.length()) {
                    this.text = this.text.substring(0, this.caretPosition) + this.text.substring(this.caretPosition + 1);
                    this.renderText();
                    this.full = false;
                }
                break;
            case LEFT:
                if (this.caretPosition != 0) {
                    this.moveCaret(this.caretPosition - 1);
                }
                break;
            case RIGHT:
                if (this.caretPosition < this.text.length()) {
                    this.moveCaret(this.caretPosition + 1);
                }
                break;
            case HOME:
                this.moveCaret(0);
                break;
            case END:
                this.moveCaret(this.text.length());
                break;
            default:
                break;
        }
    }

    public void sendKeyTyped(KeyEvent e) {
        if (volter.contains(e.getCharacter()) && !this.full) {
            this.text = this.text.substring(0, this.caretPosition) + e.getCharacter() + this.text.substring(this.caretPosition);
            this.renderText();
            this.moveCaret(this.caretPosition + 1);
        }
    }
}
