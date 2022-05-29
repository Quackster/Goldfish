package com.classichabbo.goldfish.client.game.resources;

import javax.imageio.ImageIO;

import com.classichabbo.goldfish.client.game.scheduler.SchedulerManager;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ResourceManager {
    private static ResourceManager instance;

    public Font volter;
    public Font volterBold;
    public Font volterLarge;
    public Font volterBoldLarge;

    public Map<String, Image> fxImages;
    public Map<String, BufferedImage> webImages;
    public Map<String, BufferedImage> awtImages;

    public ResourceManager() {
        this.fxImages = new HashMap<>();
        this.awtImages = new HashMap<>();
        this.webImages = new HashMap<>();
    }

    public void loadFonts() {
        try {
            this.volter = javafx.scene.text.Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter.woff").openStream(), 9);
            this.volterBold = javafx.scene.text.Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter_bold.woff").openStream(), 9);
            this.volterLarge = javafx.scene.text.Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter.woff").openStream(), 18);
            this.volterBoldLarge = Font.loadFont(ResourceManager.getInstance().getResource("sprites/volter/volter_bold.woff").openStream(), 18);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWebImage(String httpUrl, Consumer<Image> onCompletion) {
        if (this.webImages.containsKey(httpUrl)) {
            onCompletion.accept(SwingFXUtils.toFXImage(this.webImages.get(httpUrl), null));
        } else {
            SchedulerManager.getInstance().asyncCallback(() -> {
                try {
                    var image = ImageIO.read(new URL(httpUrl));
                    this.webImages.put(httpUrl, image);

                    Platform.runLater(() -> {
                        onCompletion.accept(SwingFXUtils.toFXImage(image, null));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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

    public Image getFxImage(String url, String backupUrl) {
        try {
            return getFxImage(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            return getFxImage(backupUrl);
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
            System.out.println("Could not locate: " + url);
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

    public Font getVolter() {
        return volter;
    }

    public Font getVolterBold() {
        return volterBold;
    }

    public Font getVolterLarge() {
        return volterLarge;
    }

    public Font getVolterBoldLarge() {
        return volterBoldLarge;
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }

        return instance;
    }
}
