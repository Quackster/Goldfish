package org.alexdev.krishna.game.values.types;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.resources.ResourceManager;
import javafx.scene.paint.Color;
import org.alexdev.krishna.game.values.Values;
import org.alexdev.krishna.util.libraries.AsyncClientAction;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PropertiesManager extends Values {
    private static PropertiesManager instance;

    public PropertiesManager() {

    }

    public void loadConfig() throws IOException {
        this.setFinished(false);
        this.properties = new HashMap<>();

        System.out.println("Loading system configuration...");

        InputStream resource = ResourceManager.getInstance().getResource("loader.config").openStream();
        readLines(resource);
        resource.close();

        System.out.println(properties.size() + " configuration keys loaded");
    }

    public void loadVariables() throws IOException {
        System.out.println("Loading external variables...");

        InputStream resource = new URL(this.properties.get("external.variables")).openStream();
        readLines(resource);
        resource.close();

        System.out.println(properties.size() + " configuration keys loaded");
    }

    private void readLines(InputStream resource) {
        List<String> doc = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.toList());

        for (var line : doc) {
            String key = line.substring(0, line.indexOf("="));
            String value = line.substring(line.indexOf("=") + 1);
            this.properties.put(key, value);
        }
    }

    public static PropertiesManager getInstance() {
        if (instance == null) {
            instance = new PropertiesManager();
        }

        return instance;
    }
}
