package org.alexdev.krishna.util;

import java.awt.*;

public class StaticSettings {
    public static int WIDTH;
    public static int HEIGHT;

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        WIDTH = (int) DimensionUtil.roundEven(width * 0.5);
        HEIGHT = (int) DimensionUtil.roundEven(height * 0.5);
    }
}
