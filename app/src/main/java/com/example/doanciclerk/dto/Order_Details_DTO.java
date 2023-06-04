package com.example.doanciclerk.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order_Details_DTO implements Serializable {
    private String orderID;

    private List<Goods_DTO> goodslist;
    private List<Integer> amountList;

    public Order_Details_DTO(String ordersID) {
        this.orderID = ordersID;

        this.goodslist = new ArrayList<>();
        this.amountList = new ArrayList<>();
    }

    public String getOrdersID() {
        return orderID;
    }

    public void setOrdersID(String orderID) {
        this.orderID = orderID;
    }

    public List<Goods_DTO> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<Goods_DTO> goodslist) {
        this.goodslist = goodslist;
    }

    public List<Integer> getAmountList() {
        return amountList;
    }

    public void setAmountList(List<Integer> amountList) {
        this.amountList = amountList;
    }
}
