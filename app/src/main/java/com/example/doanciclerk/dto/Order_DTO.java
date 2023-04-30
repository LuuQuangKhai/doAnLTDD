package com.example.doanciclerk.dto;

public class Order_DTO {
    private String id, goodsID, storeID, customerID;
    private int goodsAmount;
    private double priceSum;

    public Order_DTO(String id, String customerID, Goods_DTO goods, int goodsAmount) {
        this.id = id;
        this.goodsID = goods.getId();
        this.storeID = goods.getStoreID();
        this.customerID = customerID;
        this.goodsAmount = goodsAmount;

        this.priceSum = goods.getPrice() * this.goodsAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public double getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(double priceSum) {
        this.priceSum = priceSum;
    }
}
