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
    public Map<String, String> values;
    private boolean isFinished;

    public String getString(String s) {
        try {
            return this.values.get(s);
        } catch (Exception e) {
            return null;
        }
    }

    public String getString(String s, String d) {
        try {
            return this.values.get(s);
        } catch (Exception e) {
            return d;
        }
    }

    public int getInt(String s) {
        try {
            return Integer.parseInt(this.values.get(s));
        } catch (Exception ex) {
            return 0;
        }
    }

    public int getInt(String s, int d) {
        try {
            return Integer.parseInt(this.values.get(s));
        } catch (Exception ex) {
            return d;
        }
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

    public Object getType(String s, ValueType type, Object d) {
        try {
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
        } catch (Exception ex) {
            return d;
        }
    }

    private void readLines(InputStream resource) {
        List<String> doc = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.toList());

        for (var line : doc) {
            String key = line.substring(0, line.indexOf("="));
            String value = line.substring(line.indexOf("=") + 1);
            this.values.put(key, value);
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
