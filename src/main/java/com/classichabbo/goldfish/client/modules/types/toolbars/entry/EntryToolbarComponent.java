package com.classichabbo.goldfish.client.modules.types.toolbars.entry;

import com.classichabbo.goldfish.client.modules.Component;
import com.classichabbo.goldfish.client.game.entities.user.HabboClubObject;
import com.classichabbo.goldfish.client.game.entities.user.UserObject;

public class EntryToolbarComponent extends Component {
    private final EntryToolbarView entryToolbarView;

    public EntryToolbarComponent(EntryToolbarView entryToolbarView) {
        this.entryToolbarView = entryToolbarView;
    }

    public void updateDetails(UserObject userObj) {
        invoke(() -> {
            this.entryToolbarView.updateDetails(userObj);
        });
    }

    public void updateClubStatus(HabboClubObject habboClubObject) {
        invoke(() -> {
            this.entryToolbarView.updateClubStatus(habboClubObject);
        });
    }
}
