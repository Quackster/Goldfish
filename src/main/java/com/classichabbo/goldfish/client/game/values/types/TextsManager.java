package com.classichabbo.goldfish.client.game.values.types;

import com.classichabbo.goldfish.client.game.values.Values;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TextsManager extends Values {
    private static TextsManager instance;

    public void loadTexts() throws IOException {
        this.values = new HashMap<>();
        this.setFinished(false);

        if (PropertiesManager.getInstance().getValues() == null ||
            PropertiesManager.getInstance().getValues().isEmpty()) {
            return;
        }

        //System.out.println("Loading external texts...");

        InputStream resource = new URL(PropertiesManager.getInstance().getString("external.texts")).openStream();
        readLines(resource);
        resource.close();

        System.out.println(this.values.size() + " texts keys loaded");
        this.setFinished(true);
    }

    private void readLines(InputStream resource) {
        List<String> doc = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.toList());

        for (var line : doc) {
            if (line.contains("=")) {
                String key = line.substring(0, line.indexOf("="));
                String value = line.substring(line.indexOf("=") + 1);
                this.values.put(key, value);
            }
        }
    }

    public static TextsManager getInstance() {
        if (instance == null) {
            instance = new TextsManager();
        }

        return instance;
    }
}
