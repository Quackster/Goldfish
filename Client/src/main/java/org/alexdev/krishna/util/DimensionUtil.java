package org.alexdev.krishna.util;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import org.alexdev.krishna.Movie;

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

    public static double getRight(ImageView imageView) {
        return imageView.getX() + imageView.getImage().getWidth();
    }

    public static double getLeft(ImageView imageView) {
        return imageView.getX();
    }

    public static double getProgramWidth() {
        return Movie.getInstance().getPrimaryStage().getScene().getWidth();
    }

    public static double getProgramHeight() {
        return Movie.getInstance().getPrimaryStage().getScene().getHeight();
    }

    public static long roundEven(double d) {
        return (Math.round(d / 2) * 2);
    }
}
