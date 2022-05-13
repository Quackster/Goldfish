package org.alexdev.krishna.game.resources;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {
    private static ResourceManager instance;
    public Map<String, Image> fxImages;
    public Map<String, Image> webImages;
    public Map<String, BufferedImage> awtImages;

    public ResourceManager() {
        this.fxImages = new HashMap<>();
        this.awtImages = new HashMap<>();
        this.webImages = new HashMap<>();
    }

    public Image getWebImage(String httpUrl) {
        if (this.webImages.containsKey(httpUrl)) {
            return this.webImages.get(httpUrl);
        } else {
            var fxImage = new Image(httpUrl, true);
            this.webImages.put(httpUrl, fxImage);
            return fxImage;
        }
    }

    public Image getFxImage(String url) {
        if (this.fxImages.containsKey(url)) {
            return this.fxImages.get(url);
        } else {
            var fxImage = SwingFXUtils.toFXImage(getAwtImage(url), null);
            this.fxImages.put(url, fxImage);
            return fxImage;
        }
    }

    public BufferedImage getAwtImage(String url) {
        try {
            if (this.awtImages.containsKey(url)) {
                return this.awtImages.get(url);
            } else {
                var bufferedImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/" + url)));

                if (bufferedImage != null) {
                    this.awtImages.put(url, bufferedImage);
                    return bufferedImage;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage copyAwtImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
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
