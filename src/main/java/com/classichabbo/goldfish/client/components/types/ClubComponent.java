package com.classichabbo.goldfish.client.components.types;

import com.classichabbo.goldfish.client.components.Component;
import com.classichabbo.goldfish.client.game.club.HabboClubObject;
import com.classichabbo.goldfish.client.views.types.club.ClubView;

public class ClubComponent extends Component {
    private final ClubView clubView;

    public ClubComponent(ClubView clubView) {
        this.clubView = clubView;
    }

    public void setStatus(HabboClubObject habboClubObject) {

    }
}
