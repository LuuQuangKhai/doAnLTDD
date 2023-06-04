package com.example.doanciclerk.dto.models;

public class OrdersDetail_DTO_Model {
    private String orderID, goodsID;
    private int amount;

    public OrdersDetail_DTO_Model() {}

    public OrdersDetail_DTO_Model(String orderID, String goodsID, int amount) {
        this.orderID = orderID;
        this.goodsID = goodsID;
        this.amount = amount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
