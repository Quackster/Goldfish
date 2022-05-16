package com.classichabbo.goldfish.client.interfaces.types.widgets;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.controls.ButtonLarge;
import com.classichabbo.goldfish.client.controls.Label;
import com.classichabbo.goldfish.client.interfaces.types.alerts.Alert;
import com.classichabbo.goldfish.client.interfaces.types.alerts.Dialog;
import com.classichabbo.goldfish.client.interfaces.types.room.RoomTransition;
import com.classichabbo.goldfish.client.visualisers.Visualiser;
import com.classichabbo.goldfish.client.visualisers.VisualiserType;

import com.classichabbo.goldfish.client.visualisers.types.entry.EntryVisualiser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class Navigator extends Dialog {
    @Override
    public void start() {
        super.start();

        var content = new VBox();
        
        var text = new Label("This will be the navigator");
        text.setAlignment(Pos.CENTER);
        text.setWrapText(true);
        text.setLineSpacing(3);
        text.setPadding(new Insets(29, 24, 34, 12));
        text.setMinWidth(203);

        var room = new ButtonLarge("Go to room UI");
        room.setAlignment(Pos.CENTER);
        room.setPadding(new Insets(0, 0, 13, -5));
        room.setOnMouseClicked(e -> {
            if (Movie.getInstance().getCurrentVisualiser().getType() == VisualiserType.ROOM) {
                Movie.getInstance().createObject(new Alert("You are already on the room UI"));
            }
            else {
                this.setHidden(true);
                if (Movie.getInstance().getCurrentVisualiser().getType() == VisualiserType.HOTEL_VIEW) {
                    ((EntryVisualiser)Movie.getInstance().getCurrentVisualiser()).transitionTo(VisualiserType.ROOM);
                } else {
                    Movie.getInstance().showVisualiser(VisualiserType.ROOM);
                }
            }
        });

        var entry = new ButtonLarge("Go to hotelview");
        entry.setAlignment(Pos.CENTER);
        entry.setPadding(new Insets(0, 0, 13, -5));
        entry.setOnMouseClicked(e -> {
            if (Movie.getInstance().getCurrentVisualiser().getType() == VisualiserType.HOTEL_VIEW) {
                Movie.getInstance().createObject(new Alert("You are already on the hotelview"));
            }
            else {
                if (Movie.getInstance().getCurrentVisualiser().getType() == VisualiserType.ROOM) {
                    Movie.getInstance().createObject(new RoomTransition(VisualiserType.HOTEL_VIEW));
                }
                // Movie.getInstance().showVisualiser(VisualiserType.HOTEL_VIEW);
            }
        });

        content.getChildren().addAll(text, room, entry);
        
        this.setHidden(false);
        this.setPadding(9, 10, 11, 10);
        this.setTitle("Navigator");
        this.setContent(content);
        this.initInnerBackground();

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        super.stop();

        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void visualiserChanged(Visualiser previousVisualiser, Visualiser currentVisualiser) {
        // We want to make this known, so don't make it hidden when moving visualisers
        this.setHidden(false);

        // Always bring to front when we move visualisers
        super.toFront();
    }

    @Override
    protected void closeButtonClicked() {
        this.setHidden(true);
    }
}
