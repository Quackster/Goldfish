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
    private ImageView image;
    private int pVertDir;
    private int pCloudDir;
    private int yChange;

    public Cloud(int turnPoint, String fileName) {
        this.pTurnPoint = turnPoint;
        this.fileName = fileName;
        //this.images = new HashMap<>();
        this.isFinished = false;
        this.isTurning = false;
    }

    public void init() {
        this.image = new ImageView();
        this.pCloudDir = -1;
        this.yChange = 0;

        this.image.setImage(new Image(new File("resources/scenes/hotel_view/clouds/" + fileName + ".png").toURI().toString()));
        this.image.setX(280);
        this.image.setY(DimensionUtil.roundEven(DimensionUtil.getProgramHeight() * 0.30));
        this.getChildren().add(this.image);
    }

    public void update() {
        if (DimensionUtil.getTopRight(this.image) > this.pTurnPoint &&
            DimensionUtil.getTopLeft(this.image) <= this.pTurnPoint) {
            this.turn();
            this.pVertDir = 0;
            this.isTurning = true;
        } else {
            if (this.isTurning) {
                this.isFinished = true; // dispose after fully turning, used for development purposes
            }
            this.isTurning = false;
        }

        if (DimensionUtil.getTopLeft(this.image) == this.pTurnPoint) {
            this.pVertDir = this.pCloudDir * -1;
        }

        this.image.setX(this.image.getX() + 1); // locH

        if (this.image.getX() % 2 == 0) {
            this.image.setY(this.image.getY() + this.pVertDir);
            this.yChange++;
        }
    }

    private void turn() {
        if (this.pVertDir != 0) {
            pCloudDir = pVertDir;
        }

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

        int leftWidth = (int) (this.image.getImage().getWidth() - (DimensionUtil.getTopRight(this.image) - this.pTurnPoint));
        int rightWidth = (int) rightImage.getWidth() - leftWidth;
        //System.out.println("left: " + leftWidth + " // right: " + rightWidth + " // tWidth " + tWidth);

        if (leftWidth > 0) {
            var croppedLeft = leftImage.getSubimage(0, 0, leftWidth, (int) leftImage.getHeight());
            var croppedRight = rightImage.getSubimage(leftWidth, 0, rightWidth, (int) rightImage.getHeight());

            //rightImage.getSubimage(newWidth, 0, (int) rightImage.getWidth() - newWidth, (int) rightImage.getHeight());
            //BufferedImage img = new BufferedImage((int) rightImage.getWidth(), (int) rightImage.getHeight(), BufferedImage.TYPE_INT_RGB);
              /*try {
                ImageIO.write(croppedLeft, "png", new File("left_cloud.png"));
                ImageIO.write(croppedRight, "png", new File("right_cloud.png"));

                    ImageIO.write(joinBufferedImage(croppedLeft, croppedRight, width, height), "png", new File("join_cloud.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            this.image.setImage(SwingFXUtils.toFXImage(joinBufferedImage(croppedLeft, croppedRight), null));
        } else {
            this.image.setImage(SwingFXUtils.toFXImage(rightImage, null));
        }
    }


    public static BufferedImage joinBufferedImage(BufferedImage leftImage, BufferedImage rightImage) {
        int width = leftImage.getWidth() + rightImage.getWidth();
        int height = Math.max(leftImage.getHeight(), rightImage.getHeight());

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();

        // clear
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2.fillRect(0, 0, width, height);

        // reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        g2.setColor(oldColor);
        g2.drawImage(leftImage, null, 0, 0);
        g2.drawImage(rightImage, null, leftImage.getWidth(), 0);
        g2.dispose();

        return newImage;
    }

    private static BufferedImage trimImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int top = height / 2;
        int bottom = top;
        int left = width / 2 ;
        int right = left;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (image.getRGB(x, y) != 0){
                    top    = Math.min(top, y);
                    bottom = Math.max(bottom, y);
                    left   = Math.min(left, x);
                    right  = Math.max(right, x);
                }
            }
        }
        return image.getSubimage(left, top, Math.max(right - left, 1), Math.max(bottom - top, 1));
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

    public ImageView getImage() {
        return image;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
