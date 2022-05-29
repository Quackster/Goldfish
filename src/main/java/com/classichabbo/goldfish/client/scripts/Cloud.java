package com.classichabbo.goldfish.client.scripts;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import com.classichabbo.goldfish.util.DimensionUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cloud extends Pane {
    private int pTurnPoint;
    private String fileName;
    private String direction;
    private boolean isFinished;

    private ImageView cloud;
    private BufferedImage leftImage;
    private BufferedImage rightImage;

    private int pVertDir;
    private int pCloudDir;
    private int initX;
    private int initY;

    public Cloud(int turnPoint, String fileName, String direction, int initX, int initY) {
        this.pTurnPoint = turnPoint;
        this.fileName = fileName;
        this.direction = direction;
        this.isFinished = false;
        this.initX = initX;
        this.initY = initY;
        this.cloud = new ImageView();
        //this.pCloudDir = -1;

        if (direction.equals("left"))
            this.pVertDir = -1;
        else
            this.pVertDir = 1;

        this.cloud.setImage(ResourceManager.getInstance().getFxImage("sprites/views/hotel_view/clouds", this.getCloudPath()));

        // If the X is at the start, subtract its width so it slowly slides instead of just suddenly appearing
        if (this.initX <= 0)
            this.cloud.setX(this.initX - this.cloud.getImage().getWidth());
        else
            this.cloud.setX(this.initX);

        this.cloud.setY(this.initY);

        this.loadClouds();
        this.getChildren().add(this.cloud);
    }

    private void loadClouds() {
        this.leftImage = null;
        this.rightImage = null;

        try {
            this.rightImage = ResourceManager.getInstance().getAwtImage("sprites/views/hotel_view/clouds", this.getFlippedCloudPath()); // eventually C:\\ImageTest\\pic2.jpg
            this.leftImage = ResourceManager.getInstance().getAwtImage("sprites/views/hotel_view/clouds", this.getCloudPath()); // eventually C:\\ImageTest\\pic2.jpg
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (DimensionUtil.getRight(this.cloud) > this.pTurnPoint &&
            DimensionUtil.getLeft(this.cloud) <= this.pTurnPoint) {
            this.turn();
        }

        if (DimensionUtil.getLeft(this.cloud) == this.pTurnPoint) {
            this.pVertDir = this.pCloudDir * -1;
        }

        this.cloud.setX(this.cloud.getX() + 1); // locH

        if (this.cloud.getX() % 2 == 0) {
            this.cloud.setY(this.cloud.getY() + this.pVertDir);
        }

        // Cloud went off-screen, dispose
        if (DimensionUtil.getLeft(this.cloud) > DimensionUtil.getProgramWidth()) {
            this.isFinished = true;
        }
    }

    private void turn() {
        if (this.pVertDir != 0) {
            this.pCloudDir = this.pVertDir; // Save previous direction
        }

        // No Y direction when turning
        this.pVertDir = 0;

        int leftWidth = (int) (this.cloud.getImage().getWidth() - (DimensionUtil.getRight(this.cloud) - this.pTurnPoint));
        int rightWidth = (int) this.rightImage.getWidth() - leftWidth;

        if (leftWidth > 0) {
            var croppedLeft = this.leftImage.getSubimage(0, 0, leftWidth, (int) this.leftImage.getHeight());
            var croppedRight = this.rightImage.getSubimage(leftWidth, 0, rightWidth, (int) this.rightImage.getHeight());

            int width = croppedLeft.getWidth() + croppedRight.getWidth();
            int height = Math.max(croppedLeft.getHeight(), croppedRight.getHeight());

            var y = ((croppedLeft.getWidth() / 2) - (croppedLeft.getHeight() / 2)) + (width / 2);
            var y2 = ((croppedRight.getWidth() / 2) - (croppedRight.getHeight() / 2)) + (width / 2);
            //System.out.println("y " + y + " y2 " + y2 + " width " + width + " height " + height);
            this.cloud.setImage(SwingFXUtils.toFXImage(joinBufferedImage(croppedLeft, croppedRight, width, height * 2, y, y2), null));

            if (this.getTranslateY() == 0) {
                this.setTranslateY(-y);
            }
            
        } else {
            this.cloud.setImage(SwingFXUtils.toFXImage(this.rightImage, null));
            this.setTranslateY(-1);
        }
    }

    public static BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2, int width, int height, int y, int y2) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();

        //clear
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2.fillRect(0, 0, width, height);

        //reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, y);
        g2.drawImage(img2, null, img1.getWidth(), y2);
        g2.dispose();

        return newImage;
    }

    private String getCloudPath() {
        return this.fileName + "_" + this.direction + ".png";
    }

    private String getFlippedCloudPath() {
        String oppositeDirection = this.direction;

        if (oppositeDirection.equals("left")) {
            oppositeDirection = "right";
        }

        return this.fileName + "_" + oppositeDirection + ".png";
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
