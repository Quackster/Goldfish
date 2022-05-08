package org.alexdev.krishna.scenes.hotelview;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.alexdev.krishna.util.DimensionUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cloud extends Pane {
    private int pTurnPoint;
    private String fileName;
    private boolean isFinished;
    private boolean isTurning;
    private ImageView cloud;
    private int pVertDir;
    private int pCloudDir;
    private int initX;
    private int initY;

    public Cloud(int turnPoint, String fileName, int initX, int initY) {
        this.pTurnPoint = turnPoint;
        this.fileName = fileName;
        this.isFinished = false;
        this.isTurning = false;
        this.initX = initX;
        this.initY = initY;
    }

    public void init() {
        this.cloud = new ImageView();
        this.pCloudDir = -1;
        this.pVertDir = -1;

        this.cloud.setImage(new Image(new File("resources/scenes/hotel_view/clouds/" + fileName + ".png").toURI().toString()));
        this.cloud.setX(this.initX);
        this.cloud.setY(this.initY);
        this.getChildren().add(this.cloud);
    }

    public void update() {
        // loop around the corner, dev only
        if (DimensionUtil.getTopLeft(this.cloud) == 350) {
            this.isFinished = true;
        }

        if (DimensionUtil.getTopRight(this.cloud) > this.pTurnPoint &&
            DimensionUtil.getTopLeft(this.cloud) <= this.pTurnPoint) {
            this.turn();
            this.isTurning = true;
        } else {
            if (this.isTurning) {
                //this.isFinished = true; // dispose after fully turning, used for development purposes
            }
            this.isTurning = false;
        }

        if (DimensionUtil.getTopLeft(this.cloud) == this.pTurnPoint) {
            this.pVertDir = this.pCloudDir * -1;
        }

        this.cloud.setX(this.cloud.getX() + 1); // locH

        if (this.cloud.getX() % 2 == 0) {
            this.cloud.setY(this.cloud.getY() + this.pVertDir);
        }
    }

    private void turn() {
        if (this.pVertDir != 0) {
            pCloudDir = pVertDir;
        }

        pVertDir = 0;

        BufferedImage leftImage = null;
        BufferedImage rightImage = null;
        try
        {
            String direction = null;

            if (this.fileName.split("_")[2].equals("left")) {
                direction = "right";
            } else {
                direction = "left";
            }

            var fileName = this.fileName.split("_")[0] + "_" + this.fileName.split("_")[1] + "_" + direction;
            rightImage = ImageIO.read(new File("resources/scenes/hotel_view/clouds/" + fileName + ".png")); // eventually C:\\ImageTest\\pic2.jpg
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            leftImage = ImageIO.read(new File("resources/scenes/hotel_view/clouds/" + this.fileName + ".png")); // eventually C:\\ImageTest\\pic2.jpg
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        int leftWidth = (int) (this.cloud.getImage().getWidth() - (DimensionUtil.getTopRight(this.cloud) - this.pTurnPoint));
        int rightWidth = (int) rightImage.getWidth() - leftWidth;

        if (leftWidth > 0) {
            var croppedLeft = leftImage.getSubimage(0, 0, leftWidth, (int) leftImage.getHeight());
            var croppedRight = rightImage.getSubimage(leftWidth, 0, rightWidth, (int) rightImage.getHeight());

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
            this.cloud.setImage(SwingFXUtils.toFXImage(rightImage, null));
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

    public boolean isFinished() {
        return isFinished;
    }
}
