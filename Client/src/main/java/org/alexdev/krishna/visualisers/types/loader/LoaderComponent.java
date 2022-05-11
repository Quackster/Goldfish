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
import org.alexdev.krishna.game.scheduler.SchedulerManager;
import org.alexdev.krishna.game.values.types.PropertiesManager;
import org.alexdev.krishna.util.DateUtil;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.util.libraries.ClientLoadStep;
import org.alexdev.krishna.visualisers.Component;
import org.alexdev.krishna.visualisers.Visualiser;
import org.alexdev.krishna.visualisers.VisualiserType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class LoaderComponent implements Component {
    private Future<Boolean> clientConfigTask;
    private Future<Boolean> externalTextsTask;

    public LoaderComponent() {

    }

    public boolean loadClientConfig() {
        try {
            PropertiesManager.getInstance().loadConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean loadExternalVariables() {
        try {
            PropertiesManager.getInstance().loadVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public Future<Boolean> getClientConfigTask() {
        return clientConfigTask;
    }

    public void setClientConfigTask(Future<Boolean> clientConfigTask) {
        this.clientConfigTask = clientConfigTask;
    }

    public Future<Boolean> getExternalTextsTask() {
        return externalTextsTask;
    }

    public void setExternalTextsTask(Future<Boolean> externalTextsTask) {
        this.externalTextsTask = externalTextsTask;
    }
}