package com.classichabbo.goldfish.client.visualisers.types.entry;

import com.classichabbo.goldfish.client.interfaces.types.EntryToolbar;
import com.classichabbo.goldfish.client.interfaces.types.LoadingBar;
import com.classichabbo.goldfish.client.interfaces.types.Navigator;
import com.classichabbo.goldfish.client.visualisers.Component;
import com.classichabbo.goldfish.client.visualisers.VisualiserType;
import com.classichabbo.goldfish.client.game.values.types.PropertiesManager;
import com.classichabbo.goldfish.client.scripts.Cloud;
import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.util.DimensionUtil;

import java.util.concurrent.ThreadLocalRandom;

public class EntryComponent implements Component {
    private final EntryVisualiser entryVisualiser;

    public EntryComponent(EntryVisualiser visualiser) {
        this.entryVisualiser = visualiser;
    }

    @Override
    public void init() {
        // Kickstart some clouds after turn point :^)
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 5) + 1; i++) {
            int initX = this.entryVisualiser.getCloudTurnPoint() + ThreadLocalRandom.current().nextInt(30, (int) (DimensionUtil.getProgramWidth()*0.5));;
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight()*0.66));

            this.addCloud("right", initX, initY);
        }

        // Kickstart some clouds before turn point :^)
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 2) + 1; i++) {
            int initX = this.entryVisualiser.getCloudTurnPoint() - ThreadLocalRandom.current().nextInt(35, 60);
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight()*0.66));

            this.addCloud("left", initX, initY);
        }
    }

    /**
     * Add cloud handler
     */
    public void addCloud(String direction, int initX, int initY) {
        var cloud = new Cloud(this.entryVisualiser.getCloudTurnPoint(), "cloud_" + ThreadLocalRandom.current().nextInt(4), direction, initX, initY);
        cloud.setViewOrder(EntryVisualiser.CLOUD_Z_INDEX);

        this.entryVisualiser.getClouds().add(cloud);
        this.entryVisualiser.getPane().getChildren().add(cloud);
    }

    public void loggedIn() {
        // Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));

        // Remove loading bar (moved to here so it removes it before it starts animating)
        // (if this is wrong please don't hate me was just finalising EntryToolbar) :)
        Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof LoadingBar).findFirst().ifPresent(loadingBar -> Movie.getInstance().removeObject(loadingBar));

        Movie.getInstance().createObject(new EntryToolbar(this.entryVisualiser));
        Movie.getInstance().createObject(new Navigator());
    }
}
