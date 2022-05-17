package com.classichabbo.goldfish.client.interfaces.types.widgets;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.controls.Label;
import com.classichabbo.goldfish.client.controls.ScrollPane;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.interfaces.types.alerts.Dialog;
import com.classichabbo.goldfish.client.visualisers.Visualiser;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Navigator extends Dialog {
    Pane searchButton;
    Pane ownButton;
    Pane favouritesButton;
    Label title;
    Label recommendedTitle;
    ScrollPane roomsList;

    @Override
    public void start() {
        super.start();

        var content = new Pane();
        
        content.setPrefSize(342, 414);
        content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        var publicButton = new Pane();
        publicButton.setPrefSize(170, 56);
        publicButton.setCursor(Cursor.HAND);
        //publicButton.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        publicButton.setLayoutX(1);
        publicButton.setLayoutY(1);
        publicButton.setOnMouseClicked(e -> {
            searchButton.setVisible(false);
            ownButton.setVisible(false);
            favouritesButton.setVisible(false);
            content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            title.setText("Public Rooms"); // load from packet
            title.setLayoutY(64);
            recommendedTitle.setVisible(false);
        });
        
        var privateButton = new Pane();
        privateButton.setPrefSize(169, 56);
        privateButton.setCursor(Cursor.HAND);
        //privateButton.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        privateButton.setLayoutX(172);
        privateButton.setLayoutY(1);
        privateButton.setOnMouseClicked(e -> {
            searchButton.setVisible(true);
            ownButton.setVisible(true);
            favouritesButton.setVisible(true);
            content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            title.setText("Guest Rooms"); // load from packet
            title.setLayoutY(165);
            recommendedTitle.setVisible(true);
        });

        searchButton = new Pane();
        searchButton.setPrefSize(113, 22);
        searchButton.setCursor(Cursor.HAND);
        //searchButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        searchButton.setVisible(false);
        searchButton.setLayoutX(1);
        searchButton.setLayoutY(58);
        searchButton.setOnMouseClicked(e -> {
            content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            title.setText(TextsManager.getInstance().getString("nav_src_hd"));
            title.setLayoutY(135);
            recommendedTitle.setVisible(false);
        });
        
        ownButton = new Pane();
        ownButton.setPrefSize(113, 22);
        ownButton.setCursor(Cursor.HAND);
        //ownButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        ownButton.setVisible(false);
        ownButton.setLayoutX(115);
        ownButton.setLayoutY(58);
        ownButton.setOnMouseClicked(e -> {
            content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            title.setText(TextsManager.getInstance().getString("nav_own_hd"));
            title.setLayoutY(135);
            recommendedTitle.setVisible(false);
        });

        favouritesButton = new Pane();
        favouritesButton.setPrefSize(112, 22);
        favouritesButton.setCursor(Cursor.HAND);
        //favouritesButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        favouritesButton.setVisible(false);
        favouritesButton.setLayoutX(229);
        favouritesButton.setLayoutY(58);
        favouritesButton.setOnMouseClicked(e -> {
            content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            title.setText(TextsManager.getInstance().getString("nav_fav_hd"));
            title.setLayoutY(90);
            recommendedTitle.setVisible(false);
        });

        title = new Label("Public Rooms", true); // load from packet
        title.setLayoutX(22);
        title.setLayoutY(64);

        recommendedTitle = new Label(TextsManager.getInstance().getString("nav_recommended_rooms"), true);
        recommendedTitle.setLayoutX(22);
        recommendedTitle.setLayoutY(85);
        recommendedTitle.setVisible(false);

        roomsList = new ScrollPane(330, 328);
        roomsList.setLayoutX(5);
        roomsList.setLayoutY(78);

        content.getChildren().addAll(publicButton, privateButton, searchButton, ownButton, favouritesButton, title, recommendedTitle, roomsList);

        this.setHidden(false);
        this.setPadding(9, 10, 11, 10);
        this.setTitle("Navigator");
        this.setContent(content);

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        super.stop();

        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        super.update();
        roomsList.update();
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
