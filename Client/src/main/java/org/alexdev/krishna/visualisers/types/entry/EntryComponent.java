package org.alexdev.krishna.visualisers.types.entry;

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
}
