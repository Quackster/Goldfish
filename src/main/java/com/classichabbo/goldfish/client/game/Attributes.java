package com.classichabbo.goldfish.client.game;

import com.classichabbo.goldfish.client.game.entities.user.HabboClubObject;
import com.classichabbo.goldfish.client.game.entities.user.UserObject;
import io.netty.util.AttributeKey;

public class Attributes {
    public static final AttributeKey<UserObject> USER_OBJECT = AttributeKey.valueOf("UserObject");
    public static final AttributeKey<HabboClubObject> HABBO_CLUB_OBJECT = AttributeKey.valueOf("HabboClubObject");
}
