package com.classichabbo.goldfish.client.game.values.types;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.Values;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class VariablesManager extends Values {
    private static VariablesManager instance;

    public void loadVariables() throws IOException {
        this.setFinished(false);
        this.values = new HashMap<>();

        if (PropertiesManager.getInstance().getValues() == null ||
                PropertiesManager.getInstance().getValues().isEmpty()) {
            return;
        }

        //System.out.println("Loading external texts...");

        InputStream resource = new URL(PropertiesManager.getInstance().getString("external.variables")).openStream();
        readLines(resource);
        resource.close();

        System.out.println(values.size() + " external variables loaded");
        this.setFinished(true);
    }

    private void readLines(InputStream resource) {
        List<String> doc = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.toList());

        for (var line : doc) {
            if (line.startsWith("#"))
                continue;

            String key = line.substring(0, line.indexOf("="));
            String value = line.substring(line.indexOf("=") + 1);
            this.values.put(key, value);
        }
    }

    public static VariablesManager getInstance() {
        if (instance == null) {
            instance = new VariablesManager();
        }

        return instance;
    }
}
