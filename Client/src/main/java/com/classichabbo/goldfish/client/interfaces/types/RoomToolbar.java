package com.classichabbo.goldfish.client.interfaces.types;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.controls.ImageButton;
import com.classichabbo.goldfish.client.controls.Label;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.interfaces.InterfaceType;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import javafx.scene.image.Image;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class RoomToolbar extends Interface {
    private ImageButton gamesButton;
    private ImageButton helpButton;
    private ImageButton catalogueButton;
    private ImageButton eventsButton;
    private ImageButton navigatorButton;
    private ImageButton chatButton;
    private ImageButton friendsButton;
    private ImageButton volumeButton;
    private ImageButton handButton;

    @Override
    public void start() {        
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        
        this.chatButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/chat.png"));
        chatButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("chatButton clicked")));
        
        this.friendsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/friends.png"));
        friendsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("friendsButton clicked")));
        
        this.navigatorButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/navigator.png"));
        navigatorButton.setOnMouseClicked(e -> Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof Navigator).findFirst().ifPresent(navigator -> ((Navigator)navigator).toggleNavigator()));
        
        this.eventsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/events.png"));
        eventsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("eventsButton clicked")));
        
        this.handButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/catalogue.png"));
        handButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.catalogueButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/catalogue.png"));
        catalogueButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.gamesButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/games.png"));
        gamesButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("gamesButton clicked")));
        
        this.helpButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/help.png"));
        helpButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));

        this.volumeButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/entry_toolbar/help.png"));
        volumeButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));
        
        this.getChildren().addAll(chatButton, friendsButton, navigatorButton, eventsButton, catalogueButton, handButton, gamesButton, helpButton, volumeButton);

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        // Handle resizing of window
        this.setLayoutY(DimensionUtil.getProgramHeight() - this.scrollOffset);
        this.setPrefSize(DimensionUtil.getProgramWidth(), 55);

        var pWidth = DimensionUtil.getProgramWidth();
        helpButton.setLayoutX(pWidth - 45);
        gamesButton.setLayoutX(pWidth - 81);
        catalogueButton.setLayoutX(pWidth - 121);
        eventsButton.setLayoutX(pWidth - 162);
        navigatorButton.setLayoutX(pWidth - 207);
        friendsButton.setLayoutX(pWidth - 244);
        chatButton.setLayoutX(pWidth - 284);
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.ENTRY_TOOLBAR;
    }
}
