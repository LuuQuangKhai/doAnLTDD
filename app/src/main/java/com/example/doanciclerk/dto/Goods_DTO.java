package com.example.doanciclerk.dto;

import java.io.Serializable;

public class Goods_DTO implements Serializable {
    private String id, image, name, description, storeID;
    private int amount;
    private double price;

    public Goods_DTO(String id, String image, String name, String description, String storeID, int amount, double price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.storeID = storeID;
        this.amount = amount;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
