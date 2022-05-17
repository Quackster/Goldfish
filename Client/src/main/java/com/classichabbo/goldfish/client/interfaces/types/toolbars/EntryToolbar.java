package com.classichabbo.goldfish.client.interfaces.types.toolbars;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.controls.ButtonLarge;
import com.classichabbo.goldfish.client.controls.ImageButton;
import com.classichabbo.goldfish.client.controls.Label;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.interfaces.Interface;
import com.classichabbo.goldfish.client.interfaces.types.alerts.Alert;
import com.classichabbo.goldfish.client.interfaces.types.entry.EntryView;
import com.classichabbo.goldfish.client.interfaces.types.loader.LoadingScreen;
import com.classichabbo.goldfish.client.interfaces.types.room.RoomView;
import com.classichabbo.goldfish.client.interfaces.types.widgets.Navigator;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class EntryToolbar extends Interface {
    private final EntryView entryView;

    private ImageButton gamesButton;
    private ImageButton helpButton;
    private ImageButton catalogueButton;
    private ImageButton eventsButton;
    private ImageButton navigatorButton;
    private ImageButton chatButton;
    private ImageButton friendsButton;

    private int scrollOffset;
    private boolean finishedScroll;

    public EntryToolbar(EntryView entryView) {
        this.entryView = entryView;
    }

    @Override
    public void start() {
        this.scrollOffset = 0;
        this.finishedScroll = false;

        var temp = new ButtonLarge("Go to room UI");
        temp.setLayoutX(400);
        temp.setLayoutY(15);
        temp.setOnMouseClicked(e -> {
            if (Movie.getInstance().isInterfaceActive(EntryView.class)) {
                var entryView = Movie.getInstance().getInterfaceByClass(EntryView.class);

                entryView.transitionTo(() -> {
                    Movie.getInstance().createObject(new RoomView());
                    Movie.getInstance().removeObject(entryView);
                });

                Movie.getInstance().hideWidgets();
            }
            /*
            if (Movie.getInstance().getCurrentVisualiser().getType() == VisualiserType.HOTEL_VIEW) {
                ((EntryVisualiser)Movie.getInstance().getCurrentVisualiser()).transitionTo(VisualiserType.ROOM);
            } else {
                Movie.getInstance().showVisualiser(VisualiserType.ROOM);
            }*/
        });

        this.getChildren().add(temp);

        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        var userHead = new ImageButton(new Image("https://cdn.classichabbo.com/habbo-imaging/avatarimage?figure=hd-180-1.hr-100-61.ch-210-66.lg-270-82.sh-290-80&size=b&head=1&direction=3&head_direction=3&gesture=std"));
        userHead.setLayoutX(Math.floor(65 / 2 - userHead.getImage().getWidth() / 2));
        userHead.setLayoutY(Math.round(55 / 2 - userHead.getImage().getHeight() / 2));
        userHead.setCursor(Cursor.HAND);
        userHead.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("userHead clicked")));
        
        var userLabel = new Label("Testing", "#FFFFFF");
        // in Shockwave this is actually offset 2px to the left of the below two labels, but if you want it
        // in line with the others, set X back to 53 :)
        userLabel.setLayoutX(51);
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

        var clubButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/club.png"));
        clubButton.setLayoutX(245);
        clubButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("clubButton clicked")));
        
        this.chatButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/chat.png"));
        chatButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("chatButton clicked")));
        
        this.friendsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/friends.png"));
        friendsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("friendsButton clicked")));
        
        this.navigatorButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/navigator.png"));
        navigatorButton.setOnMouseClicked(e -> Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof Navigator).findFirst().ifPresent(x -> {
            x.toggleVisibility();
            x.toFront();
        }));
        
        this.eventsButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/events.png"));
        eventsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("eventsButton clicked")));
        
        this.catalogueButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/catalogue.png"));
        catalogueButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.gamesButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/games.png"));
        gamesButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("gamesButton clicked")));
        
        this.helpButton = new ImageButton(ResourceManager.getInstance().getFxImage("sprites/interfaces/entry_toolbar/help.png"));
        helpButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));
        
        this.getChildren().addAll(userHead, userLabel, mottoLabel, updateIdLabel, clubTitleLabel, clubDescLabel);
        this.getChildren().addAll(clubButton, chatButton, friendsButton, navigatorButton, eventsButton, catalogueButton, gamesButton, helpButton);
        this.toFront();

        Movie.getInstance().getGameScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        Movie.getInstance().getGameScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        /*
        if (this.scrollOffset != 55) {
            this.scrollOffset += 5;
        }
        else {
            this.finishedScroll = true;
        }

        if (this.finishedScroll) {
            Movie.getInstance().getInterfaces().stream().filter(i -> i.getType() == InterfaceType.NAVIGATOR).findFirst().ifPresent(i -> ((Navigator)i).setHidden(false));
        }*/
        if (!this.finishedScroll) {
            if (this.scrollOffset != 55) {
                this.scrollOffset += 5;
            } else {
                this.finishedScroll = true;
            }
        }

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

    /*
    @Override
    public void visualiserChanged(Visualiser previousVisualiser, Visualiser currentVisualiser) {
        if (previousVisualiser != null) {
            // System.out.println("Entry toolbar removed");
            this.remove();
        }
    }

     */
}