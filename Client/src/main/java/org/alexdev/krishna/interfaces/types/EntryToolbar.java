package org.alexdev.krishna.interfaces.types;

import javafx.scene.image.Image;
import org.alexdev.krishna.Movie;
import org.alexdev.krishna.controls.ImageButton;
import org.alexdev.krishna.controls.Label;
import org.alexdev.krishna.game.resources.ResourceManager;
import org.alexdev.krishna.interfaces.Interface;
import org.alexdev.krishna.interfaces.InterfaceType;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.alexdev.krishna.util.DimensionUtil;
import org.alexdev.krishna.visualisers.types.entry.EntryVisualiser;

public class EntryToolbar extends Interface {
    private final EntryVisualiser entryVisualiser;
    // TO-DO
    // - handle resizing
    // - add user icon
    private int scrollOffset;
    private ImageButton gamesButton;
    private ImageButton helpButton;
    private ImageButton catalogueButton;
    private ImageButton eventsButton;
    private ImageButton navigatorButton;
    private ImageButton chatButton;
    private ImageButton friendsButton;
    private boolean animateEntryBar;

    public EntryToolbar(EntryVisualiser entryVisualiser) {
        this.entryVisualiser = entryVisualiser;
    }

    @Override
    public void start() {
        this.scrollOffset = 0;
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.setLayoutY(Movie.getInstance().getCurrentVisualiser().getPane().getHeight());

        var userHead = new ImageButton(new Image("https://cdn.classichabbo.com/habbo-imaging/avatarimage?figure=hd-180-1.hr-100-61.ch-210-66.lg-270-82.sh-290-80&size=b&head=1&direction=3&head_direction=3"));
        userHead.setLayoutX(13); // 51 -> 52
        userHead.setLayoutY(8);

        var userLabel = new Label("Testing", "#FFFFFF");
        userLabel.setLayoutX(53); // 51 -> 52
        userLabel.setLayoutY(8);

        var mottoLabel = new Label("This certainly is a test", "#FFFFFF");
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

        var clubButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/club.png"));
        clubButton.setLayoutX(245);
        clubButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("clubButton clicked")));
        
        this.chatButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/chat.png"));
        chatButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("chatButton clicked")));
        chatButton.setLayoutX(676);
        
        this.friendsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/friends.png"));
        friendsButton.setLayoutX(716);
        friendsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("friendsButton clicked")));
        
        this.navigatorButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/navigator.png"));
        navigatorButton.setLayoutX(753);
        navigatorButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("navigatorButton clicked")));
        
        this.eventsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/events.png"));
        eventsButton.setLayoutX(798);
        eventsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("eventsButton clicked")));
        
        this.catalogueButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/catalogue.png"));
        catalogueButton.setLayoutX(839);
        catalogueButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.gamesButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/games.png"));
        gamesButton.setLayoutX(879);
        gamesButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("gamesButton clicked")));
        
        this.helpButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/help.png"));
        helpButton.setLayoutX(915);
        helpButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));
        //helpButton.setOnMouseClicked(e -> this.scrollOffset = 0);
        
        this.getChildren().addAll(userHead, userLabel, mottoLabel, updateIdLabel, clubTitleLabel, clubDescLabel);
        this.getChildren().addAll(clubButton, chatButton, friendsButton, navigatorButton, eventsButton, catalogueButton, gamesButton, helpButton);
        this.animateEntryBar = true;

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        if (this.animateEntryBar) {
            if (this.scrollOffset != 55) {
                this.scrollOffset += 5;
            } else {
                // Remove loading bar
                var loadingBar = Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof LoadingBar).findFirst().orElse(null);

                if (loadingBar != null) {
                    Movie.getInstance().removeObject(loadingBar);
                }

                this.entryVisualiser.getComponent().loggedIn();
                this.animateEntryBar = false;
            }

            this.setLayoutY(DimensionUtil.getProgramHeight() - this.scrollOffset);
        }

        // Handle resizing of window
        helpButton.setLayoutX(DimensionUtil.getProgramWidth() - (helpButton.getX() + helpButton.getImage().getWidth()) - 23);
        gamesButton.setLayoutX(helpButton.getLayoutX() - (gamesButton.getX() + gamesButton.getImage().getWidth()) - 12);
        catalogueButton.setLayoutX(gamesButton.getLayoutX() - (catalogueButton.getX() + catalogueButton.getImage().getWidth()) - 3);
        eventsButton.setLayoutX(catalogueButton.getLayoutX() - (eventsButton.getX() + eventsButton.getImage().getWidth()) - 12);
        navigatorButton.setLayoutX(eventsButton.getLayoutX() - (navigatorButton.getX() + navigatorButton.getImage().getWidth()) - 12);
        friendsButton.setLayoutX(navigatorButton.getLayoutX() - (friendsButton.getX() + friendsButton.getImage().getWidth()) - 12);
        chatButton.setLayoutX(friendsButton.getLayoutX() - (chatButton.getX() + chatButton.getImage().getWidth()) - 12);

        // Move bar to the bottom, always
        this.setPrefSize(DimensionUtil.getProgramWidth(), 55);

        if (!this.animateEntryBar)
            this.setLayoutY(DimensionUtil.getProgramHeight() - this.getPrefHeight());
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.ENTRY_TOOLBAR;
    }
}
