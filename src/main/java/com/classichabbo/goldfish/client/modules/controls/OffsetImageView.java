package com.classichabbo.goldfish.client.modules.controls;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.BufferedInputStream;

public class OffsetImageView extends Pane {
    public OffsetImageView(String directory, String img, int width, int height, boolean flipped) {
        try {
            setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage(directory, img + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            setPrefSize(width, height);

            if (flipped)
                setScaleX(-1);

            String contentsString = new String(ResourceManager.getInstance().getResource(directory + "/" + img + "_offset.txt").openStream().readAllBytes());
            String[] offsets = contentsString.split(",");

            if (flipped) {
                setTranslateX(-Integer.parseInt(offsets[0]));
                setTranslateY(-Integer.parseInt(offsets[1]));
            } else {
                setTranslateX(-Integer.parseInt(offsets[0]));
                setTranslateY(-Integer.parseInt(offsets[1]));
            }
        } catch (Exception e) {
            System.out.println(img);
            e.printStackTrace();
        }
    }

    public OffsetImageView(String directory, String img, int width, int height, boolean flipped, Color multiplyColor) {
        try {
            setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage(directory, img + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            setPrefSize(width, height);

            if (flipped)
                setScaleX(-1);

            String contentsString = new String(ResourceManager.getInstance().getResource(directory + "/" +  "_offset.txt").openStream().readAllBytes());
            String[] offsets = contentsString.split(",");

            if (flipped) {
                setTranslateX(Integer.parseInt(offsets[0]) - 34);
                setTranslateY(-Integer.parseInt(offsets[1]));
            } else {
                setTranslateX(-Integer.parseInt(offsets[0]));
                setTranslateY(-Integer.parseInt(offsets[1]));
            }

            Blend multiply = new Blend(BlendMode.MULTIPLY);

            ColorInput ci = new ColorInput();
            ci.setPaint(multiplyColor);
            ci.setWidth(100);
            ci.setHeight(100);

            multiply.setTopInput(ci);

            Blend atop = new Blend(BlendMode.SRC_ATOP);
            atop.setTopInput(multiply);

            setEffect(atop);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println(img);
        }
    }
}