package com.classichabbo.goldfish.client.game.values.types;

import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.Values;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertiesManager extends Values {
    private static PropertiesManager instance;

    public void loadConfig() throws IOException {
        this.setFinished(false);
        this.values = new HashMap<>();

        //System.out.println("Loading system configuration...");

        InputStream resource = ResourceManager.getInstance().getResource("loader.config").openStream();
        this.values = readLines(resource);
        resource.close();

        System.out.println(this.values);

        System.out.println(values.size() + " configuration keys loaded");
        this.setFinished(true);
    }

    public static Map<String, String> readLines(InputStream resource) {
        var values = new HashMap<String, String>();
        List<String> doc = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.toList());

        for (var line : doc) {
            if (line.startsWith("#") || line.length() <= 1)
                continue;

            String key = line.substring(0, line.indexOf("="));
            String value = line.substring(line.indexOf("=") + 1);
            values.put(key, value);
        }

        return values;
    }

    public static PropertiesManager getInstance() {
        if (instance == null) {
            instance = new PropertiesManager();
        }

        return instance;
    }
}
