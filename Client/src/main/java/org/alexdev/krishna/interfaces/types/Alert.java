package org.alexdev.krishna.interfaces.types;

import org.alexdev.krishna.HabboClient;
import org.alexdev.krishna.controls.ButtonLarge;
import org.alexdev.krishna.controls.Label;
import org.alexdev.krishna.interfaces.InterfaceType;

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
        ok.setOnMouseClicked(e -> this.stop());

        content.getChildren().addAll(text, ok);
        
        this.setPadding(9, 10, 11, 10);
        this.setTitle("Notice!");
        this.setContent(content);
        this.initInnerBackground();

        HabboClient.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.ALERT;
    }
}
