package com.classichabbo.goldfish.client.game.club;

import io.netty.util.AttributeKey;

public class HabboClubObject {
    private String prodName;
    private int daysLeft;
    private int elapsedPeriods;
    private int prepaidPeriods;
    private int responseFlag;

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public int getElapsedPeriods() {
        return elapsedPeriods;
    }

    public void setElapsedPeriods(int elapsedPeriods) {
        this.elapsedPeriods = elapsedPeriods;
    }

    public int getPrepaidPeriods() {
        return prepaidPeriods;
    }

    public void setPrepaidPeriods(int prepaidPeriods) {
        this.prepaidPeriods = prepaidPeriods;
    }

    public int getResponseFlag() {
        return responseFlag;
    }

    public void setResponseFlag(int responseFlag) {
        this.responseFlag = responseFlag;
    }
}
