package com.classichabbo.goldfish.client.controls;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends ImageView {
    public ImageButton(Image image) {
        this.setImage(image);
        //this.setPrefSize(image.getWidth(), image.getHeight());
        //this.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.setCursor(Cursor.HAND);
        this.setPickOnBounds(false);
    }
}
