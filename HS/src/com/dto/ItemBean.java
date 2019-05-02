package com.dto;

public class ItemBean {
    private int itemid;
    private String picture;
    private float price;
    private String itemname;
    private String username;
    private String description;

    public ItemBean(int itemid, String picture, float price, String itemname, String username, String description) {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
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
}
