package com.classichabbo.goldfish.client.views.types.widgets.navigator;

import java.util.ArrayList;
import java.util.Collections;

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
import com.classichabbo.goldfish.client.views.types.room.RoomTransition;
import com.classichabbo.goldfish.client.views.types.room.RoomView;
import com.classichabbo.goldfish.client.views.types.toolbars.RoomToolbar;
import com.classichabbo.goldfish.client.views.types.widgets.Widget;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NavigatorView extends Widget {
    private Pane content;
    
    private Pane searchButton;
    private Pane ownButton;
    private Pane favouritesButton;

    private Pane search;
    private Label searchTitle;
    private TextField searchCriteria;
    private Button doSearchButton;
    private Label searchNoResults;

    private Pane backTop;
    private Label backTopLabel;

    private Label title;
    private Label hideFull;

    private Label recommendedTitle;
    private Label recommendedRefresh;
    private VBox recommendedList;
    
    private ScrollPane navigatorList;
    
    private Pane info;
    private ImageView infoImg;
    private Label infoTitle;
    private Label infoSubtitle;
    private Label infoDescription;
    private Button infoLeftButton;
    private ButtonLarge infoGoButton;

    private int publicCategoryId;
    private int privateCategoryId;

    private boolean inRoom;
    private NavigatorPage currentPage;
    private Category currentCategory;
    private Runnable pendingAction;

    @Override
    public void start() {
        super.start();
        
        // TODO Avery - I'm not sure where you get these from
        this.publicCategoryId = 1;
        this.privateCategoryId = 2;
        this.inRoom = false;
        
        this.init();
        this.setPadding(9, 11, 10, 10);
        this.setTitle(TextsManager.getInstance().getString("navigator"));
        this.setContent(this.content);
        this.setLocation();
        this.setPage(NavigatorPage.PUBLIC);

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

        if (this.pendingAction != null) {
            this.pendingAction.run();
            this.pendingAction = null;
        }

        var inRoom = Movie.getInstance().getViewByClass(RoomView.class) != null;

        if (this.inRoom != inRoom) {
            this.inRoom = inRoom;
            this.updateBackButtons();
        }

        this.navigatorList.update();
    }

    @Override
    protected void closeButtonClicked() {
        this.setHidden(true);
    }

    private void init() {
        this.content = new Pane();
        this.content.setPrefSize(342, 414);
        this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        var publicButton = new Pane();
        publicButton.setPrefSize(170, 56);
        publicButton.setCursor(Cursor.HAND);
        publicButton.setLayoutX(1);
        publicButton.setLayoutY(1);
        publicButton.setOnMouseClicked(e -> this.pendingAction = () -> this.setPage(NavigatorPage.PUBLIC));
        this.content.getChildren().add(publicButton);

        var publicLabel = new Label(TextsManager.getInstance().getString("nav_publicRooms"), true);
        publicLabel.setLayoutX(64);
        publicLabel.setLayoutY(10);
        publicButton.getChildren().add(publicLabel);

        var privateButton = new Pane();
        privateButton.setPrefSize(169, 56);
        privateButton.setCursor(Cursor.HAND);
        privateButton.setLayoutX(172);
        privateButton.setLayoutY(1);
        privateButton.setOnMouseClicked(e -> this.pendingAction = () -> this.setPage(NavigatorPage.PRIVATE));
        this.content.getChildren().add(privateButton);

        var privateLabel = new Label(TextsManager.getInstance().getString("nav_privateRooms"), true);
        privateLabel.setLayoutX(63);
        privateLabel.setLayoutY(10);
        privateButton.getChildren().add(privateLabel);

        this.searchButton = new Pane();
        this.searchButton.setPrefSize(113, 22);
        this.searchButton.setCursor(Cursor.HAND);
        this.searchButton.setVisible(false);
        this.searchButton.setLayoutX(1);
        this.searchButton.setLayoutY(58);
        this.searchButton.setOnMouseClicked(e -> this.pendingAction = () -> this.setPage(NavigatorPage.SEARCH));
        this.content.getChildren().addAll(this.searchButton);

        var searchLabel = new Label(TextsManager.getInstance().getString("nav_rooms_search"));
        searchLabel.setLayoutX(23);
        searchLabel.setLayoutY(4);
        this.searchButton.getChildren().add(searchLabel);

        this.ownButton = new Pane();
        this.ownButton.setPrefSize(113, 22);
        this.ownButton.setCursor(Cursor.HAND);
        this.ownButton.setVisible(false);
        this.ownButton.setLayoutX(115);
        this.ownButton.setLayoutY(58);
        this.ownButton.setOnMouseClicked(e -> this.pendingAction = () -> this.setPage(NavigatorPage.OWN));
        this.content.getChildren().add(this.ownButton);

        var ownLabel = new Label(TextsManager.getInstance().getString("nav_rooms_own"));
        ownLabel.setLayoutX(26);
        ownLabel.setLayoutY(4);
        this.ownButton.getChildren().add(ownLabel);

        this.favouritesButton = new Pane();
        this.favouritesButton.setPrefSize(112, 22);
        this.favouritesButton.setCursor(Cursor.HAND);
        this.favouritesButton.setVisible(false);
        this.favouritesButton.setLayoutX(229);
        this.favouritesButton.setLayoutY(58);
        this.favouritesButton.setOnMouseClicked(e -> this.pendingAction = () -> this.setPage(NavigatorPage.FAVOURITES));
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
        this.doSearchButton.setOnMouseClicked(e -> this.pendingAction = () -> this.showSearchResults());
        this.search.getChildren().add(this.doSearchButton);

        this.searchNoResults = new Label(TextsManager.getInstance().getString("nav_prvrooms_notfound"));
        this.searchNoResults.setLayoutX(13);
        this.searchNoResults.setLayoutY(152);
        this.searchNoResults.setVisible(false);
        this.content.getChildren().add(this.searchNoResults);

        this.backTop = new Pane();
        this.backTop.setPrefWidth(340);
        this.backTop.setLayoutX(1);
        this.backTop.setVisible(false);
        this.backTop.setPickOnBounds(false);
        this.backTop.setCursor(Cursor.HAND);
        this.content.getChildren().add(backTop);

        this.backTopLabel = new Label("", true, "#336666");
        this.backTopLabel.setLayoutX(39);
        this.backTop.getChildren().add(backTopLabel);

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
        this.recommendedTitle.setLayoutY(83);
        this.content.getChildren().add(this.recommendedTitle);

        this.recommendedRefresh = new Label(TextsManager.getInstance().getString("nav_refresh_recoms"), "#7B9498");
        this.recommendedRefresh.setLayoutX(185);
        this.recommendedRefresh.setLayoutY(83);
        this.recommendedRefresh.setMinWidth(150);
        this.recommendedRefresh.setAlignment(Pos.TOP_RIGHT);
        this.recommendedRefresh.setUnderline(true);
        this.recommendedRefresh.setCursor(Cursor.HAND);
        this.content.getChildren().add(recommendedRefresh);

        this.recommendedList = new VBox(2);
        this.recommendedList.setLayoutX(5);
        this.recommendedList.setLayoutY(101);
        this.recommendedList.setMinWidth(311);
        this.content.getChildren().add(recommendedList);

        this.navigatorList = new ScrollPane();
        this.navigatorList.setSpacing(2);
        this.navigatorList.setLayoutX(5);
        this.content.getChildren().add(this.navigatorList);

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
        infoClose.setOnMouseClicked(e -> this.pendingAction = () -> this.infoHide());
        this.info.getChildren().add(infoClose);
    }

    private void setLocation() {
        // getWidth() and getHeight() no longer zero when wrapping around this shit - when the navigator finished sizing itself
        // make it appear to the side first and foremost :^)
        this.setCallAfterFinish(() -> {
            this.setLayoutX(DimensionUtil.roundEven((DimensionUtil.getProgramWidth() - this.getWidth()) * 0.95));
            this.setLayoutY(20);
        });
    }

    private void setPage(NavigatorPage page) {
        if (page == NavigatorPage.PUBLIC) {
            this.currentPage = NavigatorPage.PUBLIC;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setLayoutY(62);
            this.hideFull.setLayoutY(62);
            this.hideFull.setVisible(true);
            
            this.searchButton.setVisible(false);
            this.ownButton.setVisible(false);
            this.favouritesButton.setVisible(false);

            this.navigatorList.setSize(330, 232);
            this.navigatorList.setLayoutY(80);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_public_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_public_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_public.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_addtofavourites"));
            this.infoLeftButton.setTranslateX(0);

            this.showCategory(this.publicCategoryId);
        }
        else {
            this.searchButton.setVisible(true);
            this.ownButton.setVisible(true);
            this.favouritesButton.setVisible(true);
        }
        
        if (page == NavigatorPage.PRIVATE) {
            this.currentPage = NavigatorPage.PRIVATE;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setLayoutY(163);
            this.hideFull.setLayoutY(163);
            this.hideFull.setVisible(true);
            
            this.recommendedTitle.setVisible(true);
            this.recommendedRefresh.setVisible(true);
            this.recommendedList.setVisible(true);

            this.navigatorList.setSize(330, 126);
            this.navigatorList.setLayoutY(180);
            this.navigatorList.setPadding(1, 1);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd_main"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_private_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_private.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_addtofavourites"));
            this.infoLeftButton.setTranslateX(0);

            this.showCategory(this.privateCategoryId);
            this.showRecommendedRooms();
        }
        else {
            this.recommendedTitle.setVisible(false);
            this.recommendedRefresh.setVisible(false);
            this.recommendedList.setVisible(false);

            this.navigatorList.setPadding(0, 0);
        }

        if (page == NavigatorPage.SEARCH) {
            this.currentPage = NavigatorPage.SEARCH;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_src_hd"));
            this.title.setLayoutY(135);
            this.hideFull.setVisible(false);

            this.search.setVisible(true);
            this.searchCriteria.setText("");
            this.doSearchButton.setLayoutX(328 - this.doSearchButton.getWidth());

            this.navigatorList.setSize(330, 162);
            this.navigatorList.setLayoutY(150);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_search_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_search.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_addtofavourites"));
            this.infoLeftButton.setTranslateX(0);

            this.updateBackButtons();
            this.navigatorList.clearContent();
        }
        else {
            this.search.setVisible(false);
            this.searchNoResults.setVisible(false);
        }

        if (page == NavigatorPage.OWN) {
            this.currentPage = NavigatorPage.OWN;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_own_hd"));
            this.title.setLayoutY(135);
            this.hideFull.setVisible(false);

            this.navigatorList.setSize(330, 162);
            this.navigatorList.setLayoutY(150);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_ownrooms_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_own.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_modify"));
            this.infoLeftButton.setTranslateX(0);

            this.updateBackButtons();
            this.showOwnRooms();
        }

        if (page == NavigatorPage.FAVOURITES) {
            this.currentPage = NavigatorPage.FAVOURITES;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_fav_hd"));
            this.title.setLayoutY(90);
            this.hideFull.setVisible(false);

            this.navigatorList.setSize(330, 207);
            this.navigatorList.setLayoutY(105);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_favourites_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_favourites.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_removefavourites"));
            this.infoLeftButton.setTranslateX(-30);

            this.updateBackButtons();
            this.showFavouriteRooms();
        }

        this.info.setVisible(true);
        this.infoImg.setTranslateX(0);
        this.infoImg.setTranslateY(0);
        this.infoSubtitle.setText("");
        this.infoDescription.setTranslateY(0);
        this.infoLeftButton.setVisible(false);
        this.infoGoButton.setVisible(false);
    }

    private void backTopShow(String text, EventHandler<MouseEvent> event) {
        if (this.currentPage == NavigatorPage.PUBLIC) {
            this.backTop.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/back_top_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.backTop.setPrefHeight(22);
            this.backTop.setLayoutY(58);
            this.backTopLabel.setLayoutY(3);
            this.backTop.setVisible(true);
        }
        if (this.currentPage == NavigatorPage.PRIVATE) {
            this.backTop.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/back_top_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.backTop.setPrefHeight(27);
            this.backTop.setLayoutY(76);
            this.backTopLabel.setLayoutY(8);
        }

        this.backTopLabel.setText(text);
        this.backTop.setOnMouseClicked(event);
        this.backTop.setVisible(true);
    }

    private void infoShowRoom(Room room) {
        if (room.isPublic) {
            this.infoImg.setTranslateX(16);
            this.infoImg.setTranslateY(15);
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/public_placeholder.png")); // TODO Avery - get Public Room preview image
            this.infoTitle.setText(room.name + " (" + room.visitors + "/" + room.maxVisitors + ")");
            this.infoSubtitle.setText("");
            this.infoDescription.setTranslateY(0);
        }
        else {
            this.infoImg.setTranslateX(0);
            this.infoImg.setTranslateY(0);
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("sprites/interfaces/navigator/info_doorbell_" + getBackgroundByDoorbell(room.doorbell) + ".png"));
            this.infoTitle.setText(room.name);
            this.infoSubtitle.setText("(" + room.visitors + "/" + room.maxVisitors + ") " + TextsManager.getInstance().getString("room_owner") + " " + room.owner);
            this.infoDescription.setTranslateY(10);
        }

        this.infoDescription.setText(room.description);

        if (this.currentPage == NavigatorPage.OWN) {
            // TODO Parsnip - room settings UI
        }
        else if (this.currentPage == NavigatorPage.FAVOURITES) {
            this.infoLeftButton.setOnMouseClicked(e1 -> this.pendingAction = () -> this.removeFromFavourites(room.roomId));
        }
        else {
            this.infoLeftButton.setOnMouseClicked(e1 -> this.pendingAction = () -> this.addToFavourites(room.roomId));
        }

        this.infoGoButton.setOnMouseClicked(e1 -> this.pendingAction = () -> this.goToRoom(room.roomId));
        this.infoLeftButton.setVisible(true);
        this.infoGoButton.setVisible(true);
        this.info.setVisible(true);
    }

    private void infoHide() {
        this.navigatorList.setSize(330, (int)this.navigatorList.getPrefHeight() + 96);
        this.info.setVisible(false);
    }

    private void showCategory(int categoryId) {
        this.navigatorList.clearContent();
        
        this.currentCategory = this.getCategory(categoryId);
        this.updateBackButtons();
        this.title.setText(this.currentCategory.name);

        for (var room : this.currentCategory.rooms) {
            this.addRoom(room, false);
        }
        
        for (var childCategory : this.currentCategory.categories) {
            this.addCategory(childCategory);
        }
    }

    private void updateBackButtons() {
        this.content.getChildren().removeIf(NavigatorBackButton.class::isInstance);

        if (this.currentPage == NavigatorPage.SEARCH 
            || this.currentPage == NavigatorPage.OWN
            || this.currentPage == NavigatorPage.FAVOURITES) {
            this.resetBackButtonTranslations();
            return;
        }

        var index = 0;

        if (this.inRoom) {
            this.backTopShow(TextsManager.getInstance().getString("nav_hotelview"), e -> this.pendingAction = () -> {
                var roomToolbar = Movie.getInstance().getViewByClass(RoomToolbar.class);

                if (roomToolbar != null) {
                    roomToolbar.goToHotelView();
                }
            });
            index++;
        }

        var backCategories = new ArrayList<Category>();
        var currentCategory = this.currentCategory;
        while (currentCategory.parentCategory != null) {
            backCategories.add(currentCategory);
            currentCategory = currentCategory.parentCategory;
        }

        var startY = this.currentPage == NavigatorPage.PUBLIC ? 40 : 63;
        Collections.reverse(backCategories);

        for (var category : backCategories) {
            if (index == 0) {
                this.backTopShow(category.parentCategory.name, e -> this.pendingAction = () -> this.showCategory(category.parentCategory.categoryId));
            }
            else {
                content.getChildren().add(new NavigatorBackButton(category.parentCategory.name, startY, index, e -> this.pendingAction = () -> this.showCategory(category.parentCategory.categoryId)));
            }
            index++;
        }

        if (this.currentPage == NavigatorPage.PUBLIC) {
            this.title.setTranslateY(18 * index);
            this.hideFull.setTranslateY(18 * index);

            this.navigatorList.setHeightReduction(18 * index);
            this.navigatorList.setTranslateY(18 * index);
        }

        if (this.currentPage == NavigatorPage.PRIVATE) {
            this.title.setTranslateY((25 + (18 * (index - 1))) - (!backCategories.isEmpty() ? 80 : 0));
            this.hideFull.setTranslateY((25 + (18 * (index - 1))) - (!backCategories.isEmpty() ? 80 : 0));
            
            this.recommendedTitle.setTranslateY(25);
            this.recommendedRefresh.setTranslateY(25);
            this.recommendedList.setTranslateY(25);

            this.navigatorList.setHeightReduction((25 + (18 * (index - 1))) - (!backCategories.isEmpty() ? 80 : 0));
            this.navigatorList.setTranslateY((25 + (18 * (index - 1))) - (!backCategories.isEmpty() ? 80 : 0));
        }

        if (backCategories.isEmpty() && !this.inRoom) {
            this.resetBackButtonTranslations();
        }

        this.recommendedTitle.setVisible(backCategories.isEmpty() && this.currentPage == NavigatorPage.PRIVATE);
        this.recommendedRefresh.setVisible(backCategories.isEmpty() && this.currentPage == NavigatorPage.PRIVATE);
        this.recommendedList.setVisible(backCategories.isEmpty() && this.currentPage == NavigatorPage.PRIVATE);
    }

    private void resetBackButtonTranslations() {
        this.title.setTranslateY(0);
        this.hideFull.setTranslateY(0);
        
        this.recommendedTitle.setTranslateY(0);
        this.recommendedRefresh.setTranslateY(0);
        this.recommendedList.setTranslateY(0);

        this.navigatorList.setHeightReduction(0);
        this.navigatorList.setTranslateY(0);

        this.backTop.setVisible(false);
    }

    private void showRecommendedRooms() {
        this.recommendedList.getChildren().clear();

        var recommendedRooms = this.getRecommendedRooms();

        for (var room : recommendedRooms) {
            this.addRoom(room, true);
        }
    }

    private void showSearchResults() {
        this.navigatorList.clearContent();

        var searchResults = this.getSearchResults(this.searchCriteria.getText());

        if (searchResults.isEmpty()) {
            this.searchNoResults.setVisible(true);
            return;
        }

        for (var searchResult : searchResults) {
            this.addRoom(searchResult, false);
        }
    }

    private void showOwnRooms() {
        this.navigatorList.clearContent();

        var ownRooms = this.getOwnRooms();

        for (var room : ownRooms) {
            this.addRoom(room, false);
        }
    }
    
    private void showFavouriteRooms() {
        this.navigatorList.clearContent();

        var favouriteRooms = this.getFavouriteRooms();

        for (var room : favouriteRooms) {
            this.addRoom(room, false);
        }
    }

    private void addRoom(Room room, Boolean recommended) {
        var navigatorItem = new NavigatorItem(room);
        navigatorItem.setNameButtonOnMouseClicked(e -> this.pendingAction = () -> this.infoShowRoom(room));
        navigatorItem.setGoButtonOnMouseClicked(e -> this.pendingAction = () -> this.goToRoom(room.roomId));

        if (recommended) {
            this.recommendedList.getChildren().add(navigatorItem);
        }
        else {
            this.navigatorList.addContent(navigatorItem);
        }
    }

    private void addCategory(Category category) {
        var navigatorItem = new NavigatorItem(category);
        navigatorItem.setOnMouseClicked(e -> this.pendingAction = () -> this.showCategory(category.categoryId)); // TODO Parsnip - open categories

        this.navigatorList.addContent(navigatorItem);
    }

    public static String getBackgroundByDoorbell(Doorbell doorbell) {
        if (doorbell == Doorbell.OPEN || doorbell == null) {
            return "open";
        } else if (doorbell == Doorbell.RING) {
            return "ring";
        } else if (doorbell == Doorbell.PASSWORD) {
            return "password";
        }

        return "";
    }

    // TODO Avery - all the below methods are your entry points to do whatever with :)

    private Category getCategory(int categoryId) {
        // Please see note in Category.java :)
        Category category = null;

        if (categoryId == 1) {
            category = new Category(1, "Public Rooms");
            category.addRoom(new Room(1, "Welcome Lounge", "New? Lost? Get a warm welcome here!", "", 1, 40));
            category.addRoom(new Room(1, "The Park", "Visit the park and the infamous Infobus", "", 40, 65));
            category.addRoom(new Room(1, "Habbo Lido", "Splish, splash and have a bash in the famous Habbo pool!", "", 115, 120));
            category.addRoom(new Room(1, "Rooftop Rumble", "Wobble Squabble your bum off in our cool rooftop hang out", "", 50, 50));
            category.addCategory(new Category(3, "Entertainment", 0, 100));
            category.addCategory(new Category(3, "Restaurants and Cafes", 0, 100));
            category.addCategory(new Category(3, "Lounges and Clubs", 0, 100));
            category.addCategory(new Category(3, "Habbo Club", 0, 100));
            category.addCategory(new Category(3, "Outside Spaces", 0, 100));
            category.addCategory(new Category(3, "The Lobbies", 0, 100));
            category.addCategory(new Category(3, "The Hallways", 0, 100));
            category.addCategory(new Category(3, "Games", 0, 100));
        }

        if (categoryId == 2) {
            category = new Category(2, "Guest Rooms");
            category.addCategory(new Category(4, "Flower Power Puzzle", 0, 100));
            category.addCategory(new Category(4, "Gaming & Race Rooms", 0, 100));
            category.addCategory(new Category(4, "Restaurant, Bar & Night Club Rooms", 0, 100));
            category.addCategory(new Category(4, "Trade Floor", 0, 100));
            category.addCategory(new Category(4, "Chill, Chat & Discussion Rooms", 0, 100));
            category.addCategory(new Category(4, "Hair Salons & Modelling Rooms", 0, 100));
            category.addCategory(new Category(4, "Maze & Theme Park Rooms", 0, 100));
            category.addCategory(new Category(4, "Help Centre Rooms", 0, 100));
            category.addCategory(new Category(4, "Miscellaneous", 0, 100));
        }

        if (categoryId == 3) {
            category = new Category(3, "Entertainent", this.getCategory(1));
            category.addCategory(new Category(7, "Secret Subcategory", 0, 100));
        }

        if (categoryId == 4) {
            category = new Category(4, "Trade Floor", this.getCategory(2));
            category.addCategory(new Category(5, "Secret Subcategory", 0, 100));
        }

        if (categoryId == 5) {
            category = new Category(5, "Secret Subcategory", this.getCategory(4));
            category.addCategory(new Category(6, "Secret Sub-Subcategory", 0, 100));
        }

        if (categoryId == 6) {
            category = new Category(6, "Secret Sub-Subcategory", this.getCategory(5));
            category.addRoom(new Room(1, "Parsnip's Casino", "Parsnip", "Large bets welcomed, games 13/21/poker", 0, 15, Doorbell.OPEN));
        }

        if (categoryId == 7) {
            category = new Category(7, "Secret Subcategory", this.getCategory(3));
            category.addCategory(new Category(8, "Secret Sub-Subcategory", 0, 100));
        }

        if (categoryId == 8) {
            category = new Category(8, "Secret Sub-Subcategory", this.getCategory(7));
            category.addRoom(new Room(1, "Theatredome", "Perform your latest master piece, or simply catch the latest gossip.", "", 1, 100));
        }

        return category;
    }

    private ArrayList<Room> getRecommendedRooms () {
        var recommendedRooms = new ArrayList<Room>();

        recommendedRooms.add(new Room(1, "Hall", "C-3", "", 0, 40, Doorbell.OPEN));
        recommendedRooms.add(new Room(1, "Tresor", "", "zidro", 0, 65, Doorbell.RING));
        recommendedRooms.add(new Room(1, "Box ( Habbo.nl - 2007 )", "Miquel", "", 0, 120, Doorbell.PASSWORD));
    
        return recommendedRooms;
    }

    private ArrayList<Room> getSearchResults(String criteria) {
        var searchResults = new ArrayList<Room>();

        if (criteria.equals("Parsnip")) {
            searchResults.add(new Room(1, "Parsnip's Casino", "Parsnip", "Large bets welcomed, games 13/21/poker", 0, 15, Doorbell.OPEN));
        }

        return searchResults;
    }

    private ArrayList<Room> getOwnRooms() {        
        var ownRooms = new ArrayList<Room>();

        ownRooms.add(new Room(1, "Parsnip's Casino", "Parsnip", "Large bets welcomed, games 13/21/poker", 0, 15, Doorbell.OPEN));
        ownRooms.add(new Room(1, "Parsnip's Hub", "Parsnip", "Sit and chat or go through the teles to see some of my favourite rooms", 0, 25, Doorbell.OPEN));
        ownRooms.add(new Room(1, "Parsnip's Room", "Parsnip", "If I'm sat here alone, I'm probably afk", 0, 10, Doorbell.OPEN));
        ownRooms.add(new Room(1, "Siract's Trophy Room", "Parsnip", "Tribute to Siract - will be sorely missed!", 0, 10, Doorbell.OPEN));
        ownRooms.add(new Room(1, "Pea's Dutch Lounge", "Parsnip", "Dutch themed lounge for Pea", 0, 15, Doorbell.OPEN));
        ownRooms.add(new Room(1, "Parsnip's Hallway", "Parsnip", "", 0, 25, Doorbell.OPEN));
        ownRooms.add(new Room(1, "Animal Nitrate", "Parsnip", "", 0, 25, Doorbell.OPEN));

        return ownRooms;
    }

    private ArrayList<Room> getFavouriteRooms() {
        var favouriteRooms = new ArrayList<Room>();

        favouriteRooms.add(new Room(1, "Parsnip's Casino", "Parsnip", "Large bets welcomed, games 13/21/poker", 0, 15, Doorbell.OPEN));
        
        return favouriteRooms;
    }

    private void goToRoom(int roomId) {
        System.out.println("Go to " + roomId);

        Platform.runLater(() -> {
            if (Movie.getInstance().isViewActive(EntryView.class)) {
                var entryView = Movie.getInstance().getViewByClass(EntryView.class);

                entryView.transitionTo(() -> {
                    Movie.getInstance().createObject(new RoomView());
                    Movie.getInstance().removeObject(entryView);
                });

                Movie.getInstance().hideWidgets();
            }
        });
    }

    private void goToHotelview() {
        Platform.runLater(() -> {
            if (Movie.getInstance().isViewActive(RoomView.class)) {
                var roomView = Movie.getInstance().getViewByClass(RoomView.class);
                Movie.getInstance().removeObject(roomView);

                Movie.getInstance().createObject(new RoomTransition(() -> {
                    Movie.getInstance().createObject(new EntryView());
                }));
            }
        });
    }

    private void addToFavourites(int roomId) { 
        System.out.println("Add " + roomId + " to favourites");
    }
    
    private void removeFromFavourites(int roomId) { 
        System.out.println("Remove " + roomId + " from favourites");
    }

    private enum NavigatorPage {
        PUBLIC,
        PRIVATE,
        SEARCH,
        OWN,
        FAVOURITES
    }
}