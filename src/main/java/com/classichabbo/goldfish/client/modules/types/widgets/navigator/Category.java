package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import java.util.ArrayList;

public class Category {
    public int categoryId;
    public String name;
    public int visitors;
    public int maxVisitors;
    public Category parentCategory;
    public ArrayList<NavigatorRoom> rooms;
    public ArrayList<Category> categories;

    public Category(int categoryId, String name, int visitors, int maxVisitors) {
        this.categoryId = categoryId;
        this.name = name;
        this.visitors = visitors;
        this.maxVisitors = maxVisitors;
        this.rooms = new ArrayList<NavigatorRoom>();
        this.categories = new ArrayList<Category>();
    }

    public Category(int categoryId, String name, Category parentCategory, int visitors, int maxVisitors) {
        this.categoryId = categoryId;
        this.name = name;
        this.visitors = visitors;
        this.maxVisitors = maxVisitors;
        this.parentCategory = parentCategory;
        this.rooms = new ArrayList<NavigatorRoom>();
        this.categories = new ArrayList<Category>();
    }

    public void addRoom(NavigatorRoom room) {
        this.rooms.add(room);
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}
