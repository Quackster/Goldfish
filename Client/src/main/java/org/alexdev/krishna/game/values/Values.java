package org.alexdev.krishna.game.values;

import javafx.scene.paint.Color;
import org.alexdev.krishna.util.libraries.AsyncClientAction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Values implements AsyncClientAction {
    public Map<String, String> properties;
    private boolean isFinished;

    public String getString(String s) {
        return this.properties.get(s);
    }

    public int getInt(String s) {
        return Integer.parseInt(this.properties.get(s));
    }

    public Object getType(String s, ValueType type) {
        switch (type) {
            case COLOR_RGB:
                String value = this.getString(s)
                        .replace("rgb", "")
                        .replace("(", "")
                        .replace(")", "")
                        .replace(" ", "");

                return Color.rgb(
                        Integer.parseInt(value.split(",")[0]),
                        Integer.parseInt(value.split(",")[1]),
                        Integer.parseInt(value.split(",")[2])
                );
            default:
                throw new IllegalStateException("Unexpected type: " + type);
        }
    }

    private void readLines(InputStream resource) {
        List<String> doc = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.toList());

        for (var line : doc) {
            String key = line.substring(0, line.indexOf("="));
            String value = line.substring(line.indexOf("=") + 1);
            this.properties.put(key, value);
        }
    }

    @Override
    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
