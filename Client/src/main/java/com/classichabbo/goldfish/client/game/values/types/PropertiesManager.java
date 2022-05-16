package com.classichabbo.goldfish.client.game.values.types;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.Values;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PropertiesManager extends Values {
    private static PropertiesManager instance;

    public void loadConfig() throws IOException {
        this.setFinished(false);
        this.values = new HashMap<>();

        //System.out.println("Loading system configuration...");

        InputStream resource = ResourceManager.getInstance().getResource("loader.config").openStream();
        readLines(resource);
        resource.close();

        System.out.println(this.values);

        System.out.println(values.size() + " configuration keys loaded");
        this.setFinished(true);
    }

    public void loadVariables() throws IOException {
        this.setFinished(false);

        if (this.values == null || this.values.isEmpty()) {
            return;
        }

        //System.out.println("Loading external variables...");

        InputStream resource = new URL(this.values.get("external.variables")).openStream();
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

    public static PropertiesManager getInstance() {
        if (instance == null) {
            instance = new PropertiesManager();
        }

        return instance;
    }
}
