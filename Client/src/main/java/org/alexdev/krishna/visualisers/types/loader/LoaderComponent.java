package org.alexdev.krishna.visualisers.types.loader;

import org.alexdev.krishna.game.values.types.PropertiesManager;
import org.alexdev.krishna.game.values.types.TextsManager;
import org.alexdev.krishna.visualisers.Component;

import java.util.concurrent.Future;

public class LoaderComponent implements Component {
    private Future<Boolean> clientConfigTask;
    private Future<Boolean> externalTextsTask;
    private Future<Boolean> externalVariablesTask;

    public LoaderComponent() {

    }

    @Override
    public void init() {
        this.clientConfigTask = null;
        this.externalTextsTask = null;
        this.externalVariablesTask = null;
    }

    public boolean loadClientConfig() {
        try {
            PropertiesManager.getInstance().loadConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return PropertiesManager.getInstance().isFinished();
    }

    public boolean loadExternalVariables() {
        try {
            PropertiesManager.getInstance().loadVariables();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return PropertiesManager.getInstance().isFinished();
    }

    public boolean loadExternalTexts() {
        try {
            TextsManager.getInstance().loadTexts();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return TextsManager.getInstance().isFinished();
    }

    public Future<Boolean> getClientConfigTask() {
        return clientConfigTask;
    }

    public void setClientConfigTask(Future<Boolean> clientConfigTask) {
        this.clientConfigTask = clientConfigTask;
    }

    public Future<Boolean> getExternalVariablesTask() {
        return externalVariablesTask;
    }

    public void setExternalVariablesTask(Future<Boolean> externalVariablesTask) {
        this.externalVariablesTask = externalVariablesTask;
    }

    public Future<Boolean> getExternalTextsTask() {
        return externalTextsTask;
    }

    public void setExternalTextsTask(Future<Boolean> externalTextsTask) {
        this.externalTextsTask = externalTextsTask;
    }
}