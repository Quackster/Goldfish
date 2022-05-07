package org.alexdev.krishna.util;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.stage.Window;
import org.alexdev.krishna.Krishna;

// LocV = Y
// LocH = X
public class DimensionUtil {
    public static Point2D getCenterCords(double width, double height) {
        return getCenterCords((int)width, (int)height);
    }

    public static Point2D getCenterCords(int width, int height) {
        return new Point2D(
                roundEven((getProgramWidth() / 2) - (width / 2)),
                roundEven(getProgramHeight() / 2) - (height / 2));
    }

    public static double getTopRight(ImageView imageView) {
        return imageView.getX() + imageView.getImage().getWidth();
    }

    public static double getTopLeft(ImageView imageView) {
        return imageView.getX();
    }

    public static double getBottomRight(ImageView imageView) {
        return imageView.getY() + imageView.getImage().getHeight();
    }

    public static double getBottomLeft(ImageView imageView) {
        return imageView.getY();
    }

    public static double getProgramWidth() {
        return Krishna.getClient().getPrimaryStage().getScene().getWidth();
    }

    public static double getProgramHeight() {
        return Krishna.getClient().getPrimaryStage().getScene().getHeight();
    }

    public static long roundEven(double d) {
        return (Math.round(d / 2) * 2);
    }
}
