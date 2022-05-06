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
    //private Map<String, ImageView> images;
    private boolean isFinished;
    private boolean isTurning;
    private ImageView cloud;
    private int pVertDir;
    private int pCloudDir;

    public Cloud(int turnPoint, String fileName) {
        this.pTurnPoint = turnPoint;
        this.fileName = fileName;
        //this.images = new HashMap<>();
        this.isFinished = false;
        this.isTurning = false;
    }

    public void init() {
        this.cloud = new ImageView();
        this.pCloudDir = -1;

        this.cloud.setImage(new Image(new File("resources/scenes/hotel_view/clouds/" + fileName + ".png").toURI().toString()));
        this.cloud.setX(280);
        this.cloud.setY(280);
        this.getChildren().add(this.cloud);
    }

    public void update() {
        if (DimensionUtil.getRight(this.cloud) > this.pTurnPoint &&
            DimensionUtil.getLeft(this.cloud) <= this.pTurnPoint) {
            this.turn();
            this.pVertDir = 0;
            this.isTurning = true;
        } else {
            if (this.isTurning) {
                // this.isFinished = true; // dispose after fully turning, used for development purposes
            }
            this.isTurning = false;
        }

        if (DimensionUtil.getLeft(this.cloud) == this.pTurnPoint) {
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

        var tWidth = DimensionUtil.getRight(this.cloud) - pTurnPoint;
        var tHeigth = (-tWidth / 2) - 1;

        //System.out.println(tWidth + " // " + tHeigth);

        BufferedImage leftImage = null;//SwingFXUtils.fromFXImage(this.cloud.getImage(), null);
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

        int newWidth = (int) (this.cloud.getImage().getWidth() - (DimensionUtil.getRight(this.cloud) - this.pTurnPoint));

        if (newWidth > 0) {
            var croppedLeft = leftImage.getSubimage(0, 0, newWidth, (int) leftImage.getHeight());
            var croppedRight = leftImage.getSubimage(newWidth, 0, (int) leftImage.getWidth() - newWidth, (int) leftImage.getHeight());

            //rightImage.getSubimage(newWidth, 0, (int) rightImage.getWidth() - newWidth, (int) rightImage.getHeight());

            //BufferedImage img = new BufferedImage((int) rightImage.getWidth(), (int) rightImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            /*
            try {
                ImageIO.write(croppedLeft, "png", new File("left_cloud.png"));
                ImageIO.write(croppedRight, "png", new File("right_cloud.png"));
                ImageIO.write(joinBufferedImage(croppedLeft, croppedRight), "png", new File("joined_cloud.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            this.cloud.setImage(SwingFXUtils.toFXImage(joinBufferedImage(croppedLeft, croppedRight), null));
        } else {
            this.cloud.setImage(SwingFXUtils.toFXImage(rightImage, null));
        }

        //        this.cloud.setImage(SwingFXUtils.toFXImage(newImage, null));

        //image.getGraphics()
    }

    public static BufferedImage horizontalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getType());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return flippedImage;
    }


    public static BufferedImage joinBufferedImage(BufferedImage img1,
                                                  BufferedImage img2) {
        //int offset = 2;
        int width = img1.getWidth() + img2.getWidth();
        int height = Math.max(img1.getHeight(), img2.getHeight());

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();

        //clear
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2.fillRect(0, 0, width, height);

        //reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth(), 0);
        g2.dispose();
        return newImage;
    }

    /*
    public void update() {
        var left = images.get("left");
        var right = images.get("right");

        if (DimensionUtil.getLeft(left) >= turnPoint) {
            this.turn();
        }

        int pVertDir = -1; // -1 for left, 1 for right

        if (left != null) {
            if (left.isVisible()) {
                left.setX(left.getX() + 1);

                if (left.getX() % 2 == 0) {
                    //left.setY(left.getY() - 1);
                }
            }
        }


        if (right != null) {
            if (right.isVisible()) {

                if (right.getY() % 2 == 0) {
                }
            }
        }
    }

    private void turn() {
        var left = images.get("left");
        var right = images.get("right");

        //System.out.println("left: " + left);
        //System.out.println("right: " + right);

        var newLeftWidth = left.getImage().getWidth() - (DimensionUtil.getLeft(left) - this.turnPoint);
        System.out.println(newLeftWidth);

        if (newLeftWidth <= 0) {
            if (left.isVisible()) {
                left.setVisible(false);
                //this.isFinished = true;
            }
        } else {
            Rectangle2D rect = new Rectangle2D(0, 0, newLeftWidth, left.getImage().getHeight());
            left.setViewport(rect);
        }

        if (right == null) {
            var rightFileName = this.fileName.split("_")[0] + "_" + this.fileName.split("_")[1] + "_right";

            right = new ImageView();
            right.setImage(new Image(new File("resources/scenes/hotel_view/clouds/" + rightFileName + ".png").toURI().toString()));
            right.setViewOrder(HotelViewManager.CLOUD_Z_INDEX);
            right.setY(left.getY());

            this.images.put("right", right);
            this.getChildren().add(right);
        }

        if (this.isTurning) {
            right.setX(turnPoint +1);
            //right.setX(DimensionUtil.getRight(left) + newLeftWidth + 1);
            //right.setY((left.getY() - DimensionUtil.roundEven(right.getImage().getHeight() / 2))-newLeftWidth);
        }

        Rectangle2D rect2 = new Rectangle2D(newLeftWidth, 0, right.getImage().getWidth(), right.getImage().getHeight());
        right.setViewport(rect2);

        if (newLeftWidth != left.getImage().getWidth() && newLeftWidth > 0) {
            this.isTurning = true;
        } else {
            if (this.isTurning) {
                this.isFinished = true;
            }

            this.isTurning = false;
        }
    }*/

    public boolean isFinished() {
        return isFinished;
    }
}
