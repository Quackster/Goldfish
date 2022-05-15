package org.alexdev.krishna.controls;

import org.alexdev.krishna.game.resources.ResourceManager;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class ImageButton extends ImageView {
    public ImageButton(Image image) {
        this.setImage(image);
        //this.setPrefSize(image.getWidth(), image.getHeight());
        //this.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.setCursor(Cursor.HAND);
        this.setPickOnBounds(false);
    }
}
