package com.classichabbo.goldfish.client.modules.controls;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends ImageView {
    public ImageButton(Image image) {
        this.setImage(image);
        this.setCursor(Cursor.HAND);
        this.setPickOnBounds(false);
    }
}
