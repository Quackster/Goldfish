package org.alexdev.krishna.interfaces.types;

import org.alexdev.krishna.Movie;
import org.alexdev.krishna.controls.Label;
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.interfaces.InterfaceType;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class EntryToolbar extends Interface {
    @Override
    public void start() {
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        var userLabel = new Label("Parsnip", "#FFFFFF");
        userLabel.setLayoutX(51);
        userLabel.setLayoutY(8);

        var mottoLabel = new Label("Developer, Habbo enthusiast :)", "#FFFFFF");
        mottoLabel.setLayoutX(53);
        mottoLabel.setLayoutY(20);

        var updateIdLabel = new Label("Update My Habbo Id >>", "#FFFFFF");
        updateIdLabel.setLayoutX(53);
        updateIdLabel.setLayoutY(32);
        updateIdLabel.setUnderline(true);
        updateIdLabel.setCursor(Cursor.HAND);
        updateIdLabel.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("updateIdLabel clicked")));
        
        var clubTitleLabel = new Label("Habbo Club", "#FFFFFF");
        clubTitleLabel.setLayoutX(287);
        clubTitleLabel.setLayoutY(19);

        var clubDescLabel = new Label("4015 days", "#FFFFFF");
        clubDescLabel.setLayoutX(287);
        clubDescLabel.setLayoutY(30);
        clubDescLabel.setCursor(Cursor.HAND);
        clubDescLabel.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("clubDescLabel clicked")));

        this.getChildren().addAll(userLabel, mottoLabel, updateIdLabel, clubTitleLabel, clubDescLabel);

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }
    
    @Override
    public void update() {
        this.setPrefSize(Movie.getInstance().getPrimaryStage().getWidth(), 55);
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.ENTRY_TOOLBAR;
    }
}
