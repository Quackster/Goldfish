package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.AliasManager;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.game.values.types.VariablesManager;
import com.classichabbo.goldfish.client.modules.controls.BorderPane;
import com.classichabbo.goldfish.client.modules.controls.Button;
import com.classichabbo.goldfish.client.modules.controls.ButtonLarge;
import com.classichabbo.goldfish.client.modules.controls.CheckBox;
import com.classichabbo.goldfish.client.modules.controls.Dropdown;
import com.classichabbo.goldfish.client.modules.controls.ImageButton;
import com.classichabbo.goldfish.client.modules.controls.Label;
import com.classichabbo.goldfish.client.modules.controls.NumericUpDown;
import com.classichabbo.goldfish.client.modules.controls.ScrollPane;
import com.classichabbo.goldfish.client.modules.controls.TextAreaRound;
import com.classichabbo.goldfish.client.modules.controls.TextFieldRound;
import com.classichabbo.goldfish.client.modules.controls.TextFieldSquare;
import com.classichabbo.goldfish.client.modules.types.alerts.Alert;
import com.classichabbo.goldfish.client.modules.types.room.RoomView;
import com.classichabbo.goldfish.client.modules.types.widgets.Widget;

import com.classichabbo.goldfish.util.DimensionUtil;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class NavigatorView extends Widget {
    private Pane content;
    private Insets padding;

    private Pane searchButton;
    private Pane ownButton;
    private Pane favouritesButton;

    private Pane search;
    private Label searchTitle;
    private TextFieldRound searchCriteria;
    private Button doSearchButton;
    private Label noResults;

    private Pane room;
    private Label roomTitle;
    private Button roomCreateButton;
    private ImageButton roomCreateImg;

    private Pane backTop;
    private Label backTopLabel;

    private Label title;
    private Label hideFull;

    private Label recommendedTitle;
    private Label recommendedRefresh;
    private VBox recommendedList;
    
    private ScrollPane navigatorList;
    
    private Pane modifyRoomBack;
    private Label modifyRoomBackLabel;
    
    private Pane modifyRoomPage1;
    private Label modifyRoomNameLabel;
    private TextFieldRound modifyRoomName;
    private Label modifyRoomDescriptionLabel;
    private TextAreaRound modifyRoomDescription;
    private Label modifyRoomCategoryLabel;
    private Dropdown modifyRoomCategory;
    private Label modifyRoomMaxVisitorsLabel;
    private NumericUpDown modifyRoomMaxVisitors;
    private Label modifyRoomShowNameLabel;
    private CheckBox modifyRoomShowNameYes;
    private Label modifyRoomShowNameYesLabel;
    private CheckBox modifyRoomShowNameNo;
    private Label modifyRoomShowNameNoLabel;

    private Pane modifyRoomPage2;
    private Label modifyRoomDoorbellLabel;
    private CheckBox modifyRoomDoorbellOpen;
    private Label modifyRoomDoorbellOpenLabel;
    private CheckBox modifyRoomDoorbellRing;
    private Label modifyRoomDoorbellRingLabel;
    private CheckBox modifyRoomDoorbellPassword;
    private Label modifyRoomDoorbellPasswordLabel;
    private Pane modifyRoomPasswordFields;
    private TextAreaRound modifyRoomDoorbellPassword1;
    private Label modifyRoomDoorbellPassword1Label;
    private TextAreaRound modifyRoomDoorbellPassword2;
    private Label modifyRoomDoorbellPassword2Label;
    private CheckBox modifyRoomOpenRights;
    private Label modifyRoomOpenRightsLabel;
    private Button modifyRoomResetRightsButton;
    private Label modifyRoomResetRightsLabel;

    private Pane modifyRoomFooter;
    private Label modifyRoomPrevious;
    private Label modifyRoomPageLabel;
    private Label modifyRoomNext;
    private Button modifyRoomCancelButton;
    private Button modifyRoomDeleteButton;
    private ButtonLarge modifyRoomOkButton;

    private Label modifyRoomNoteHeader;
    private Label modifyRoomNote;
    private ButtonLarge modifyRoomNoteOk;

    private BorderPane resetRights;
    private Label resetRightsName;
    private Button resetRightsCancel;
    private ButtonLarge resetRightsOk;

    private BorderPane deleteRoom;
    private Label deleteRoomName;
    private Label deleteRoomDescription;
    private Button deleteRoomCancel;
    private ButtonLarge deleteRoomOk;

    private Pane info;
    private ImageView infoImg;
    private Label infoTitle;
    private Label infoSubtitle;
    private Label infoDescription;
    private Button infoLeftButton;
    private ButtonLarge infoGoButton;

    private BorderPane passwordPrompt;
    private Label passwordPromptName;
    private Label passwordPromptIsProtected;
    private TextFieldSquare passwordPromptField;
    private Button passwordPromptCancelButton;
    private ButtonLarge passwordPromptOkButton;

    private int publicCategoryId;
    private int privateCategoryId;

    private final int defaultPublicCategoryId;
    private final int defaultPrivateCategoryId;

    private boolean inRoom;
    private NavigatorPage currentPage;
    private NavigatorNode modifyRoom;
    private Runnable pendingAction;

    private NavigatorComponent component;
    private NavigatorHandler handler;
    private AliasManager aliasManager;

    private boolean isHideFull;

    private boolean hideFullRooms;

    public NavigatorView() {
        this.component = new NavigatorComponent(this);
        this.handler = new NavigatorHandler(this);

        this.aliasManager = new AliasManager();
        this.aliasManager.loadMemberAlias("assets/views/navigator/thumbnails/memberalias.index");

        this.defaultPublicCategoryId = VariablesManager.getInstance().getInt("navigator.public.default", 3);
        this.defaultPrivateCategoryId = VariablesManager.getInstance().getInt("navigator.private.default", 4);
    }

    @Override
    public void start() {
        super.start();

        this.inRoom = false;
        this.hideFullRooms = false;

        this.publicCategoryId = this.defaultPublicCategoryId;
        this.privateCategoryId = this.defaultPrivateCategoryId;
        
        this.init();
        this.padding = new Insets(9, 11, 10, 10);
        this.setTitle(TextsManager.getInstance().getString("navigator"));
        this.setContent(this.content, this.padding, true);
        this.setLocation();

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
        this.searchCriteria.update();
        this.passwordPromptField.update();
    }

    @Override
    protected void closeButtonClicked() {
        this.setHidden(true);
    }

    @Override
    public void setHidden(boolean flag) {
        if (this.currentPage == null)
            this.currentPage = NavigatorPage.PUBLIC;

        if (!flag)
            this.setPage(this.currentPage); // used to reset info box at the bottom

        super.setHidden(flag);
    }

    @Override
    public NavigatorHandler getHandler() {
        return handler;
    }

    @Override
    public NavigatorComponent getComponent() {
        return component;
    }

    private void init() {
        this.content = new Pane();
        this.content.setPrefSize(342, 414);
        this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

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

        this.searchCriteria = new TextFieldRound("");
        this.searchCriteria.setWidth(250);
        this.searchCriteria.setLayoutY(15);
        this.search.getChildren().add(this.searchCriteria);

        this.doSearchButton = new Button(TextsManager.getInstance().getString("nav_searchbutton"));
        this.doSearchButton.setLayoutY(12);
        this.doSearchButton.setOnMouseClicked(e -> this.pendingAction = () -> this.component.sendSearchFlats(this.searchCriteria.getText()));
        this.search.getChildren().add(this.doSearchButton);

        this.noResults = new Label("");
        this.noResults.setLayoutX(13);
        this.noResults.setLayoutY(152);
        this.noResults.setVisible(false);
        this.content.getChildren().add(this.noResults);

        this.room = new Pane();
        this.room.setPrefSize(340, 44);
        this.room.setLayoutX(1);
        this.room.setLayoutY(81);
        this.room.setVisible(false);
        this.content.getChildren().add(this.room);

        this.roomTitle = new Label(TextsManager.getInstance().getString("nav_createroom_hd"));
        this.roomTitle.setLayoutX(11);
        this.roomTitle.setLayoutY(8);
        this.room.getChildren().add(this.roomTitle);

        this.roomCreateButton = new Button(TextsManager.getInstance().getString("nav_createroom"));
        this.roomCreateButton.setLayoutX(5);
        this.roomCreateButton.setLayoutY(22);
        this.roomCreateButton.setOnMouseClicked(e -> this.pendingAction = () -> this.showCreateRoom());
        this.room.getChildren().add(this.roomCreateButton);

        this.roomCreateImg = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "room-create-img.png"));
        this.roomCreateImg.setLayoutX(306);
        this.roomCreateImg.setLayoutY(5);
        this.roomCreateImg.setOnMouseClicked(e -> this.pendingAction = () -> this.showCreateRoom());
        this.room.getChildren().add(this.roomCreateImg);

        this.backTop = new Pane();
        this.backTop.setPrefWidth(340);
        this.backTop.setLayoutX(1);
        this.backTop.setVisible(false);
        this.backTop.setPickOnBounds(false);
        this.backTop.setCursor(Cursor.HAND);
        this.content.getChildren().add(this.backTop);

        this.backTopLabel = new Label("", true, "#336666");
        this.backTopLabel.setLayoutX(39);
        this.backTop.getChildren().add(this.backTopLabel);

        this.title = new Label("", true);
        this.title.setLayoutX(22);
        this.content.getChildren().add(this.title);

        this.hideFull = new Label(TextsManager.getInstance().getString("nav_hidefull"), "#7B9498");
        this.hideFull.setLayoutX(235);
        this.hideFull.setMinWidth(100);
        this.hideFull.setAlignment(Pos.TOP_RIGHT);
        this.hideFull.setUnderline(true);
        this.hideFull.setCursor(Cursor.HAND);
        this.hideFull.setOnMouseClicked(e -> toggleHideFull());
        this.content.getChildren().add(this.hideFull);

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
        this.recommendedRefresh.setOnMouseClicked(e -> {
            this.recommendedList.getChildren().clear();
            this.component.sendGetRecommendedRooms();
        });
        this.content.getChildren().add(this.recommendedRefresh);

        this.recommendedList = new VBox(2);
        this.recommendedList.setLayoutX(5);
        this.recommendedList.setLayoutY(101);
        this.recommendedList.setMinWidth(311);
        this.content.getChildren().add(this.recommendedList);

        this.navigatorList = new ScrollPane();
        this.navigatorList.setSpacing(2);
        this.navigatorList.setLayoutX(5);
        this.content.getChildren().add(this.navigatorList);

        this.modifyRoomBack = new Pane();
        this.modifyRoomBack.setPrefSize(340, 22);
        this.modifyRoomBack.setLayoutX(1);
        this.modifyRoomBack.setLayoutY(79);
        this.modifyRoomBack.setCursor(Cursor.HAND);
        this.modifyRoomBack.setVisible(false);
        this.modifyRoomBack.setOnMouseClicked(e -> this.pendingAction = () -> this.hideModifyRoom());
        this.content.getChildren().add(this.modifyRoomBack);
        
        this.modifyRoomBackLabel = new Label(TextsManager.getInstance().getString("nav_roomnfo_hd_own"), true, "#336666");
        this.modifyRoomBackLabel.setLayoutX(39);
        this.modifyRoomBackLabel.setLayoutY(8);
        this.modifyRoomBack.getChildren().add(this.modifyRoomBackLabel);

        this.modifyRoomPage1 = new Pane();
        this.modifyRoomPage1.setPrefSize(340, 250);
        this.modifyRoomPage1.setLayoutX(1);
        this.modifyRoomPage1.setLayoutY(102);
        this.modifyRoomPage1.setVisible(false);
        this.content.getChildren().add(this.modifyRoomPage1);

        this.modifyRoomNameLabel = new Label(TextsManager.getInstance().getString("nav_modify_nameoftheroom"), true);
        this.modifyRoomNameLabel.setLayoutX(39);
        this.modifyRoomNameLabel.setLayoutY(11);
        this.modifyRoomPage1.getChildren().add(this.modifyRoomNameLabel);
        
        this.modifyRoomDescriptionLabel = new Label(TextsManager.getInstance().getString("nav_modify_roomdescription"), true);
        this.modifyRoomDescriptionLabel.setLayoutX(39);
        this.modifyRoomDescriptionLabel.setLayoutY(50);
        this.modifyRoomPage1.getChildren().add(this.modifyRoomDescriptionLabel);

        this.modifyRoomCategoryLabel = new Label(TextsManager.getInstance().getString("nav_modify_choosecategory"), true);
        this.modifyRoomCategoryLabel.setLayoutX(39);
        this.modifyRoomCategoryLabel.setLayoutY(119);
        this.modifyRoomPage1.getChildren().add(this.modifyRoomCategoryLabel);

        this.modifyRoomMaxVisitorsLabel = new Label(TextsManager.getInstance().getString("nav_modify_maxvisitors"), true);
        this.modifyRoomMaxVisitorsLabel.setLayoutX(39);
        this.modifyRoomMaxVisitorsLabel.setLayoutY(163);
        this.modifyRoomPage1.getChildren().add(this.modifyRoomMaxVisitorsLabel);

        this.modifyRoomShowNameLabel = new Label(TextsManager.getInstance().getString("nav_modify_nameshow"), true);
        this.modifyRoomShowNameLabel.setLayoutX(39);
        this.modifyRoomShowNameLabel.setLayoutY(210);
        this.modifyRoomPage1.getChildren().add(this.modifyRoomShowNameLabel);

        this.modifyRoomShowNameYesLabel = new Label(TextsManager.getInstance().getString("yes"));
        this.modifyRoomShowNameYesLabel.setLayoutX(61);
        this.modifyRoomShowNameYesLabel.setLayoutY(226);
        this.modifyRoomPage1.getChildren().add(this.modifyRoomShowNameYesLabel);

        this.modifyRoomShowNameNoLabel = new Label(TextsManager.getInstance().getString("no"));
        this.modifyRoomShowNameNoLabel.setLayoutX(142);
        this.modifyRoomShowNameNoLabel.setLayoutY(226);
        this.modifyRoomPage1.getChildren().add(this.modifyRoomShowNameNoLabel);

        this.modifyRoomPage2 = new Pane();
        this.modifyRoomPage2.setPrefSize(340, 250);
        this.modifyRoomPage2.setLayoutX(1);
        this.modifyRoomPage2.setLayoutY(102);
        this.modifyRoomPage2.setVisible(false);
        this.content.getChildren().add(this.modifyRoomPage2);
        
        this.modifyRoomDoorbellLabel = new Label(TextsManager.getInstance().getString("nav_modify_doorstatus"), true);
        this.modifyRoomDoorbellLabel.setLayoutX(39);
        this.modifyRoomDoorbellLabel.setLayoutY(10);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomDoorbellLabel);

        this.modifyRoomDoorbellOpenLabel = new Label(TextsManager.getInstance().getString("nav_modify_doorstatus_open"));
        this.modifyRoomDoorbellOpenLabel.setLayoutX(61);
        this.modifyRoomDoorbellOpenLabel.setLayoutY(29);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomDoorbellOpenLabel);

        this.modifyRoomDoorbellRingLabel = new Label(TextsManager.getInstance().getString("nav_modify_doorstatus_locked"));
        this.modifyRoomDoorbellRingLabel.setLayoutX(61);
        this.modifyRoomDoorbellRingLabel.setLayoutY(46);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomDoorbellRingLabel);

        this.modifyRoomDoorbellPasswordLabel = new Label(TextsManager.getInstance().getString("nav_modify_doorstatus_pwprotected"));
        this.modifyRoomDoorbellPasswordLabel.setLayoutX(61);
        this.modifyRoomDoorbellPasswordLabel.setLayoutY(63);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomDoorbellPasswordLabel);

        this.modifyRoomPasswordFields = new Pane();
        this.modifyRoomPasswordFields.setLayoutX(61);
        this.modifyRoomPasswordFields.setLayoutY(79);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomPasswordFields);

        this.modifyRoomDoorbellPassword1Label = new Label(TextsManager.getInstance().getString("nav_modify_doorstatus_givepw"), true);
        this.modifyRoomPasswordFields.getChildren().add(this.modifyRoomDoorbellPassword1Label);
        
        this.modifyRoomDoorbellPassword2Label = new Label(TextsManager.getInstance().getString("nav_modify_doorstatus_pwagain"), true);
        this.modifyRoomDoorbellPassword2Label.setLayoutY(35);
        this.modifyRoomPasswordFields.getChildren().add(this.modifyRoomDoorbellPassword2Label);

        this.modifyRoomOpenRightsLabel = new Label(TextsManager.getInstance().getString("nav_modify_letothersmove"));
        this.modifyRoomOpenRightsLabel.setLayoutX(61);
        this.modifyRoomOpenRightsLabel.setLayoutY(164);
        this.modifyRoomOpenRightsLabel.setMaxWidth(240);
        this.modifyRoomOpenRightsLabel.setLineSpacing(-1);
        this.modifyRoomOpenRightsLabel.setWrapText(true);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomOpenRightsLabel);

        this.modifyRoomResetRightsButton = new Button(TextsManager.getInstance().getString("nav_removerights"));
        this.modifyRoomResetRightsButton.setLayoutX(41);
        this.modifyRoomResetRightsButton.setLayoutY(204);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomResetRightsButton);

        this.modifyRoomResetRightsLabel = new Label(TextsManager.getInstance().getString("nav_removerights_desc"));
        this.modifyRoomResetRightsLabel.setLayoutX(131);
        this.modifyRoomResetRightsLabel.setLayoutY(202);
        this.modifyRoomResetRightsLabel.setMaxWidth(170);
        this.modifyRoomResetRightsLabel.setLineSpacing(-1);
        this.modifyRoomResetRightsLabel.setWrapText(true);
        this.modifyRoomPage2.getChildren().add(this.modifyRoomResetRightsLabel);

        this.modifyRoomFooter = new Pane();
        this.modifyRoomFooter.setPrefSize(340, 58);
        this.modifyRoomFooter.setLayoutX(1);
        this.modifyRoomFooter.setLayoutY(352);
        this.modifyRoomFooter.setVisible(false);
        this.content.getChildren().add(this.modifyRoomFooter);

        this.modifyRoomPrevious = new Label(TextsManager.getInstance().getString("previous_onearrowed"), true, "#A4A4A4");
        this.modifyRoomPrevious.setOnWidth(() -> this.modifyRoomPrevious.setLayoutX(138 - this.modifyRoomPrevious.getWidth()));
        this.modifyRoomPrevious.setUnderline(true);
        this.modifyRoomPrevious.setOnMouseClicked(e -> this.pendingAction = () -> this.showModifyRoomPage1());
        this.modifyRoomFooter.getChildren().add(this.modifyRoomPrevious);
        
        this.modifyRoomPageLabel = new Label("1/2", true, "#333333");
        this.modifyRoomPageLabel.setOnWidth(() -> this.modifyRoomPageLabel.setLayoutX(167 - Math.round(this.modifyRoomPageLabel.getWidth() / 2)));
        this.modifyRoomFooter.getChildren().add(this.modifyRoomPageLabel);

        this.modifyRoomNext = new Label(TextsManager.getInstance().getString("next_onearrowed"), true, "#333333");
        this.modifyRoomNext.setLayoutX(196);
        this.modifyRoomNext.setUnderline(true);
        this.modifyRoomNext.setOnMouseClicked(e -> this.pendingAction = () -> this.showModifyRoomPage2());
        this.modifyRoomNext.setCursor(Cursor.HAND);
        this.modifyRoomFooter.getChildren().add(this.modifyRoomNext);

        this.modifyRoomCancelButton = new Button(TextsManager.getInstance().getString("nav_cancel"));
        this.modifyRoomCancelButton.setLayoutX(49);
        this.modifyRoomCancelButton.setLayoutY(33);
        this.modifyRoomCancelButton.setOnMouseClicked(e -> this.pendingAction = () -> this.hideModifyRoom());
        this.modifyRoomFooter.getChildren().add(this.modifyRoomCancelButton);

        this.modifyRoomDeleteButton = new Button(TextsManager.getInstance().getString("nav_deleteroom"));
        this.modifyRoomDeleteButton.setLayoutX(117);
        this.modifyRoomDeleteButton.setLayoutY(33);
        this.modifyRoomDeleteButton.setVisible(false);
        this.modifyRoomFooter.getChildren().add(this.modifyRoomDeleteButton);
        
        this.modifyRoomOkButton = new ButtonLarge(TextsManager.getInstance().getString("nav_ok"));
        this.modifyRoomOkButton.setLayoutX(268);
        this.modifyRoomOkButton.setLayoutY(31);
        this.modifyRoomOkButton.setOnMouseClicked(e -> this.pendingAction = () -> this.doModifyRoom());
        this.modifyRoomFooter.getChildren().add(this.modifyRoomOkButton);

        this.modifyRoomNoteHeader = new Label(TextsManager.getInstance().getString("nav_updatenote_header"), true);
        this.modifyRoomNoteHeader.setLayoutY(148);
        this.modifyRoomNoteHeader.setOnWidth(() -> this.modifyRoomNoteHeader.setLayoutX(170 - Math.round(this.modifyRoomNoteHeader.getWidth() / 2)));
        this.modifyRoomNoteHeader.setVisible(false);
        this.content.getChildren().add(this.modifyRoomNoteHeader);

        this.modifyRoomNote = new Label(TextsManager.getInstance().getString("nav_updatenote"));
        this.modifyRoomNote.setTextAlignment(TextAlignment.CENTER);
        this.modifyRoomNote.setMaxWidth(240);
        this.modifyRoomNote.setWrapText(true);
        this.modifyRoomNote.setLayoutY(162);
        this.modifyRoomNote.setOnWidth(() -> this.modifyRoomNote.setLayoutX(173 - Math.round(this.modifyRoomNote.getWidth() / 2)));
        this.modifyRoomNote.setVisible(false);
        this.modifyRoomNote.setLineSpacing(-1);
        this.content.getChildren().add(this.modifyRoomNote);

        this.modifyRoomNoteOk = new ButtonLarge(TextsManager.getInstance().getString("nav_ok"));
        this.modifyRoomNoteOk.setLayoutY(246);
        this.modifyRoomNoteOk.setOnWidth(() -> this.modifyRoomNoteOk.setLayoutX(175 - Math.round(this.modifyRoomNoteOk.getWidth() / 2)));
        this.modifyRoomNoteOk.setOnMouseClicked(e -> this.pendingAction = () -> this.hideModifyRoom());
        this.modifyRoomNoteOk.setVisible(false);
        this.content.getChildren().add(this.modifyRoomNoteOk);

        this.resetRights = new BorderPane();
        this.resetRights.setSize(342, 412);

        this.resetRightsName = new Label("", true);
        this.resetRightsName.setLayoutY(98);
        this.resetRightsName.setOnWidth(() -> this.resetRightsName.setLayoutX(171 - Math.round(this.resetRightsName.getWidth() / 2)));
        this.resetRights.getChildren().add(this.resetRightsName);

        var resetRightsDescription = new Label(TextsManager.getInstance().getString("nav_remrightsconf"), true);
        resetRightsDescription.setLayoutX(60);
        resetRightsDescription.setLayoutY(139);
        resetRightsDescription.setMaxWidth(220);
        resetRightsDescription.setLineSpacing(-1);
        resetRightsDescription.setWrapText(true);
        this.resetRights.getChildren().add(resetRightsDescription);

        this.resetRightsCancel = new Button(TextsManager.getInstance().getString("nav_cancel"));
        this.resetRightsCancel.setLayoutY(199);
        this.resetRightsCancel.setOnWidth(() -> this.resetRightsCancel.setLayoutX(164 - this.resetRightsCancel.getWidth()));
        this.resetRightsCancel.setOnMouseClicked(e -> this.pendingAction = () -> this.setContent(this.content, this.padding, true));
        this.resetRights.getChildren().add(this.resetRightsCancel);

        this.resetRightsOk = new ButtonLarge(TextsManager.getInstance().getString("nav_removerights"));
        this.resetRightsOk.setLayoutX(175);
        this.resetRightsOk.setLayoutY(196);
        this.resetRights.getChildren().add(this.resetRightsOk);

        this.deleteRoom = new BorderPane();
        this.deleteRoom.setSize(342, 412);

        this.deleteRoomName = new Label("", true);
        this.deleteRoomName.setLayoutY(98);
        this.deleteRoomName.setOnWidth(() -> this.deleteRoomName.setLayoutX(171 - Math.round(this.deleteRoomName.getWidth() / 2)));
        this.deleteRoom.getChildren().add(this.deleteRoomName);

        this.deleteRoomDescription = new Label("", true);
        this.deleteRoomDescription.setLayoutX(60);
        this.deleteRoomDescription.setLayoutY(152);
        this.deleteRoomDescription.setMaxWidth(220);
        this.deleteRoomDescription.setLineSpacing(-1);
        this.deleteRoomDescription.setWrapText(true);
        this.deleteRoom.getChildren().add(this.deleteRoomDescription);

        this.deleteRoomCancel = new Button(TextsManager.getInstance().getString("nav_cancel"));
        this.deleteRoomCancel.setLayoutY(199);
        this.deleteRoomCancel.setOnWidth(() -> this.deleteRoomCancel.setLayoutX(154 - this.deleteRoomCancel.getWidth()));
        this.deleteRoomCancel.setOnMouseClicked(e -> this.pendingAction = () -> {
            this.showModifyRoomPage1();
            this.setContent(this.content, this.padding, true);
        });
        this.deleteRoom.getChildren().add(this.deleteRoomCancel);

        this.deleteRoomOk = new ButtonLarge(TextsManager.getInstance().getString("nav_deleteroom"));
        this.deleteRoomOk.setLayoutX(165);
        this.deleteRoomOk.setLayoutY(196);
        this.deleteRoom.getChildren().add(this.deleteRoomOk);

        this.info = new Pane();
        this.info.setPrefSize(340, 100);
        this.info.setLayoutX(1);
        this.info.setLayoutY(312);
        this.info.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_background.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
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

        this.passwordPrompt = new BorderPane();
        this.passwordPrompt.setSize(342, 412);

        var passwordPromptImg = new ImageView(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_doorbell_password.png"));
        passwordPromptImg.setLayoutX(122);
        passwordPromptImg.setLayoutY(60);

        var passwordPromptGoing = new Label(TextsManager.getInstance().getString("nav_goingprivate"), true);
        passwordPromptGoing.setOnWidth(() -> passwordPromptGoing.setLayoutX(171 - Math.round(passwordPromptGoing.getWidth() / 2)));
        passwordPromptGoing.setLayoutY(152);

        this.passwordPromptName = new Label("", true);
        this.passwordPromptName.setOnWidth(() -> this.passwordPromptName.setLayoutX(171 - Math.round(this.passwordPromptName.getWidth() / 2)));
        this.passwordPromptName.setLayoutY(167);

        this.passwordPromptIsProtected = new Label(TextsManager.getInstance().getString("nav_roomispwprotected"));
        this.passwordPromptIsProtected.setOnWidth(() -> this.passwordPromptIsProtected.setLayoutX(171 - Math.round(this.passwordPromptIsProtected.getWidth() / 2)));
        this.passwordPromptIsProtected.setLayoutY(187);

        this.passwordPromptField = new TextFieldSquare("");
        this.passwordPromptField.setLayoutX(100);
        this.passwordPromptField.setLayoutY(207);
        this.passwordPromptField.setWidth(138);

        this.passwordPromptCancelButton = new Button(TextsManager.getInstance().getString("nav_cancel"));
        this.passwordPromptCancelButton.setOnWidth(() -> this.passwordPromptCancelButton.setLayoutX(166 - this.passwordPromptCancelButton.getWidth()));
        this.passwordPromptCancelButton.setLayoutY(256);
        this.passwordPromptCancelButton.setOnMouseClicked(e -> {
            this.passwordPromptField.setText("");
            this.setContent(this.content, this.padding, true);
        });

        this.passwordPromptOkButton = new ButtonLarge(TextsManager.getInstance().getString("nav_ok"));
        this.passwordPromptOkButton.setLayoutX(174);
        this.passwordPromptOkButton.setLayoutY(253);
        this.passwordPromptOkButton.setOnMouseClicked(e -> {
            this.setContent(this.content, this.padding, true);
        });

        this.passwordPrompt.addContent(passwordPromptImg, passwordPromptGoing, this.passwordPromptName, this.passwordPromptIsProtected, this.passwordPromptField, this.passwordPromptCancelButton, this.passwordPromptOkButton);
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
        if (this.modifyRoom != null) {
            return;
        }

        this.noResults.setVisible(false);

        if (page == NavigatorPage.PUBLIC) {
            this.currentPage = NavigatorPage.PUBLIC;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
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
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_public.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_addtofavourites"));
            this.infoLeftButton.setTranslateX(0);
        }
        else {
            this.searchButton.setVisible(true);
            this.ownButton.setVisible(true);
            this.favouritesButton.setVisible(true);
        }

        if (page == NavigatorPage.PRIVATE) {
            this.currentPage = NavigatorPage.PRIVATE;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
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
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_private.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_addtofavourites"));
            this.infoLeftButton.setTranslateX(0);

            this.updateRecommendedRooms(new ArrayList<>());
        }
        else {
            this.recommendedTitle.setVisible(false);
            this.recommendedRefresh.setVisible(false);
            this.recommendedList.setVisible(false);

            this.navigatorList.setPadding(0, 0);
        }

        if (page == NavigatorPage.SEARCH) {
            this.currentPage = NavigatorPage.SEARCH;
            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_search.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
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
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_search.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_addtofavourites"));
            this.infoLeftButton.setTranslateX(0);

            this.updateBackButtons();
            this.navigatorList.clearContent();
        }
        else {
            this.search.setVisible(false);
        }

        if (page == NavigatorPage.OWN) {
            this.currentPage = NavigatorPage.OWN;
            this.component.sendGetOwnFlats();

            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_own_hd"));
            this.title.setLayoutY(135);
            this.hideFull.setVisible(false);

            this.room.setVisible(true);

            this.navigatorList.setSize(330, 162);
            this.navigatorList.setLayoutY(150);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_ownrooms_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_own.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_modify"));
            this.infoLeftButton.setTranslateX(0);

            this.updateBackButtons();
        }
        else {
            this.room.setVisible(false);
        }

        if (page == NavigatorPage.FAVOURITES) {
            this.currentPage = NavigatorPage.FAVOURITES;
            this.component.sendGetFavoriteFlats();

            this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_favourites.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.title.setText(TextsManager.getInstance().getString("nav_fav_hd"));
            this.title.setLayoutY(90);
            this.hideFull.setVisible(false);

            this.navigatorList.setSize(330, 207);
            this.navigatorList.setLayoutY(105);

            this.infoTitle.setText(TextsManager.getInstance().getString("nav_private_helptext_hd"));
            this.infoDescription.setText(TextsManager.getInstance().getString("nav_favourites_helptext"));
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_favourites.png"));
            this.infoLeftButton.setText(TextsManager.getInstance().getString("nav_removefavourites"));
            this.infoLeftButton.setTranslateX(-30);

            this.updateBackButtons();
        }

        this.sendPageRequest();

        this.info.setVisible(true);
        this.infoImg.setTranslateX(0);
        this.infoImg.setTranslateY(0);
        this.infoSubtitle.setText("");
        this.infoDescription.setTranslateY(0);
        this.infoLeftButton.setVisible(false);
        this.infoGoButton.setVisible(false);
    }

    private void toggleHideFull() {
        if (this.currentPage == NavigatorPage.PRIVATE || this.currentPage == NavigatorPage.PUBLIC) {
            this.isHideFull = !this.isHideFull;

            if (this.isHideFull) {
                this.hideFull.setText(TextsManager.getInstance().getString("nav_showfull"));
            } else {
                this.hideFull.setText(TextsManager.getInstance().getString("nav_hidefull"));
            }

            if (this.currentPage == NavigatorPage.PUBLIC) {
                this.component.sendNavigate(this.publicCategoryId);
            }

            if (this.currentPage == NavigatorPage.PRIVATE) {
                this.component.sendNavigate(this.privateCategoryId);
            }
        }
    }

    private void sendPageRequest() {
        switch (this.currentPage) {
            case PUBLIC:
                this.component.sendNavigate(this.publicCategoryId);
                break;
            case PRIVATE:
                this.component.sendNavigate(this.privateCategoryId);

                if (this.privateCategoryId == this.defaultPrivateCategoryId) {
                    this.component.sendGetRecommendedRooms();
                }
                break;
            case OWN:
                this.component.sendGetOwnFlats();
                break;
            case FAVOURITES:
                this.component.sendGetFavoriteFlats();
                break;
        }
    }

    private void backTopShow(String text, EventHandler<MouseEvent> event) {
        if (this.currentPage == NavigatorPage.PUBLIC) {
            this.backTop.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "back_top_public.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.backTop.setPrefHeight(22);
            this.backTop.setLayoutY(58);
            this.backTopLabel.setLayoutY(3);
            this.backTop.setVisible(true);
        }
        if (this.currentPage == NavigatorPage.PRIVATE) {
            this.backTop.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "back_top_private.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            this.backTop.setPrefHeight(27);
            this.backTop.setLayoutY(76);
            this.backTopLabel.setLayoutY(8);
        }

        this.backTopLabel.setText(text);
        this.backTop.setOnMouseClicked(event);
        this.backTop.setVisible(true);
    }

    private void infoShowRoom(NavigatorNode node) {
        if (node.getNodeType() == NavigatorNodeType.PUBLIC_ROOM) {
            this.infoImg.setTranslateX(16);
            this.infoImg.setTranslateY(15);

            // System.out.println("assets/views/navigator/", "thumbnails/" + room.infoImg + ".png");
            this.infoImg.setImage(this.resolveThumbnail(node));
            this.infoTitle.setText(node.getName() + " (" + node.getUsercount() + "/" + node.getMaxUsers() + ")");
            this.infoSubtitle.setText("");
            this.infoDescription.setTranslateY(0);
        } else {
            this.infoImg.setTranslateX(0);
            this.infoImg.setTranslateY(0);
            this.infoImg.setImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "info_doorbell_" + getBackgroundByDoorbell(node.getDoorbell()) + ".png"));
            this.infoTitle.setText(node.getName());
            this.infoSubtitle.setText("(" + node.getUsercount() + "/" + node.getMaxUsers() + ") " + TextsManager.getInstance().getString("room_owner") + " " + node.getOwner());
            this.infoDescription.setTranslateY(10);
        }

        this.infoDescription.setText(node.getDescription());

        if (this.currentPage == NavigatorPage.OWN) {
            this.infoLeftButton.setOnMouseClicked(e1 -> this.pendingAction = () -> this.showModifyRoom(node));
        } else if (this.currentPage == NavigatorPage.FAVOURITES) {
            this.infoLeftButton.setOnMouseClicked(e1 -> this.pendingAction = () -> this.removeFromFavourites(node.getId()));
        } else {
            this.infoLeftButton.setOnMouseClicked(e1 -> this.pendingAction = () -> this.addToFavourites(node.getId()));
        }

        this.infoGoButton.setOnMouseClicked(e1 -> this.pendingAction = () -> this.goToRoom(node));
        this.infoLeftButton.setVisible(true);
        this.infoGoButton.setVisible(true);

        if (!this.info.isVisible()) {
            this.navigatorList.setSize(330, (int) this.navigatorList.getPrefHeight() - 96);
        }

        this.info.setVisible(true);
    }

    private Image resolveThumbnail(NavigatorNode node) {
        Image img = null;
        String imageName = node.getUnitStrId();

        if (this.aliasManager.getAliases().containsKey(imageName)) {
            imageName = this.aliasManager.getAliases().get(imageName);
        }

        try {
            img = ResourceManager.getInstance().getFxImage("assets/views/navigator/thumbnails", imageName + ".png");
        } catch (Exception ex) { }

        int maxAttempts = 2; // any increase will be useless, any decrease will break it... lol
        while (img == null && maxAttempts > 0) {
            maxAttempts--;

            try {
                imageName = String.join("_", Arrays.copyOf(imageName.split("_"), imageName.split("_").length - 1)); // remove last entry and try again, eg beauty salon

                if (this.aliasManager.getAliases().containsKey(imageName)) {
                    imageName = this.aliasManager.getAliases().get(imageName);
                }

                img = ResourceManager.getInstance().getFxImage("assets/views/navigator/thumbnails", imageName + ".png");
            } catch (Exception ex) { }
        }

        if (img == null) {
            try {
                img = ResourceManager.getInstance().getFxImage("assets/views/navigator/thumbnails", "parsnip_placeholder.png");
            } catch (Exception ex) { }
        }

        return img;
    }

    private void infoHide() {
        this.navigatorList.setSize(330, (int)this.navigatorList.getPrefHeight() + 96);
        this.info.setVisible(false);
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
                Movie.getInstance().goToHotelView();
            });
            index++;
        }

        var backCategories = new ArrayList<NavigatorNode>();
        var currentNavigatorNode = this.getComponent().getCurrentNode();
        while (this.getComponent().getParentNode(currentNavigatorNode) != null) {
            backCategories.add(currentNavigatorNode);
            currentNavigatorNode = this.getComponent().getParentNode(currentNavigatorNode);
        }

        var startY = this.currentPage == NavigatorPage.PUBLIC ? 40 : 63;
        Collections.reverse(backCategories);

        for (var category : backCategories) {
            var parentNode = this.getComponent().getParentNode(category);

            if (index == 0) {
                this.backTopShow(parentNode.getName(), e -> this.pendingAction = () -> this.component.sendNavigate(category.getParentid())); //this.showCategory(category.parentCategory.categoryId));
            } else {
                content.getChildren().add(new NavigatorBackButton(parentNode.getName(), startY, index, e -> this.pendingAction = () ->  this.component.sendNavigate(category.getParentid()))); //this.showCategory(category.parentCategory.categoryId)));
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

    public void updateRecommendedRooms(List<NavigatorNode> recommendedRooms) {
        this.recommendedList.getChildren().clear();

        for (var room : recommendedRooms) {
            this.addRoom(room, true);
        }
    }

    public void updateRoomList(NavigatorNode category) {
        this.navigatorList.clearContent();

        if (this.currentPage == NavigatorPage.FAVOURITES) {
            category.setName(TextsManager.getInstance().getString("nav_fav_hd"));
        }

        this.updateBackButtons();
        this.title.setText(category.getName());

        // Filter out rooms
        for (var room : category.getChildren().stream().filter(x -> x.isRoom()).collect(Collectors.toList())) {
            this.addRoom(room, false);
        }

        for (var childNavigatorNode : category.getChildren().stream().filter(x -> x.isCategory()).collect(Collectors.toList())) {
            this.addCategory(childNavigatorNode);
        }
    }

    public void updateRoomList(List<NavigatorNode> rooms) {
        this.navigatorList.clearContent();

        if (rooms.isEmpty()) {
            if (this.currentPage == NavigatorPage.SEARCH) {
                this.noResults.setText(TextsManager.getInstance().getString("nav_prvrooms_notfound"));
            }
            if (this.currentPage == NavigatorPage.OWN) {
                this.noResults.setText(TextsManager.getInstance().getString("nav_private_norooms"));
            }
            this.noResults.setVisible(true);
            return;
        }

        for (var room : rooms) {
            this.addRoom(room, false);
        }
    }

    private void goToRoom(NavigatorNode room) {
        if (room.getDoorbell() == Doorbell.PASSWORD) {
            this.showPasswordPrompt(room);
        }
        else {
            Movie.getInstance().goToRoom(room.getId(), null);
        }
    }

    private void showPasswordPrompt(NavigatorNode room) {
        this.passwordPromptName.setText(room.getName());
        this.passwordPromptOkButton.setOnMouseClicked(e -> Movie.getInstance().goToRoom(room.getId(), this.passwordPromptField.getText()));
        this.setContent(this.passwordPrompt, new Insets(9, 11, 11, 10), true);
    }

    private void doModifyRoom() {
        this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_modify_room_note.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.modifyRoomPage1.setVisible(false);
        this.modifyRoomPage2.setVisible(false);
        this.modifyRoomFooter.setVisible(false);
        this.modifyRoomNoteHeader.setVisible(true);
        this.modifyRoomNote.setVisible(true);
        this.modifyRoomNoteOk.setVisible(true);
    }
    
    private void showModifyRoom(NavigatorNode room) {
        this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_modify_room.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.room.setVisible(false);
        this.title.setVisible(false);
        this.navigatorList.setVisible(false);
        this.infoHide();

        this.modifyRoom = room;
        this.modifyRoomResetRightsButton.setOnMouseClicked(e -> this.pendingAction = () -> this.showResetRights(room));
        this.modifyRoomDeleteButton.setOnMouseClicked(e -> this.pendingAction = () -> this.showDeleteRoom(room));

        this.modifyRoomBack.setVisible(true);
        this.showModifyRoomPage1();
        this.modifyRoomFooter.setVisible(true);
    }

    private void showResetRights(NavigatorNode room) {
        this.resetRightsName.setText(room.getName());
        this.resetRightsOk.setOnMouseClicked(e -> this.pendingAction = () -> this.doResetRights(room.getFlatId()));
        this.setContent(this.resetRights, new Insets(9, 11, 11, 10), true);
    }

    private void showDeleteRoom(NavigatorNode room) {
        this.deleteRoomName.setText(room.getName());
        this.deleteRoomDescription.setText(TextsManager.getInstance().getString("nav_delroom1"));
        this.deleteRoomOk.setOnMouseClicked(e -> this.pendingAction = () -> {
            this.deleteRoomDescription.setText(TextsManager.getInstance().getString("nav_delroom2"));
            this.deleteRoomOk.setOnMouseClicked(e1 -> this.pendingAction = () -> this.doDeleteRoom(room.getFlatId()));
        });
        this.setContent(this.deleteRoom, new Insets(9, 11, 11, 10), true);
    }

    private void hideModifyRoom() {
        this.content.setBackground(new Background(new BackgroundImage(ResourceManager.getInstance().getFxImage("assets/views/navigator/", "background_own.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.room.setVisible(true);
        this.title.setVisible(true);
        this.navigatorList.setVisible(true);
        this.infoShowRoom(this.modifyRoom);

        this.modifyRoom = null;

        this.modifyRoomBack.setVisible(false);
        this.modifyRoomPage1.setVisible(false);
        this.modifyRoomPage2.setVisible(false);
        this.modifyRoomFooter.setVisible(false);
        this.modifyRoomNoteHeader.setVisible(false);
        this.modifyRoomNote.setVisible(false);
        this.modifyRoomNoteOk.setVisible(false);
    }

    private void showModifyRoomPage1() {
        this.modifyRoomPage1.setVisible(true);
        this.modifyRoomPage2.setVisible(false);
        this.modifyRoomPrevious.setTextFill(Color.web("#A4A4A4"));
        this.modifyRoomPrevious.setCursor(Cursor.DEFAULT);
        this.modifyRoomPageLabel.setText("1/2");
        this.modifyRoomPageLabel.setTranslateX(0);
        this.modifyRoomNext.setTextFill(Color.web("#333333"));
        this.modifyRoomNext.setCursor(Cursor.HAND);
        this.modifyRoomDeleteButton.setVisible(false);
    }

    private void showModifyRoomPage2() {
        this.modifyRoomPage1.setVisible(false);
        this.modifyRoomPage2.setVisible(true);
        this.modifyRoomPrevious.setTextFill(Color.web("#333333"));
        this.modifyRoomPrevious.setCursor(Cursor.HAND);
        this.modifyRoomPageLabel.setText("2/2");
        this.modifyRoomPageLabel.setTranslateX(-1);
        this.modifyRoomNext.setTextFill(Color.web("#A4A4A4"));
        this.modifyRoomNext.setCursor(Cursor.DEFAULT);
        this.modifyRoomDeleteButton.setVisible(true);
    }
    
    private void addRoom(NavigatorNode room, Boolean recommended) {
        var navigatorItem = new NavigatorItem(room);
        navigatorItem.setNameButtonOnMouseClicked(e -> this.pendingAction = () -> this.infoShowRoom(navigatorItem.getNode()));
        navigatorItem.setGoButtonOnMouseClicked(e -> this.pendingAction = () -> this.goToRoom(navigatorItem.getNode()));

        if (recommended) {
            this.recommendedList.getChildren().add(navigatorItem);
        } else {
            this.navigatorList.addContent(navigatorItem);
        }
    }

    private void addCategory(NavigatorNode category) {
        var navigatorItem = new NavigatorItem(category);
        navigatorItem.setOnMouseClicked(e -> this.pendingAction = () -> this.component.sendNavigate(category.getId()));
        
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

    public void showCreateRoom() {
        Movie.getInstance().createObject(new Alert("showCreateRoom"));
    }

    // TODO
    public void sendPasswordResult(Boolean correct) {
        if (correct) {
            this.setContent(this.content, this.padding, false);
        }
        else {
            this.passwordPromptIsProtected.setText(TextsManager.getInstance().getString("nav_tryingpw"));
            this.passwordPromptIsProtected.setLayoutX(171 - Math.round(this.passwordPromptIsProtected.getWidth() / 2));
            this.passwordPromptIsProtected.setLayoutY(192);

            var incorrect = new Label(TextsManager.getInstance().getString("nav_incorrectflatpw"), true);
            incorrect.setOnWidth(() -> incorrect.setLayoutX(171 - Math.round(incorrect.getWidth() / 2)));;
            incorrect.setLayoutY(207);
            this.passwordPrompt.getChildren().add(incorrect);

            this.passwordPromptField.setVisible(false);
            this.passwordPromptCancelButton.setVisible(false);

            var oldClicked = this.passwordPromptOkButton.getOnMouseClicked();
            
            this.passwordPromptOkButton.setLayoutX(171 - Math.round(this.passwordPromptOkButton.getWidth() / 2));
            this.passwordPromptOkButton.setLayoutY(238);
            this.passwordPromptOkButton.setOnMouseClicked(e -> {
                this.passwordPromptIsProtected.setText(TextsManager.getInstance().getString("nav_tryingpw"));
                this.passwordPromptIsProtected.setLayoutX(171 - Math.round(this.passwordPromptIsProtected.getWidth() / 2));
                this.passwordPromptIsProtected.setLayoutY(187);

                this.passwordPrompt.getChildren().remove(incorrect);

                this.passwordPromptField.setVisible(true);
                this.passwordPromptField.setText("");
                this.passwordPromptCancelButton.setVisible(true);
                
                this.passwordPromptOkButton.setLayoutX(174);
                this.passwordPromptOkButton.setLayoutY(253);
                this.passwordPromptOkButton.setOnMouseClicked(oldClicked);
            });
        }
    }

    // TODO
    private void doResetRights(int roomId) {
        System.out.println("Reset rights for roomId " + roomId);
        this.setContent(this.content, this.padding, true);
    }

    // TODO
    private void doDeleteRoom(int roomId) {
        System.out.println("Delete room with roomId " + roomId);
        this.hideModifyRoom();
        this.setContent(this.content, this.padding, true);
    }

    // TODO
    private void addToFavourites(int roomId) { 
        System.out.println("Add " + roomId + " to favourites");
    }
    
    // TODO
    private void removeFromFavourites(int roomId) { 
        System.out.println("Remove " + roomId + " from favourites");
    }

    public boolean isHideFullRooms() {
        return hideFullRooms;
    }

    public void setHideFullRooms(boolean hideFullRooms) {
        this.hideFullRooms = hideFullRooms;
    }

    public boolean isHideFull() {
        return isHideFull;
    }

    public void setHideFull(boolean hideFull) {
        isHideFull = hideFull;
    }

    private enum NavigatorPage {
        PUBLIC,
        PRIVATE,
        SEARCH,
        OWN,
        FAVOURITES
    }
}