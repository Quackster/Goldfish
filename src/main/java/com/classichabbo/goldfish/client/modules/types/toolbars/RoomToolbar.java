package com.classichabbo.goldfish.client.modules.types.toolbars;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.modules.controls.ButtonLarge;
import com.classichabbo.goldfish.client.modules.controls.ImageButton;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.types.alerts.Alert;
import com.classichabbo.goldfish.util.DimensionUtil;

import com.classichabbo.goldfish.client.modules.types.widgets.navigator.NavigatorView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class RoomToolbar extends View {
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
        var temp = new ButtonLarge("Go to hotelview");
        temp.setLayoutX(15);
        temp.setLayoutY(15);
        temp.setOnMouseClicked(e -> {
            Movie.getInstance().goToHotelView();
        });

        this.getChildren().add(temp);
        
        this.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "background.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        
        this.chatButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "chat.png"));
        chatButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("chatButton clicked")));
        
        this.friendsButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "friends.png"));
        friendsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("friendsButton clicked")));
        
        this.navigatorButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "navigator.png"));
        navigatorButton.setOnMouseClicked(e -> Movie.getInstance().getViews().stream().filter(x -> x instanceof NavigatorView).findFirst().ifPresent(navigator -> {
            navigator.toggleVisibility();
            navigator.toFront();
        }));
        
        this.eventsButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "events.png"));
        eventsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("eventsButton clicked")));
        
        this.handButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "hand.png"));
        handButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.catalogueButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "catalogue.png"));
        catalogueButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));
        
        this.gamesButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "games.png"));
        gamesButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("gamesButton clicked")));
        
        this.helpButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "help.png"));
        helpButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));

        this.volumeButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/room_toolbar/", "volume.png"));
        volumeButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));
        
        this.getChildren().addAll(chatButton, friendsButton, navigatorButton, eventsButton, catalogueButton, handButton, gamesButton, helpButton, volumeButton);

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

    /*
    @Override
    public void visualiserChanged(Visualiser previousVisualiser, Visualiser currentVisualiser) {
        if (previousVisualiser != null) {
            // System.out.println("Room toolbar removed");
            this.remove();
        }
    }

     */
}
