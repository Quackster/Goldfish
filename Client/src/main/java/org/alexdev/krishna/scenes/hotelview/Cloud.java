package org.alexdev.krishna.scenes.hotelview;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.alexdev.krishna.util.DimensionUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Cloud extends Pane {
    private int turnPoint;
    private String fileName;
    private Map<String, ImageView> images;
    private boolean isFinished;
    private boolean isTurning;

    public Cloud(int turnPoint, String fileName) {
        this.turnPoint = turnPoint;
        this.fileName = fileName;
        this.images = new HashMap<>();
        this.isFinished = false;
        this.isTurning = false;
    }

    public void init() {
        var cloud = new ImageView();

        cloud.setImage(new Image(new File("resources/scenes/hotel_view/clouds/" + fileName + ".png").toURI().toString()));
        cloud.setX(280);
        cloud.setY(280);
        cloud.setViewOrder(HotelViewManager.CLOUD_Z_INDEX);

        this.images.put("left", cloud);
        this.getChildren().add(cloud);
    }

    public void update() {
        var left = images.get("left");
        var right = images.get("right");

        if (DimensionUtil.getLeft(left) >= turnPoint) {
            this.turn();
        }

        System.out.println("IsTurning: " + isTurning);

        int pVertDir = -1; // -1 for left, 1 for right

        if (left.isVisible()) {
            if (isTurning)
                pVertDir = 0;

            
            left.setX(left.getX() + 1);

            if (left.getX() % 2 == 0) {
                left.setY(left.getY() + pVertDir);
            }
        }

        // Modify left cloud... if exists
        if (right != null) {
            if (isTurning)
                pVertDir = 0;

            if (pVertDir == -1) {
                pVertDir = 1;
            }

            right.setX(right.getX() + 1);

            if (right.getX() % 2 == 0) {
                right.setY(right.getY() + pVertDir);
            }
        }
    }

    private void turn() {
        var left = images.get("left");
        var right = images.get("right");

        //System.out.println("left: " + left);
        //System.out.println("right: " + right);

        var newLeftWidth = left.getImage().getWidth() - (DimensionUtil.getLeft(left) - turnPoint);

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
            right.setX(DimensionUtil.getRight(left) + newLeftWidth);
            right.setY(left.getY());

            this.images.put("right", right);
            this.getChildren().add(right);
        }

        var tWidth = DimensionUtil.getRight(left) - turnPoint;
        var tHeigth = tWidth / 2 + 1;

        Rectangle2D rect2 = new Rectangle2D(newLeftWidth, 0, right.getImage().getWidth(), right.getImage().getHeight());
        right.setViewport(rect2);

        if (newLeftWidth != left.getImage().getWidth() && newLeftWidth > 0) {
            isTurning = true;
        } else {
            if (isTurning) {
                this.isFinished = true;
            }
            isTurning = false;
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
