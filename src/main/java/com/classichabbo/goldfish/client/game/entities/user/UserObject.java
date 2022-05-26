package com.classichabbo.goldfish.client.game.entities.user;

import io.netty.util.AttributeKey;

public class UserObject {
    private int id;
    private String username;
    private String figure;
    private String gender;
    private String motto;

    /*
    public UserObject(int id, String username, String figure, String gender, String motto) {
        this.id = id;
        this.username = username;
        this.figure = figure;
        this.gender = gender;
        this.motto = motto;
    }
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
}
