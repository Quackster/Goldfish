package com.classichabbo.goldfish.client.views.types.widgets;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.controls.Label;
import com.classichabbo.goldfish.client.controls.ScrollPane;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;


public class Navigator extends Widget {
    Pane content;
    Pane searchButton;
    Pane ownButton;
    Pane favouritesButton;
    Label title;
    Label recommendedTitle;
    ScrollPane roomsList;
    Pane bottom;
    Label bottomTitle;
    Label bottomDescription;

    NavigatorPage setPage;
    boolean closeBottom;

    @Override
    public void start() {
        super.start();

        this.content = new Pane();
        this.content.setPrefSize(342, 414);
        this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        var publicButton = new Pane();
        publicButton.setPrefSize(170, 56);
        publicButton.setCursor(Cursor.HAND);
        //publicButton.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        publicButton.setLayoutX(1);
        publicButton.setLayoutY(1);
        publicButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.PUBLIC);

        var publicLabel = new Label(TextsManager.getInstance().getString("nav_publicRooms"), true);
        publicLabel.setLayoutX(64);
        publicLabel.setLayoutY(10);

        publicButton.getChildren().add(publicLabel);
        
        var privateButton = new Pane();
        privateButton.setPrefSize(169, 56);
        privateButton.setCursor(Cursor.HAND);
        //privateButton.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        privateButton.setLayoutX(172);
        privateButton.setLayoutY(1);
        privateButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.PRIVATE);

        var privateLabel = new Label(TextsManager.getInstance().getString("nav_privateRooms"), true);
        privateLabel.setLayoutX(63);
        privateLabel.setLayoutY(10);
        
        privateButton.getChildren().add(privateLabel);

        this.searchButton = new Pane();
        this.searchButton.setPrefSize(113, 22);
        this.searchButton.setCursor(Cursor.HAND);
        //this.searchButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.searchButton.setVisible(false);
        this.searchButton.setLayoutX(1);
        this.searchButton.setLayoutY(58);
        this.searchButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.SEARCH);
        
        var searchLabel = new Label(TextsManager.getInstance().getString("nav_searchbutton"));
        searchLabel.setLayoutX(23);
        searchLabel.setLayoutY(4);

        this.searchButton.getChildren().add(searchLabel);

        this.ownButton = new Pane();
        this.ownButton.setPrefSize(113, 22);
        this.ownButton.setCursor(Cursor.HAND);
        //this.ownButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.ownButton.setVisible(false);
        this.ownButton.setLayoutX(115);
        this.ownButton.setLayoutY(58);
        this.ownButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.OWN);
        
        var ownLabel = new Label(TextsManager.getInstance().getString("nav_rooms_own"));
        ownLabel.setLayoutX(26);
        ownLabel.setLayoutY(4);

        this.ownButton.getChildren().add(ownLabel);

        this.favouritesButton = new Pane();
        this.favouritesButton.setPrefSize(112, 22);
        this.favouritesButton.setCursor(Cursor.HAND);
        //this.favouritesButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.favouritesButton.setVisible(false);
        this.favouritesButton.setLayoutX(229);
        this.favouritesButton.setLayoutY(58);
        this.favouritesButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.FAVOURITES);
        
        var favouritesLabel = new Label(TextsManager.getInstance().getString("nav_rooms_favourite"));
        favouritesLabel.setLayoutX(24);
        favouritesLabel.setLayoutY(4);
        
        this.favouritesButton.getChildren().add(favouritesLabel);

        this.title = new Label("", true);
        this.title.setLayoutX(22);

        this.recommendedTitle = new Label(TextsManager.getInstance().getString("nav_recommended_rooms"), true);
        this.recommendedTitle.setLayoutX(22);
        this.recommendedTitle.setLayoutY(85);
        this.recommendedTitle.setVisible(false);

        this.roomsList = new ScrollPane();
        this.roomsList.setLayoutX(5);

        this.bottom = new Pane();
        this.bottom.setPrefSize(340, 100);
        this.bottom.setLayoutX(1);
        this.bottom.setLayoutY(312);
        this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.bottomTitle = new Label(TextsManager.getInstance().getString("nav_public_helptext_hd"), true);
        this.bottomTitle.setLayoutX(85);
        this.bottomTitle.setLayoutY(12);

        this.bottomDescription = new Label(TextsManager.getInstance().getString("nav_public_helptext"));
        this.bottomDescription.setLayoutX(85);
        this.bottomDescription.setLayoutY(24);
        this.bottomDescription.setMaxWidth(230);
        this.bottomDescription.setWrapText(true);

        var closeBottom = new Pane();
        closeBottom.setPrefSize(13, 13);
        closeBottom.setCursor(Cursor.HAND);
        closeBottom.setLayoutX(323);
        closeBottom.setLayoutY(13);
        closeBottom.setOnMouseClicked(e -> this.closeBottom = true);

        this.bottom.getChildren().addAll(bottomTitle, bottomDescription, closeBottom);
        this.content.getChildren().addAll(publicButton, privateButton, this.searchButton, this.ownButton, this.favouritesButton, this.title, this.recommendedTitle, this.roomsList, this.bottom);

        this.setPage = NavigatorPage.PUBLIC;
        this.closeBottom = false;

        this.setHidden(false);
        this.setPadding(9, 10, 11, 10);
        this.setTitle(TextsManager.getInstance().getString("navigator"));
        this.setContent(this.content);
        this.setFixedLocation((int)Movie.getInstance().getPrimaryStage().getWidth() - 376, 20);

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

        if (this.setPage == NavigatorPage.PUBLIC) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText("Public Rooms"); // load from packet
            this.title.setLayoutY(64);
            this.recommendedTitle.setVisible(false);
            
            this.searchButton.setVisible(false);
            this.ownButton.setVisible(false);
            this.favouritesButton.setVisible(false);

            this.roomsList.setSize(330, 236);
            this.roomsList.setLayoutY(76);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_public_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_public_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.setPage = null;
        }
        
        if (this.setPage == NavigatorPage.PRIVATE) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText("Guest Rooms"); // load from packet
            this.title.setLayoutY(165);
            this.recommendedTitle.setVisible(true);

            this.searchButton.setVisible(true);
            this.ownButton.setVisible(true);
            this.favouritesButton.setVisible(true);

            this.roomsList.setSize(330, 130);
            this.roomsList.setLayoutY(182);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd_main"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_private_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.setPage = null;
        }

        if (this.setPage == NavigatorPage.SEARCH) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_src_hd"));
            this.title.setLayoutY(135);
            this.recommendedTitle.setVisible(false);

            this.roomsList.setSize(330, 166);
            this.roomsList.setLayoutY(146);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_search_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.setPage = null;
        }

        if (this.setPage == NavigatorPage.OWN) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_own_hd"));
            this.title.setLayoutY(135);
            this.recommendedTitle.setVisible(false);

            this.roomsList.setSize(330, 166);
            this.roomsList.setLayoutY(146);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_ownrooms_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.setPage = null;
        }

        if (this.setPage == NavigatorPage.FAVOURITES) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_fav_hd"));
            this.title.setLayoutY(90);
            this.recommendedTitle.setVisible(false);

            this.roomsList.setSize(330, 211);
            this.roomsList.setLayoutY(101);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_favourites_helptext"));
            
            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.setPage = null;
        }

        if (this.closeBottom) {
            var newHeight = this.roomsList.getPrefHeight() + 96;
            this.roomsList.setSize(330, (int)newHeight);
            this.bottom.setVisible(false);

            this.closeBottom = false;
        }

        roomsList.update();
    }

    /*
    @Override
    public void visualiserChanged(Visualiser previousVisualiser, Visualiser currentVisualiser) {
        // We want to make this known, so don't make it hidden when moving visualisers
        this.setHidden(false);

        // Always bring to front when we move visualisers
        super.toFront();
    }

     */

    @Override
    protected void closeButtonClicked() {
        this.setHidden(true);
    }

    private enum NavigatorPage {
        PUBLIC,
        PRIVATE,
        SEARCH,
        OWN,
        FAVOURITES
    }
}
