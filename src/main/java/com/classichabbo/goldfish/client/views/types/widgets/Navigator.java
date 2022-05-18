package com.classichabbo.goldfish.client.views.types.widgets;

import java.util.ArrayList;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.util.DimensionUtil;
import com.classichabbo.goldfish.client.views.controls.Button;
import com.classichabbo.goldfish.client.views.controls.ButtonLarge;
import com.classichabbo.goldfish.client.views.controls.Label;
import com.classichabbo.goldfish.client.views.controls.ScrollPane;
import com.classichabbo.goldfish.client.views.controls.TextField;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.room.RoomView;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
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

    private Pane search;
    private Label searchTitle;
    private TextField searchCriteria;
    private Button doSearchButton;
    private Label searchNoResults;

    private Label title;
    private Label hideFull;

    private Label recommendedTitle;
    private Label recommendedRefresh;
    private VBox recommendedList;
    
    private Pane info;
    private ImageView infoImg;
    private Label infoTitle;
    private Label infoSubtitle;
    private Label infoDescription;
    private Button infoLeftButton;
    private ButtonLarge infoGoButton;

    private ScrollPane navList;

    private NavItem showNavItem;
    private NavigatorPage setPage;
    private NavigatorPage currentPage;

    private ArrayList<NavItem> navItems;
    private ArrayList<NavItem> recommendedItems;

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
        privateButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.PRIVATE);
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
        this.searchButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.SEARCH);
        this.content.getChildren().addAll(this.searchButton);

        var searchLabel = new Label(TextsManager.getInstance().getString("nav_rooms_search"));
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
        this.favouritesButton.setOnMouseClicked(e -> this.setPage = NavigatorPage.FAVOURITES);
        this.content.getChildren().add(this.favouritesButton);

        var favouritesLabel = new Label(TextsManager.getInstance().getString("nav_rooms_favourite"));
        favouritesLabel.setLayoutX(24);
        favouritesLabel.setLayoutY(4);
        this.favouritesButton.getChildren().add(favouritesLabel);

        this.search = new Pane();
        this.search.setPrefSize(328, 30);
        this.search.setLayoutX(6);
        this.search.setLayoutY(89);
        this.search.setVisible(false);
        this.content.getChildren().add(this.search);
        
        this.searchTitle = new Label(TextsManager.getInstance().getString("nav_search_hd"));
        this.searchTitle.setLayoutX(7);
        this.search.getChildren().add(this.searchTitle);

        this.searchCriteria = new TextField("");
        this.searchCriteria.setWidth(250);
        this.searchCriteria.setLayoutY(13);
        this.search.getChildren().add(this.searchCriteria);

        this.doSearchButton = new Button(TextsManager.getInstance().getString("nav_searchbutton"));
        this.doSearchButton.setLayoutY(12);
        this.doSearchButton.setOnMouseClicked(e -> this.getSearch(this.searchCriteria.getText()));
        this.search.getChildren().add(this.doSearchButton);

        this.searchNoResults = new Label(TextsManager.getInstance().getString("nav_prvrooms_notfound"));
        this.searchNoResults.setLayoutX(13);
        this.searchNoResults.setLayoutY(152);
        this.searchNoResults.setVisible(false);
        this.content.getChildren().add(this.searchNoResults);

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

        this.info = new Pane();
        this.info.setPrefSize(340, 100);
        this.info.setLayoutX(1);
        this.info.setLayoutY(312);
        this.info.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_background.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.content.getChildren().add(this.info);

        this.infoImg = new ImageView();
        this.info.getChildren().add(this.infoImg);

        this.infoTitle = new Label(TextsManager.getInstance().getString("nav_public_helptext_hd"), true);
        this.infoTitle.setLayoutX(85);
        this.infoTitle.setLayoutY(12);
        this.info.getChildren().add(this.infoTitle);

        this.infoSubtitle = new Label("", true);
        this.infoSubtitle.setLayoutX(85);
        this.infoSubtitle.setLayoutY(22);
        this.info.getChildren().add(this.infoSubtitle);

        this.infoDescription = new Label(TextsManager.getInstance().getString("nav_public_helptext"));
        this.infoDescription.setLayoutX(85);
        this.infoDescription.setLayoutY(24);
        this.infoDescription.setMaxWidth(230);
        this.infoDescription.setWrapText(true);
        this.infoDescription.setLineSpacing(-2);
        this.info.getChildren().add(this.infoDescription);

        this.infoLeftButton = new Button("");
        this.infoLeftButton.setLayoutX(114);
        this.infoLeftButton.setLayoutY(74);
        this.infoLeftButton.setVisible(false);
        this.info.getChildren().add(this.infoLeftButton);

        this.infoGoButton = new ButtonLarge(TextsManager.getInstance().getString("nav_gobutton"));
        this.infoGoButton.setLayoutX(280);
        this.infoGoButton.setLayoutY(71);
        this.infoGoButton.setCursor(Cursor.HAND);
        this.infoGoButton.setVisible(false);
        this.info.getChildren().add(this.infoGoButton);

        var infoClose = new Pane();
        infoClose.setPrefSize(13, 13);
        infoClose.setCursor(Cursor.HAND);
        infoClose.setLayoutX(323);
        infoClose.setLayoutY(13);
        infoClose.setOnMouseClicked(e -> this.closeBottom = true);
        this.info.getChildren().add(infoClose);
        
        this.setPage = NavigatorPage.PUBLIC;
        this.closeBottom = false;

        this.setPadding(9, 11, 11, 10);
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

        if (this.setPage == NavigatorPage.PUBLIC) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getCategory(1); // TODO - where does the root category for public rooms come from?
            this.title.setLayoutY(64);
            this.hideFull.setLayoutY(64);
            this.hideFull.setVisible(true);
            
            this.searchButton.setVisible(false);
            this.ownButton.setVisible(false);
            this.favouritesButton.setVisible(false);

            this.navList.setSize(330, 232);
            this.navList.setLayoutY(80);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_public_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_public_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_public.png"));
        }
        else if (this.setPage != null) {
            this.searchButton.setVisible(true);
            this.ownButton.setVisible(true);
            this.favouritesButton.setVisible(true);
        }
        
        if (this.setPage == NavigatorPage.PRIVATE) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getCategory(2); // TODO - same as above
            this.getRecommendedItems();
            this.title.setLayoutY(165);
            this.hideFull.setLayoutY(165);
            this.hideFull.setVisible(true);
            
            this.recommendedTitle.setVisible(true);
            this.recommendedRefresh.setVisible(true);
            this.recommendedList.setVisible(true);

            this.navList.setSize(330, 126);
            this.navList.setLayoutY(186);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd_main"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_private_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_private.png"));
        }
        else if (this.setPage != null) {
            this.recommendedTitle.setVisible(false);
            this.recommendedRefresh.setVisible(false);
            this.recommendedList.setVisible(false);
        }

        if (this.setPage == NavigatorPage.SEARCH) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getSearch(null);
            this.title.setText(TextsManager.getInstance().getString("nav_src_hd"));
            this.title.setLayoutY(135);
            this.hideFull.setVisible(false);

            this.search.setVisible(true);
            this.searchCriteria.setText("");
            this.doSearchButton.setLayoutX(328 - this.doSearchButton.getWidth());

            this.navList.setSize(330, 162);
            this.navList.setLayoutY(150);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_search_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_search.png"));
        }
        else if (this.setPage != null) {
            this.search.setVisible(false);
            this.searchNoResults.setVisible(false);
        }

        if (this.setPage == NavigatorPage.OWN) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getOwn();
            this.title.setText(TextsManager.getInstance().getString("nav_own_hd"));
            this.title.setLayoutY(135);
            this.hideFull.setVisible(false);

            this.navList.setSize(330, 162);
            this.navList.setLayoutY(150);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_ownrooms_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_own.png"));
        }

        if (this.setPage == NavigatorPage.FAVOURITES) {
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.getFavourites();
            this.title.setText(TextsManager.getInstance().getString("nav_fav_hd"));
            this.title.setLayoutY(90);
            this.hideFull.setVisible(false);

            this.navList.setSize(330, 207);
            this.navList.setLayoutY(105);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_favourites_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_favourites.png"));
        }

        if (this.setPage != null) {
            this.info.setVisible(true);
            this.infoImg.setTranslateX(0);
            this.infoImg.setTranslateY(0);
            this.infoSubtitle.setText("");
            this.infoDescription.setTranslateY(0);
            this.infoLeftButton.setVisible(false);
            this.infoGoButton.setVisible(false);
            this.currentPage = this.setPage;
            this.setPage = null;
        }

        if (this.showNavItem != null) {
            if (this.showNavItem.isPublic) {
                this.infoImg.setTranslateX(16);
                this.infoImg.setTranslateY(15);
                this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/public_placeholder.png")); // TODO get this
                this.infoTitle.setText(this.showNavItem.name + " (" + this.showNavItem.visitors + "/" + this.showNavItem.maxVisitors + ")");
                this.infoSubtitle.setText("");
                this.infoDescription.setTranslateY(0);
            }
            else {
                this.infoImg.setTranslateX(0);
                this.infoImg.setTranslateY(0);
                this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_door_" + getBackgroundByDoor(this.showNavItem) + ".png"));
                this.infoTitle.setText(this.showNavItem.name);
                this.infoSubtitle.setText("(" + this.showNavItem.visitors + "/" + this.showNavItem.maxVisitors + ") " + TextsManager.getInstance().getString("room_owner") + " " + this.showNavItem.owner);
                this.infoDescription.setTranslateY(10);
            }

            this.infoDescription.setText(this.showNavItem.description);

            var id = this.showNavItem.id;

            if (this.currentPage == NavigatorPage.OWN) {
                this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_modify"));
                this.infoLeftButton.setTranslateX(0);
                // TODO this will be handled in UI
            }
            else if (this.currentPage == NavigatorPage.FAVOURITES) {
                this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_removefavourites"));
                this.infoLeftButton.setOnMouseClicked(e1 -> this.removeFromFavourites(id));
                this.infoLeftButton.setTranslateX(-30);
            }
            else {
                this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_addtofavourites"));
                this.infoLeftButton.setOnMouseClicked(e1 -> this.addToFavourites(id));
                this.infoLeftButton.setTranslateX(0);
            }

            this.infoGoButton.setOnMouseClicked(e1 -> openRoom(id));
            this.infoLeftButton.setVisible(true);
            this.infoGoButton.setVisible(true);
            this.info.setVisible(true);

            this.showNavItem = null;
        }

        if (this.closeBottom) {
            var newHeight = this.navList.getPrefHeight() + 96;
            this.navList.setSize(330, (int)newHeight);
            this.info.setVisible(false);

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

    private String getBackgroundByDoor(NavItem navItem) {
        if (navItem.door == null || navItem.door == Door.OPEN) {
            return "open";
        } else if (navItem.door == Door.DOORBELL) {
            return "doorbell";
        } else if (navItem.door == Door.PASSWORD) {
            return "password";
        }

        return "";
    }

    private Pane generateNavListItem(NavItem navItem) {
        var navListItem = new Pane();
        navListItem.setMinHeight(16);
        navListItem.setMaxWidth(311);

        var percentageFull = (1.0 / navItem.maxVisitors) * navItem.visitors;
        var background = "";

        if (percentageFull == 0) {
            background = "0";
        } else if (percentageFull < 0.34) {
            background = "lt_34";
        } else if (percentageFull < 0.76) {
            background = "lt_76";
        } else if (percentageFull < 1) {
            background = "lt_100";
        } else if (percentageFull == 1) {
            background = "100";
        }

        if (navItem.isCategory) {
            var nameLabel = new Label(navItem.name);
            nameLabel.setLayoutX(17);
            nameLabel.setLayoutY(2);
            nameLabel.setTextFill(percentageFull == 1 ? Color.web("#D47979") : Color.BLACK); // TODO does this actually change color? Can't find screenshots

            var openLabel = new Label(TextsManager.getInstance().getString("nav_openbutton"));
            openLabel.setLayoutX(233);
            openLabel.setLayoutY(2);
            openLabel.setAlignment(Pos.TOP_RIGHT);
            openLabel.setMinWidth(48);
            openLabel.setUnderline(true);
            openLabel.setTextFill(percentageFull == 1 ? Color.web("#D47979") : Color.BLACK);

            navListItem.getChildren().addAll(nameLabel, openLabel);
            navListItem.setCursor(Cursor.HAND);
            navListItem.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/category_" + background +".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        }
        else {
            var nameButton = new Pane();
            nameButton.setPrefSize(251, 16);
            nameButton.setCursor(Cursor.HAND);
            nameButton.setOnMouseClicked(e -> this.showNavItem = navItem);
            nameButton.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/room_" + getBackgroundByDoor(navItem) + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            
            var nameLabel = new Label(navItem.name);
            nameLabel.setLayoutX(17);
            nameLabel.setLayoutY(2);
            nameButton.getChildren().add(nameLabel);

            var goButton = new Pane();
            goButton.setPrefSize(58, 16);
            goButton.setLayoutX(253);
            goButton.setCursor(Cursor.HAND);
            goButton.setOnMouseClicked(e -> openRoom(navItem.id));
            goButton.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/go_" + background + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

            var goLabel = new Label(TextsManager.getInstance().getString(percentageFull == 1 ? "nav_fullbutton" : "nav_gobutton"));
            goLabel.setLayoutX(18);
            goLabel.setLayoutY(2);
            goLabel.setAlignment(Pos.TOP_RIGHT);
            goLabel.setMinWidth(24);
            goLabel.setUnderline(true);
            goLabel.setTextFill(percentageFull == 1 ? Color.web("#D47979") : Color.BLACK);
            goButton.getChildren().add(goLabel);

            navListItem.getChildren().addAll(nameButton, goButton);
        }

        return navListItem;
    }

    private void getRecommendedItems () {
        this.recommendedItems = new ArrayList<NavItem>();

        this.recommendedItems.add(new NavItem(1, "Hall", "New? Lost? Get a warm welcome here!", "C-3", 0, 40, Door.OPEN));
        this.recommendedItems.add(new NavItem(1, "Tresor", "Visit the park and the infamous Infobus", "zidro", 0, 65, Door.DOORBELL));
        this.recommendedItems.add(new NavItem(1, "Box ( Habbo.nl - 2007 )", "Splish, splash and have a bash in the famous Habbo pool!", "Miquel", 0, 120, Door.PASSWORD));
    }

    private void getCategory(int categoryId) {
        this.navItems = new ArrayList<NavItem>();

        if (categoryId == 1) {
            this.title.setText("Public Rooms");
            this.navItems.add(new NavItem(1, "Welcome Lounge", "New? Lost? Get a warm welcome here!", "", 1, 40));
            this.navItems.add(new NavItem(1, "The Park", "Visit the park and the infamous Infobus", "", 40, 65));
            this.navItems.add(new NavItem(1, "Habbo Lido", "Splish, splash and have a bash in the famous Habbo pool!", "", 115, 120));
            this.navItems.add(new NavItem(1, "Rooftop Rumble", "Wobble Squabble your bum off in our cool rooftop hang out", "", 50, 50));
            this.navItems.add(new NavItem(1, "Entertainment", 0, 100));
            this.navItems.add(new NavItem(1, "Restaurants and Cafes", 1, 100));
            this.navItems.add(new NavItem(1, "Lounges and Clubs", 40, 100));
            this.navItems.add(new NavItem(1, "Habbo Club", 77, 100));
            this.navItems.add(new NavItem(1, "Outside Spaces", 98, 100));
            this.navItems.add(new NavItem(1, "The Lobbies", 100, 100));
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
        if (criteria != null) {
            // do search here
            System.out.println("Search for '" + criteria + "'");
            if (navItems.isEmpty()) {
                this.searchNoResults.setVisible(true);
            }
        }
    }

    private void getOwn() {        
        this.navItems = new ArrayList<NavItem>();

        this.navItems.add(new NavItem(1, "Parsnip's Casino", "Large bets welcomed, games 13/21/poker", "Parsnip", 0, 15, Door.OPEN));
        this.navItems.add(new NavItem(1, "Parsnip's Hub", "Sit and chat or go through the teles to see some of my favourite rooms", "Parsnip", 0, 25, Door.OPEN));
        this.navItems.add(new NavItem(1, "Parsnip's Room", "If I'm sat here alone, I'm probably afk", "Parsnip", 0, 10, Door.OPEN));
        this.navItems.add(new NavItem(1, "Siract's Trophy Room", "Tribute to Siract - will be sorely missed!", "Parsnip",  0, 10, Door.OPEN));
        this.navItems.add(new NavItem(1, "Pea's Dutch Lounge", "Dutch themed lounge for Pea", "Parsnip", 0, 15, Door.OPEN));
        this.navItems.add(new NavItem(1, "Parsnip's Hallway", "", "Parsnip", 0, 25, Door.OPEN));
        this.navItems.add(new NavItem(1, "Animal Nitrate", "", "Parsnip",  0, 25, Door.OPEN));
    }

    private void getFavourites() {
        this.navItems = new ArrayList<NavItem>();

        this.navItems.add(new NavItem(1, "Parsnip's Casino", "Large bets welcomed, games 13/21/poker", "Parsnip", 0, 15, Door.OPEN));
    }

    private void openRoom(int roomId) {
        System.out.println("Go to " + roomId);

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

    private void addToFavourites(int roomId) { 
        System.out.println("Add " + roomId + " to favourites");
    }
    
    private void removeFromFavourites(int roomId) { 
        System.out.println("Remove " + roomId + " from favourites");
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

    private enum Door {
        OPEN,
        DOORBELL,
        PASSWORD
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
        Door door;

        public NavItem(int id, String name, int visitors, int maxVisitors) {
            this.id = id;
            this.name = name;
            this.isCategory = true;
            this.visitors = visitors;
            this.maxVisitors = maxVisitors;
        }

        public NavItem(int id, String name, String description, String img, int visitors, int maxVisitors) {
            this.id = id;
            this.isPublic = true;
            this.name = name;
            this.isCategory = false;
            this.visitors = visitors;
            this.maxVisitors = maxVisitors;
            this.description = description;
            this.img = img;
        }

        public NavItem(int id, String name, String description, String owner, int visitors, int maxVisitors, Door door) {
            this.id = id;
            this.isPublic = false;
            this.name = name;
            this.isCategory = false;
            this.visitors = visitors;
            this.maxVisitors = maxVisitors;
            this.owner = owner;
            this.description = description;
            this.door = door;
        }
    }
}
