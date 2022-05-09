package org.alexdev.krishna.game.properties;

public class PropertiesManager {
    private static PropertiesManager instance;



    public static PropertiesManager getInstance() {
        if (instance == null) {
            instance = new PropertiesManager();
        }

        return instance;
    }
}
