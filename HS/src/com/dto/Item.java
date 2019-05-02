package com.dto;

import java.sql.Blob;

public class Item {
    private int itemid;
    private Blob picture;
    private float price;
    private String itemname;
    private String username;
    private String description;

    public Item(int itemid, Blob picture, float price, String itemname, String username, String description) {
        this.itemid = itemid;
        this.picture = picture;
        this.price = price;
        this.itemname = itemname;
        this.username = username;
        this.description = description;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemid=" + itemid +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", itemname='" + itemname + '\'' +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
