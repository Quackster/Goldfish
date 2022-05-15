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
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
        this.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/background.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        
        this.chatButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/chat.png"));
        chatButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("chatButton clicked")));
        
        this.friendsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/friends.png"));
        friendsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("friendsButton clicked")));
        
        this.navigatorButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/navigator.png"));
        navigatorButton.setOnMouseClicked(e -> Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof Navigator).findFirst().ifPresent(navigator -> ((Navigator)navigator).toggleNavigator()));
        
        this.eventsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/events.png"));
        eventsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("eventsButton clicked")));
        
        this.handButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/hand.png"));
        handButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.catalogueButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/catalogue.png"));
        catalogueButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.gamesButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/games.png"));
        gamesButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("gamesButton clicked")));
        
        this.helpButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/help.png"));
        helpButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));

        this.volumeButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/room_toolbar/volume.png"));
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
        this.setLayoutY(DimensionUtil.getProgramHeight() - 52);
        this.setPrefSize(DimensionUtil.getProgramWidth(), 52);

        var pWidth = DimensionUtil.getProgramWidth();
        volumeButton.setLayoutX(pWidth - 30);
        helpButton.setLayoutX(pWidth - 59);
        gamesButton.setLayoutX(pWidth - 94);
        handButton.setLayoutX(pWidth - 140);
        catalogueButton.setLayoutX(pWidth - 178);
        eventsButton.setLayoutX(pWidth - 219);
        navigatorButton.setLayoutX(pWidth - 260);
        friendsButton.setLayoutX(pWidth - 295);
        chatButton.setLayoutX(pWidth - 333);
    }

    @Override
    public InterfaceType getType() {
        return InterfaceType.ROOM_TOOLBAR;
    }
}
