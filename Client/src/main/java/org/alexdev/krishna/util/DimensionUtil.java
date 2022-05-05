package org.alexdev.krishna.util;

import javafx.geometry.Point2D;
import org.alexdev.krishna.Krishna;

public class DimensionUtil {
    public static Point2D getCenterCords(double width, double height) {
        return getCenterCords((int)width, (int)height);
    }

    public static Point2D getCenterCords(int width, int height) {
        return new Point2D(
                (Krishna.getClient().getPrimaryStage().getWidth() / 2) - (width / 2),
                (Krishna.getClient().getPrimaryStage().getHeight() / 2) - (height / 2));
    }
}
