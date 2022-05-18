package com.classichabbo.goldfish.client.views.types.widgets;

import java.util.ArrayList;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.views.controls.ButtonLarge;
import com.classichabbo.goldfish.client.views.controls.Label;
import com.classichabbo.goldfish.client.views.controls.ScrollPane;
import com.classichabbo.goldfish.client.views.types.alerts.Alert;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.room.RoomView;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;

import com.classichabbo.goldfish.client.util.DimensionUtil;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Navigator extends Widget {
    private Pane content;
    private Pane searchButton;
    private Pane ownButton;
    private Pane favouritesButton;
    private Pane bottom;

    private Label title;
    private Label hideFull;
    private Label recommendedTitle;
    private Label recommendedRefresh;
    private VBox recommendedList;

    private Label bottomTitle;
    private Label bottomDescription;
    private ImageView bottomRoomImg;
    private Pane bottomSecondaryButton;
    private ButtonLarge bottomGoButton;

    private ScrollPane navList;

    private ArrayList<NavItem> navItems;
    private ArrayList<NavItem> recommendedItems;
    private NavigatorPage currentPage;

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
        publicButton.setOnMouseClicked(e -> this.currentPage = NavigatorPage.PUBLIC);
        this.content.getChildren().add(publicButton);

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
        privateButton.setOnMouseClicked(e -> this.currentPage = NavigatorPage.PRIVATE);
        this.content.getChildren().add(privateButton);

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
        this.searchButton.setOnMouseClicked(e -> this.currentPage = NavigatorPage.SEARCH);
        this.content.getChildren().addAll(this.searchButton);

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
        this.ownButton.setOnMouseClicked(e -> this.currentPage = NavigatorPage.OWN);
        this.content.getChildren().add(this.ownButton);


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
        this.favouritesButton.setOnMouseClicked(e -> this.currentPage = NavigatorPage.FAVOURITES);
        this.content.getChildren().add(this.favouritesButton);

        var favouritesLabel = new Label(TextsManager.getInstance().getString("nav_rooms_favourite"));
        favouritesLabel.setLayoutX(24);
        favouritesLabel.setLayoutY(4);
        
        this.favouritesButton.getChildren().add(favouritesLabel);

        this.title = new Label("", true);
        this.title.setLayoutX(22);
        this.content.getChildren().add(this.title);

        this.hideFull = new Label(TextsManager.getInstance().getString("nav_hidefull"), "#7B9498");
        this.hideFull.setLayoutX(235);
        this.hideFull.setMinWidth(100);
        this.hideFull.setAlignment(Pos.TOP_RIGHT);
        this.hideFull.setUnderline(true);
        this.hideFull.setCursor(Cursor.HAND);
        this.content.getChildren().add(hideFull);

        this.recommendedTitle = new Label(TextsManager.getInstance().getString("nav_recommended_rooms"), true);
        this.recommendedTitle.setLayoutX(22);
        this.recommendedTitle.setLayoutY(85);
        this.content.getChildren().add(this.recommendedTitle);

        this.recommendedRefresh = new Label(TextsManager.getInstance().getString("nav_refresh_recoms"), "#7B9498");
        this.recommendedRefresh.setLayoutX(185);
        this.recommendedRefresh.setLayoutY(85);
        this.recommendedRefresh.setMinWidth(150);
        this.recommendedRefresh.setAlignment(Pos.TOP_RIGHT);
        this.recommendedRefresh.setUnderline(true);
        this.recommendedRefresh.setCursor(Cursor.HAND);
        this.content.getChildren().add(recommendedRefresh);

        this.recommendedList = new VBox(2);
        this.recommendedList.setLayoutX(5);
        this.recommendedList.setLayoutY(103);
        this.recommendedList.setMinWidth(311);
        this.content.getChildren().add(recommendedList);

        this.navList = new ScrollPane();
        this.navList.setSpacing(2);
        this.navList.setLayoutX(5);
        this.content.getChildren().add(this.navList);

        this.bottom = new Pane();
        this.bottom.setPrefSize(340, 100);
        this.bottom.setLayoutX(1);
        this.bottom.setLayoutY(312);
        this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.content.getChildren().add(this.bottom);

        this.bottomTitle = new Label(TextsManager.getInstance().getString("nav_public_helptext_hd"), true);
        this.bottomTitle.setLayoutX(85);
        this.bottomTitle.setLayoutY(12);
        this.bottom.getChildren().add(this.bottomTitle);

        this.bottomDescription = new Label(TextsManager.getInstance().getString("nav_public_helptext"));
        this.bottomDescription.setLayoutX(85);
        this.bottomDescription.setLayoutY(24);
        this.bottomDescription.setMaxWidth(230);
        this.bottomDescription.setWrapText(true);
        this.bottom.getChildren().add(this.bottomDescription);

        var closeBottom = new Pane();
        closeBottom.setPrefSize(13, 13);
        closeBottom.setCursor(Cursor.HAND);
        closeBottom.setLayoutX(323);
        closeBottom.setLayoutY(13);
        closeBottom.setOnMouseClicked(e -> this.closeBottom = true);
        this.bottom.getChildren().add(closeBottom);

        this.bottomRoomImg = new ImageView();
        this.bottomRoomImg.setLayoutX(16);
        this.bottomRoomImg.setLayoutY(15);
        this.bottomRoomImg.setVisible(false);
        this.bottom.getChildren().add(bottomRoomImg);

        this.bottomGoButton = new ButtonLarge(TextsManager.getInstance().getString("nav_gobutton"));
        this.bottomGoButton.setLayoutX(280);
        this.bottomGoButton.setLayoutY(71);
        this.bottomGoButton.setCursor(Cursor.HAND);
        this.bottomGoButton.setVisible(false);
        this.bottom.getChildren().add(bottomGoButton);
        
        this.currentPage = NavigatorPage.PUBLIC;
        this.closeBottom = false;

        this.setPadding(9, 10, 11, 10);
        this.setTitle(TextsManager.getInstance().getString("navigator"));
        this.setContent(this.content);

        // getWidth() and getHeight() no longer zero when wrapping around this shit - when the navigator finished sizing itself
        // make it appear to the side first and foremost :^)
        this.setCallAfterFinish(() -> {
            this.setLayoutX(DimensionUtil.roundEven((DimensionUtil.getProgramWidth() - this.getWidth()) * 0.95));
            this.setLayoutY(20);
        });

        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void stop() {
        super.update();
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void update() {
        super.update();

        if (this.currentPage == NavigatorPage.PUBLIC) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getCategory(1); // TODO - where does the root category for public rooms come from?
            this.title.setLayoutY(64);
            this.hideFull.setLayoutY(64);
            this.hideFull.setVisible(true);
            this.recommendedTitle.setVisible(false);
            this.recommendedRefresh.setVisible(false);
            this.recommendedList.setVisible(false);
            
            this.searchButton.setVisible(false);
            this.ownButton.setVisible(false);
            this.favouritesButton.setVisible(false);

            this.navList.setSize(330, 232);
            this.navList.setLayoutY(80);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_public_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_public_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.currentPage = null;
        }
        
        if (this.currentPage == NavigatorPage.PRIVATE) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getCategory(2); // TODO - same as above
            this.getRecommendedItems();
            this.title.setLayoutY(165);
            this.hideFull.setLayoutY(165);
            this.hideFull.setVisible(true);
            this.recommendedTitle.setVisible(true);
            this.recommendedRefresh.setVisible(true);
            this.recommendedList.setVisible(true);

            this.searchButton.setVisible(true);
            this.ownButton.setVisible(true);
            this.favouritesButton.setVisible(true);

            this.navList.setSize(330, 126);
            this.navList.setLayoutY(186);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd_main"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_private_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.currentPage = null;
        }

        if (this.currentPage == NavigatorPage.SEARCH) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getSearch(null);
            this.title.setText(TextsManager.getInstance().getString("nav_src_hd"));
            this.title.setLayoutY(135);
            this.hideFull.setVisible(false);
            this.recommendedTitle.setVisible(false);
            this.recommendedRefresh.setVisible(false);
            this.recommendedList.setVisible(false);

            this.navList.setSize(330, 162);
            this.navList.setLayoutY(150);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_search_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.currentPage = null;
        }

        if (this.currentPage == NavigatorPage.OWN) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getOwn();
            this.title.setText(TextsManager.getInstance().getString("nav_own_hd"));
            this.title.setLayoutY(135);
            this.hideFull.setVisible(false);
            this.recommendedTitle.setVisible(false);
            this.recommendedRefresh.setVisible(false);
            this.recommendedList.setVisible(false);

            this.navList.setSize(330, 162);
            this.navList.setLayoutY(150);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_ownrooms_helptext"));

            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.currentPage = null;
        }

        if (this.currentPage == NavigatorPage.FAVOURITES) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getFavourites();
            this.title.setText(TextsManager.getInstance().getString("nav_fav_hd"));
            this.title.setLayoutY(90);
            this.hideFull.setVisible(false);
            this.recommendedTitle.setVisible(false);
            this.recommendedRefresh.setVisible(false);
            this.recommendedList.setVisible(false);

            this.navList.setSize(330, 207);
            this.navList.setLayoutY(105);

            this.bottomTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.bottomDescription.setText(TextsManager.getInstance().getString("nav_favourites_helptext"));
            
            this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.bottom.setVisible(true);
            this.currentPage = null;
        }

        if (this.closeBottom) {
            var newHeight = this.navList.getPrefHeight() + 96;
            this.navList.setSize(330, (int)newHeight);
            this.bottom.setVisible(false);

            this.closeBottom = false;
        }

        if (this.navItems != null) {
            this.navList.clearContent();

            for (var navItem : navItems) {
                this.navList.addContent(generateNavListItem(navItem));
            }

            this.navItems = null;
        }

        if (this.recommendedItems != null) {
            this.recommendedList.getChildren().clear();

            for (var recommendedItem : recommendedItems) {
                this.recommendedList.getChildren().add(generateNavListItem(recommendedItem));
            }

            this.recommendedItems = null;
        }

        this.navList.update();
    }

    private Pane generateNavListItem(NavItem navItem) {
        var navListItem = new Pane();
        navListItem.setMinHeight(16);
        navListItem.setMaxWidth(311);

        var nameLabel = new Label(navItem.name);
        nameLabel.setLayoutX(17);
        nameLabel.setLayoutY(2);

        navListItem.getChildren().add(nameLabel);

        if (navItem.isCategory) {
            var openLabel = new Label(TextsManager.getInstance().getString("nav_openbutton"));
            openLabel.setLayoutX(233);
            openLabel.setLayoutY(2);
            openLabel.setAlignment(Pos.TOP_RIGHT);
            openLabel.setMinWidth(48);
            openLabel.setUnderline(true);

            var openButton = new Pane();
            openButton.setPrefSize(311, 16);
            openButton.setCursor(Cursor.HAND);

            navListItem.getChildren().addAll(openLabel, openButton);
            navListItem.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/category_empty.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        }
        else {
            var goLabel = new Label(TextsManager.getInstance().getString("nav_gobutton"));
            goLabel.setLayoutX(271);
            goLabel.setLayoutY(2);
            goLabel.setAlignment(Pos.TOP_RIGHT);
            goLabel.setMinWidth(24);
            goLabel.setUnderline(true);

            var nameButton = new Pane();
            nameButton.setPrefSize(251, 16);
            nameButton.setCursor(Cursor.HAND);
            nameButton.setOnMouseClicked(e -> {
                this.bottom.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/bottom_background_room.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
                
                this.bottomTitle.setText(navItem.name + "\n" + "(" + navItem.visitors + "/" + navItem.maxVisitors + ") " + TextsManager.getInstance().getString("room_owner") + " " + navItem.owner);
                this.bottomDescription.setText(navItem.description);

                this.bottomRoomImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/door.png"));
                this.bottomRoomImg.setVisible(true);
                
                this.bottomGoButton.setOnMouseClicked(e1 -> openRoom(navItem.id));
                this.bottomGoButton.setVisible(true);

                this.bottom.setVisible(true);
            });

            var goButton = new Pane();
            goButton.setPrefSize(58, 16);
            goButton.setLayoutX(253);
            goButton.setCursor(Cursor.HAND);
            goButton.setOnMouseClicked(e -> openRoom(navItem.id));

            navListItem.getChildren().addAll(goLabel, nameButton, goButton);
            navListItem.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/room_empty.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        }

        return navListItem;
    }

    private void getRecommendedItems () {
        this.recommendedItems = new ArrayList<NavItem>();

        this.recommendedItems.add(new NavItem(1, false, "Hall", "New? Lost? Get a warm welcome here!", "C-3", 0, 40));
        this.recommendedItems.add(new NavItem(1, false, "Tresor", "Visit the park and the infamous Infobus", "zidro", 0, 65));
        this.recommendedItems.add(new NavItem(1, false, "Box ( Habbo.nl - 2007 )", "Splish, splash and have a bash in the famous Habbo pool!", "Miquel", 0, 120));
    }

    private void getCategory(int categoryId) {
        this.navItems = new ArrayList<NavItem>();

        if (categoryId == 1) {
            this.title.setText("Public Rooms");
            this.navItems.add(new NavItem(1, true, "Welcome Lounge", "New? Lost? Get a warm welcome here!", "", 0, 40));
            this.navItems.add(new NavItem(1, true, "The Park", "Visit the park and the infamous Infobus", "", 0, 65));
            this.navItems.add(new NavItem(1, true, "Habbo Lido", "Splish, splash and have a bash in the famous Habbo pool!", "", 0, 120));
            this.navItems.add(new NavItem(1, true, "Rooftop Rumble", "Wobble Squabble your bum off in our cool rooftop hang out", "", 0, 50));
            this.navItems.add(new NavItem(1, "Entertainment", 0, 100));
            this.navItems.add(new NavItem(1, "Restaurants and Cafes", 0, 100));
            this.navItems.add(new NavItem(1, "Lounges and Clubs", 0, 100));
            this.navItems.add(new NavItem(1, "Habbo Club", 0, 100));
            this.navItems.add(new NavItem(1, "Outside Spaces", 0, 100));
            this.navItems.add(new NavItem(1, "The Lobbies", 0, 100));
            this.navItems.add(new NavItem(1, "The Hallways", 0, 100));
            this.navItems.add(new NavItem(1, "Games", 0, 100));
        }

        if (categoryId == 2) {
            this.title.setText("Guest Rooms");
            this.navItems.add(new NavItem(1, "Flower Power Puzzle", 0, 100));
            this.navItems.add(new NavItem(1, "Gaming & Race Rooms", 0, 100));
            this.navItems.add(new NavItem(1, "Restaurant, Bar & Night Club Rooms", 0, 100));
            this.navItems.add(new NavItem(1, "Trade Floor", 0, 100));
            this.navItems.add(new NavItem(1, "Chill, Chat & Discussion Rooms", 0, 100));
            this.navItems.add(new NavItem(1, "Hair Salons & Modelling Rooms", 0, 100));
            this.navItems.add(new NavItem(1, "Maze & Theme Park Rooms", 0, 100));
            this.navItems.add(new NavItem(1, "Help Centre Rooms", 0, 100));
            this.navItems.add(new NavItem(1, "Miscellaneous", 0, 100));
        }
    }

    private void getSearch(String criteria) {
        this.navItems = new ArrayList<NavItem>();
    }

    private void getOwn() {        
        this.navItems = new ArrayList<NavItem>();

        this.navItems.add(new NavItem(1, false, "Parsnip's Casino", "Large bets welcomed, games 13/21/poker", "Parsnip", 0, 15));
        this.navItems.add(new NavItem(1, false, "Parsnip's Hub", "Sit and chat or go through the teles to see some of my favourite rooms", "Parsnip", 0, 25));
        this.navItems.add(new NavItem(1, false, "Parsnip's Room", "If I'm sat here alone, I'm probably afk", "Parsnip", 0, 10));
        this.navItems.add(new NavItem(1, false, "Siract's Trophy Room", "Tribute to Siract - will be sorely missed!", "Parsnip",  0, 10));
        this.navItems.add(new NavItem(1, false, "Pea's Dutch Lounge", "Dutch themed lounge for Pea", "Parsnip", 0, 15));
        this.navItems.add(new NavItem(1, false, "Parsnip's Hallway", "", "Parsnip", 0, 25));
        this.navItems.add(new NavItem(1, false, "Animal Nitrate", "", "Parsnip",  0, 25));
    }

    private void getFavourites() {
        this.navItems = new ArrayList<NavItem>();
    }

    private void openRoom(int roomId) {
        if (Movie.getInstance().isInterfaceActive(EntryView.class)) {
            var entryView = Movie.getInstance().getInterfaceByClass(EntryView.class);
            Movie.getInstance().removeObject(this);

            entryView.transitionTo(() -> {
                Movie.getInstance().createObject(new RoomView());
                Movie.getInstance().removeObject(entryView);
            });

            Movie.getInstance().hideWidgets();
        }
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

    private class NavItem {
        int id;
        String name;
        Boolean isCategory;
        Boolean isPublic;
        int visitors;
        int maxVisitors;
        String owner;
        String description;
        String img;

        public NavItem(int id, String name, int visitors, int maxVisitors) {
            this.id = id;
            this.name = name;
            this.isCategory = true;
            this.visitors = visitors;
            this.maxVisitors = maxVisitors;
        }

        public NavItem(int id, Boolean isPublic, String name, String description, String ownerImg, int visitors, int maxVisitors) {
            this.id = id;
            this.isPublic = isPublic;
            this.name = name;
            this.isCategory = false;
            this.visitors = visitors;
            this.maxVisitors = maxVisitors;
            this.owner = isPublic ? null : ownerImg;
            this.description = description;
            this.img = !isPublic ? null : ownerImg;
        }
    }
}
