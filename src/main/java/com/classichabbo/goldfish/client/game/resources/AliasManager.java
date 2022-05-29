package com.classichabbo.goldfish.client.game.resources;

import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AliasManager {
    private HashMap<String, String> aliases;

    public AliasManager() {
        this.aliases = new HashMap<>();
    }

    public void loadMemberAlias(String path) {
        try {
            this.aliases.putAll(PropertiesManager.readLines(getResource(path).openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private URL getResource(String url) {
        return getClass().getResource("/" + url);
    }

    public Map<String, String> getAliases() {
        return aliases;
    }
}
