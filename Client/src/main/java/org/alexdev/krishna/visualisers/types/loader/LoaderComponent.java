package org.alexdev.krishna.visualisers.types.loader;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.game.values.types.PropertiesManager;
import org.alexdev.krishna.util.DateUtil;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.util.libraries.ClientLoadStep;
import org.alexdev.krishna.visualisers.Component;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.visualisers.VisualiserType;

import java.util.HashMap;
import java.util.Map;

public class LoaderComponent implements Component {

    public static boolean loadClientConfig() {
        return false;
    }

    public static boolean loadExternalVariables() {
        return false;
    }
}