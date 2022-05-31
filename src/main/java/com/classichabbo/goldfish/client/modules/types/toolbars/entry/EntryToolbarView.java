package com.classichabbo.goldfish.client.modules.types.toolbars.entry;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.Attributes;
import com.classichabbo.goldfish.client.game.entities.user.HabboClubObject;
import com.classichabbo.goldfish.client.game.entities.user.UserObject;
import com.classichabbo.goldfish.client.game.values.types.TextsManager;
import com.classichabbo.goldfish.client.modules.controls.ImageButton;
import com.classichabbo.goldfish.client.modules.controls.Label;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.types.alerts.Alert;
import com.classichabbo.goldfish.client.modules.types.error.ErrorWindow;
import com.classichabbo.goldfish.client.modules.types.widgets.navigator.NavigatorView;
import com.classichabbo.goldfish.networking.Connection;
import com.classichabbo.goldfish.util.DimensionUtil;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class EntryToolbarView extends View {
    private final EntryToolbarComponent component;

    private ImageButton gamesButton;
    private ImageButton helpButton;
    private ImageButton catalogueButton;
    private ImageButton eventsButton;
    private ImageButton navigatorButton;
    private ImageButton chatButton;
    private ImageButton friendsButton;

    private int scrollOffset;
    private boolean finishedScroll;
    private boolean scrollReady = false;

    private Label club_bottombar_text1;
    private Label club_bottombar_text2;
    private ImageButton ownhabbo_icon_image;
    private Label ownhabbo_name_text;
    private Label ownhabbo_mission_text;

    private Runnable runAfterScroll;

    public EntryToolbarView() {
        this.component = new EntryToolbarComponent(this);
    }

    @Override
    public void start() {
        this.scrollOffset = 0;
        this.finishedScroll = false;
        this.scrollReady = false;

        var userObj = Connection.get().attr(Attributes.USER_OBJECT).get();

        if (userObj == null) {
            Movie.getInstance().createObject(new ErrorWindow());
            return;
        }

        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        this.ownhabbo_icon_image = new ImageButton(ResourceManager.getInstance().getFxImage("assets/misc", "blank.png"));
        ownhabbo_icon_image.setCursor(Cursor.HAND);
        ownhabbo_icon_image.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("userHead clicked")));

        // in Shockwave this is actually offset 2px to the left of the below two labels, but if you want it
        // in line with the others, set X back to 53 :)

        this.ownhabbo_name_text = new Label("", "#FFFFFF");
        ownhabbo_name_text.setLayoutX(51);
        ownhabbo_name_text.setLayoutY(8);

        this.ownhabbo_mission_text = new Label("", "#FFFFFF");
        ownhabbo_mission_text.setLayoutX(53);
        ownhabbo_mission_text.setLayoutY(20);

        var updateIdLabel = new Label("Update My Habbo Id >>", "#FFFFFF");
        updateIdLabel.setLayoutX(53);
        updateIdLabel.setLayoutY(32);
        updateIdLabel.setUnderline(true);
        updateIdLabel.setCursor(Cursor.HAND);
        updateIdLabel.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("updateIdLabel clicked")));

        this.club_bottombar_text1 = new Label("Habbo Club", "#FFFFFF");
        club_bottombar_text1.setLayoutX(287);
        club_bottombar_text1.setLayoutY(19);

        this.club_bottombar_text2 = new Label("", "#FFFFFF");
        club_bottombar_text2.setLayoutX(287);
        club_bottombar_text2.setLayoutY(30);
        club_bottombar_text2.setCursor(Cursor.HAND);
        club_bottombar_text2.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("clubDescLabel clicked")));

        var clubButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "club.png"));
        clubButton.setLayoutX(245);
        clubButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("clubButton clicked")));

        this.chatButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "chat.png"));
        this.chatButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("chatButton clicked")));

        this.friendsButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "friends.png"));
        this.friendsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("friendsButton clicked")));

        this.navigatorButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "navigator.png"));
        this.navigatorButton.setOnMouseClicked(e -> Movie.getInstance().getViews().stream().filter(x -> x instanceof NavigatorView).findFirst().ifPresent(x -> {
            x.toggleVisibility();
            x.toFront();
        }));

        this.eventsButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "events.png"));
        this.eventsButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("eventsButton clicked")));

        this.catalogueButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "catalogue.png"));
        this.catalogueButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("catalogueButton clicked")));

        this.gamesButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "games.png"));
        this.gamesButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("gamesButton clicked")));

        this.helpButton = new ImageButton(ResourceManager.getInstance().getFxImage("assets/views/entry_toolbar", "help.png"));
        this.helpButton.setOnMouseClicked(e -> Movie.getInstance().createObject(new Alert("helpButton clicked")));

        this.getChildren().addAll(ownhabbo_icon_image, ownhabbo_name_text, ownhabbo_mission_text, updateIdLabel, club_bottombar_text1, club_bottombar_text2);
        this.getChildren().addAll(clubButton, chatButton, friendsButton, navigatorButton, eventsButton, catalogueButton, gamesButton, helpButton);
        this.refreshTexts();

        this.toFront();
        this.registerUpdate();

        this.scrollReady = true;
    }

    private void refreshTexts() {
        var userObj = Connection.get().attr(Attributes.USER_OBJECT).get();

        if (userObj != null) {
            this.getComponent().updateDetails(userObj);
        }

        var habboClubObject = Connection.get().attr(Attributes.HABBO_CLUB_OBJECT).get();

        if (userObj != null) {
            this.getComponent().updateClubStatus(habboClubObject);
        }

        this.scrollReady = true;
    }

    public void updateDetails(UserObject userObj) {
        if (userObj == null) {
            return;
        }

        ResourceManager.getInstance().getWebImage("https://cdn.classichabbo.com/habbo-imaging/avatarimage?figure=" + userObj.getFigure() + "&size=b&head=1&direction=3&head_direction=3&gesture=std", (image) -> {
            this.ownhabbo_icon_image.setImage(image);
            this.ownhabbo_icon_image.setLayoutX(Math.floor(65 / 2 - this.ownhabbo_icon_image.getImage().getWidth() / 2));
            this.ownhabbo_icon_image.setLayoutY(Math.round(55 / 2 - this.ownhabbo_icon_image.getImage().getHeight() / 2));
        });


        //this.ownhabbo_icon_image.setImage(new Image("https://cdn.classichabbo.com/habbo-imaging/avatarimage?figure=" + userObj.getFigure() + "&size=b&head=1&direction=3&head_direction=3&gesture=std"));


        this.ownhabbo_name_text.setText(userObj.getUsername());
        this.ownhabbo_mission_text.setText(userObj.getMission());
    }

    public void updateClubStatus(HabboClubObject tStatus) {
        if (tStatus == null) {
            return;
        }

        var tDays = tStatus.getDaysLeft() + (tStatus.getPrepaidPeriods() * 31);

        if (tStatus.getPrepaidPeriods() < 0) {
            this.club_bottombar_text1.setText(TextsManager.getInstance().getString("club_habbo.bottombar.text.member"));
            this.club_bottombar_text2.setText(TextsManager.getInstance().getString("club_member"));
        } else {
            if (tDays == 0) {
                this.club_bottombar_text1.setText(TextsManager.getInstance().getString("club_habbo.bottombar.text.notmember"));
                this.club_bottombar_text2.setText(TextsManager.getInstance().getString("club_habbo.bottombar.link.notmember"));
            } else {
                var tStr = TextsManager.getInstance().getString("club_habbo.bottombar.link.member");
                tStr = tStr.replace("%days%", String.valueOf(tDays));
                this.club_bottombar_text1.setText(TextsManager.getInstance().getString("club_habbo.bottombar.text.member"));
                this.club_bottombar_text2.setText(tStr);
            }
        }
    }

    @Override
    public void stop() {
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
        if (!this.scrollReady)
            return;
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

                if (this.runAfterScroll != null) {
                    this.runAfterScroll.run();
                    this.runAfterScroll = null;
                }
            }
        }

        // Handle resizing of window
        this.setLayoutY(DimensionUtil.getProgramHeight() - this.scrollOffset);
        this.setPrefSize(DimensionUtil.getProgramWidth(), 55);

        var pWidth = DimensionUtil.getProgramWidth();

        this.helpButton.setLayoutX(pWidth - 45);
        this.gamesButton.setLayoutX(pWidth - 81);
        this.catalogueButton.setLayoutX(pWidth - 121);
        this.eventsButton.setLayoutX(pWidth - 162);
        this.navigatorButton.setLayoutX(pWidth - 207);
        this.friendsButton.setLayoutX(pWidth - 244);
        this.chatButton.setLayoutX(pWidth - 284);
    }

    @Override
    public EntryToolbarComponent getComponent() {
        return component;
    }

    public void setRunAfterScroll(Runnable runAfterScroll) {
        this.runAfterScroll = runAfterScroll;
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
