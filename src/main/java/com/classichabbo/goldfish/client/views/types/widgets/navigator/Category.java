package com.classichabbo.goldfish.client.views.types.widgets.navigator;

import java.util.ArrayList;

public class Category {
    public int categoryId;
    public String name;
    private int visitors;
    private int maxVisitors;
    public Category parentCategory;
    public ArrayList<Room> rooms;
    public ArrayList<Category> categories;

    // I didn't know whether visitors/maxVisitors is calculated by the client or server, but this should be able to
    // handle either - just pick a constructor as necessary :P

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
        this.visitors = -1;
        this.maxVisitors = -1;
        this.rooms = new ArrayList<Room>();
        this.categories = new ArrayList<Category>();
    }

    public Category(int categoryId, String name, int visitors, int maxVisitors) {
        this.categoryId = categoryId;
        this.name = name;
        this.visitors = visitors;
        this.maxVisitors = maxVisitors;
        this.rooms = new ArrayList<Room>();
        this.categories = new ArrayList<Category>();
    }

    public Category(int categoryId, String name, Category parentCategory) {
        this.categoryId = categoryId;
        this.name = name;
        this.visitors = -1;
        this.maxVisitors = -1;
        this.parentCategory = parentCategory;
        this.rooms = new ArrayList<Room>();
        this.categories = new ArrayList<Category>();
    }

    public Category(int categoryId, String name, Category parentCategory, int visitors, int maxVisitors) {
        this.categoryId = categoryId;
        this.name = name;
        this.visitors = visitors;
        this.maxVisitors = maxVisitors;
        this.parentCategory = parentCategory;
        this.rooms = new ArrayList<Room>();
        this.categories = new ArrayList<Category>();
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public int getVisitors() {
        if (this.visitors == -1) {
            return this.rooms.stream().mapToInt(r -> r.visitors).sum();
        }

        return this.visitors;
    }

    public int getMaxVisitors() {
        if (this.maxVisitors == -1) {
            return this.rooms.stream().mapToInt(r -> r.maxVisitors).sum();
        }

        return this.maxVisitors;
    }
}
