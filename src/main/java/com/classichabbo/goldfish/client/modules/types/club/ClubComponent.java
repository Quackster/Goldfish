package com.classichabbo.goldfish.client.modules.types.club;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.modules.Component;
import com.classichabbo.goldfish.client.game.entities.user.HabboClubObject;
import com.classichabbo.goldfish.client.modules.types.toolbars.entry.EntryToolbarView;

public class ClubComponent extends Component {
    private final ClubView clubView;

    public ClubComponent(ClubView clubView) {
        this.clubView = clubView;
    }

    public void setStatus(HabboClubObject habboClubObject) {
        invoke(() -> {
            var entryToolbar = Movie.getInstance().getViewByClass(EntryToolbarView.class);

            if (entryToolbar != null) {
                entryToolbar.getComponent().updateClubStatus(habboClubObject);
            }
        });
    }
}
