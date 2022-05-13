package org.alexdev.krishna.visualisers.types.room;

import org.alexdev.krishna.visualisers.Component;

public class RoomComponent implements Component {
    private final RoomVisualiser roomVisualiser;

    public RoomComponent(RoomVisualiser visualiser) {
        this.roomVisualiser = visualiser;
    }

    @Override
    public void init() {


    }

}
