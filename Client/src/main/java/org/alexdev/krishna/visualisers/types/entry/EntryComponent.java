package org.alexdev.krishna.visualisers.types.entry;

import org.alexdev.krishna.Movie;
import org.alexdev.krishna.interfaces.types.Alert;
import org.alexdev.krishna.scripts.Cloud;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.Component;
import org.alexdev.krishna.visualisers.Visualiser;

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
        Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));
    }
}
