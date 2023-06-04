package com.example.doanciclerk.dto;

import java.io.Serializable;

public class Goods_DTO implements Serializable {
    private String id, image, name, description, storeID;
    private double price, discount;

    public Goods_DTO(String id, String image, String name, String description, String storeID, double discount, double price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.storeID = storeID;
        this.discount = discount;
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

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
