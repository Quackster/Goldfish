package org.alexdev.krishna.interfaces.types;

import org.alexdev.krishna.Movie;
import org.alexdev.krishna.controls.ImageButton;
import org.alexdev.krishna.controls.Label;
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.interfaces.InterfaceType;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class EntryToolbar extends Interface {
    // TO-DO
    // - handle resizing
    // - add user icon
    private int scrollOffset;

    @Override
    public void start() {
        this.scrollOffset = 0;
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.setLayoutY(Movie.getInstance().getCurrentVisualiser().getPane().getHeight());

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

        var clubButton = new ImageButton("sprites/entry_toolbar/club.png");
        clubButton.setLayoutX(245);
        clubButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("clubButton clicked")));
        
        var chatButton = new ImageButton("sprites/entry_toolbar/chat.png");
        chatButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("chatButton clicked")));
        chatButton.setLayoutX(676);
        
        var friendsButton = new ImageButton("sprites/entry_toolbar/friends.png");
        friendsButton.setLayoutX(716);
        friendsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("friendsButton clicked")));
        
        var navigatorButton = new ImageButton("sprites/entry_toolbar/navigator.png");
        navigatorButton.setLayoutX(753);
        navigatorButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("navigatorButton clicked")));
        
        var eventsButton = new ImageButton("sprites/entry_toolbar/events.png");
        eventsButton.setLayoutX(798);
        eventsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("eventsButton clicked")));
        
        var catalogueButton = new ImageButton("sprites/entry_toolbar/catalogue.png");
        catalogueButton.setLayoutX(839);
        catalogueButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        var gamesButton = new ImageButton("sprites/entry_toolbar/games.png");
        gamesButton.setLayoutX(879);
        gamesButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("gamesButton clicked")));
        
        var helpButton = new ImageButton("sprites/entry_toolbar/help.png");
        helpButton.setLayoutX(915);
        helpButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));
        //helpButton.setOnMouseClicked(e -> this.scrollOffset = 0);
        
        this.getChildren().addAll(userLabel, mottoLabel, updateIdLabel, clubTitleLabel, clubDescLabel);
        this.getChildren().addAll(clubButton, chatButton, friendsButton, navigatorButton, eventsButton, catalogueButton, gamesButton, helpButton);

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        this.setPrefSize(Movie.getInstance().getPrimaryStage().getWidth(), 55);

        if (this.scrollOffset != 55) {
            this.scrollOffset += 5;
        }

        this.setLayoutY(Movie.getInstance().getCurrentVisualiser().getPane().getHeight() - scrollOffset);
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.ENTRY_TOOLBAR;
    }
}
