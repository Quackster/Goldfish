package com.classichabbo.goldfish.client.game.room.model;

import com.classichabbo.goldfish.client.Goldfish;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;

import java.io.BufferedReader;
import java.io.IOException;

public class RoomModelManager {
    private static RoomModelManager instance;

    public RoomModelManager() {

        /*try {
            var object = Goldfish.getInstance().getGson().fromJson(
                    new String(ResourceManager.getInstance().getResource("assets/views/room/models/model_a.json").openStream().readAllBytes()),
                    RoomModel.class
            );


            System.out.println(Goldfish.getInstance().getGson().toJson(object));

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static RoomModelManager getInstance() {
        if (instance == null) {
            instance = new RoomModelManager();
        }

        return instance;
    }
}
