package org.alexdev.krishna.game.values.types;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.game.resources.ResourceManager;
import javafx.scene.paint.Color;
import org.alexdev.krishna.game.values.Values;
import org.alexdev.krishna.util.libraries.AsyncClientAction;

import java.io.*;
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

    private void loadConfig() {
        this.setFinished(false);
        this.properties = new HashMap<>();

        System.out.println("Loading system configuration...");

        try (InputStream resource = ResourceManager.getInstance().getResource("loader.config").openStream()) {
            readLines(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(properties.size() + " configuration keys loaded");
    }

    private void loadVariables() {
        System.out.println("Loading external variables...");

        try (InputStream resource = new URL(this.properties.get("external.variables")).openStream()) {
            readLines(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public static void init() {
        if (instance == null) {
            instance = new PropertiesManager();
        }

        HabboClient.getInstance().getScheduler().schedule(() -> {
            instance.loadConfig();
            instance.loadVariables();
            instance.setFinished(true);
        }, 0, TimeUnit.SECONDS);
    }

    public static PropertiesManager getInstance() {
        return instance;
    }
}
