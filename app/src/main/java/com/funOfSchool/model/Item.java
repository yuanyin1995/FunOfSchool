package com.funOfSchool.model;

/**
 * Created by Aiome on 2016/12/27.
 */

public class Item {
    private String itemId;
    private String url;
    private String itmeName;
    private double price;

    public Item(String itemId, String url, String itmeName, double price) {
        this.itemId = itemId;
        this.url = url;
        this.itmeName = itmeName;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getItmeName() {
        return itmeName;
    }

    public void setItmeName(String itmeName) {
        this.itmeName = itmeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
