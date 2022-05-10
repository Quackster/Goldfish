package org.alexdev.krishna.game.resources;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ResourceManager {
    private static ResourceManager instance;

    public Image getFxImage(String url) {
        return SwingFXUtils.toFXImage(getAwtImage(url), null);
    }

    public BufferedImage getAwtImage(String url) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResource("/" + url)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public URL getResource(String url) {
        return getClass().getResource("/" + url);
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }

        return instance;
    }
}
