package com.classichabbo.goldfish.client.modules.types.alerts;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.modules.controls.ButtonLarge;
import com.classichabbo.goldfish.client.modules.controls.Label;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class Alert extends Dialog {
    private String text;

    public Alert(String text) {
        this.text = text;
    }

    @Override
    public void start() {
        super.start();

        var content = new VBox();
        var text = new Label(this.text);
        text.setAlignment(Pos.CENTER);
        text.setWrapText(true);
        text.setLineSpacing(3);
        text.setPadding(new Insets(29, 24, 34, 12));
        text.setMinWidth(203);

        var ok = new ButtonLarge("OK");
        ok.setAlignment(Pos.CENTER);
        ok.setPadding(new Insets(0, 0, 13, -5));
        ok.setOnMouseClicked(e -> this.remove());

        content.getChildren().addAll(text, ok);

        this.setTitle("Notice!");
        this.setContent(content, new Insets(9, 10, 11, 10), true);
        this.initInnerBackground();
        this.setCallAfterFinish(() -> {
            System.out.println("alert finished");
        });

        this.registerUpdate();
    }

    @Override
    public void stop() {
        super.stop();
        this.removeUpdate();
    }

    @Override
    public void registerUpdate() {
        // Queue to receive
        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void removeUpdate() {
        // Remove from update queue
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    /*
    @Override
    public void visualiserChanged(Visualiser previousVisualiser, Visualiser currentVisualiser) {
        super.toFront(); // Always bring to front when we move visualisers
    }

     */
}
