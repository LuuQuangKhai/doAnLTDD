package com.example.doanciclerk.dto;

import com.example.doanciclerk.bll.Goods_BLL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order_DTO implements Serializable {
    private String id, storeID, customerID;

    private Order_Details_DTO orderDetails;
    private double priceSum;

    public Order_DTO(String id, Order_Details_DTO orderDetails, String storeID, String customerID) {
        this.id = id;

        this.orderDetails = orderDetails;

        this.storeID = storeID;
        this.customerID = customerID;

        this.priceSum = 0;

        sum();
    }

    public void sum(){
        List<Goods_DTO> goodsList = new ArrayList<>();
        if(orderDetails != null && orderDetails.getGoodslist() != null)
            goodsList = orderDetails.getGoodslist();

        List<Integer> amountList = new ArrayList<>();
        if (orderDetails != null && orderDetails.getAmountList() != null)
            amountList = orderDetails.getAmountList();

        this.priceSum = 0;
        for (int i = 0; i < goodsList.size(); i++) {
            double discount = goodsList.get(i).getDiscount();

            if(discount > 0)
                this.priceSum += discount * amountList.get(i);
            else
                this.priceSum += goodsList.get(i).getPrice() * amountList.get(i);
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(double priceSum) {
        this.priceSum = priceSum;
    }

    public List<Goods_DTO> getGoodsList() {
        if (orderDetails != null)
            return orderDetails.getGoodslist();

        return null;
    }

    public void setGoodsList(List<Goods_DTO> goodsList) {
        orderDetails.setGoodslist(goodsList);
    }

    public List<Integer> getAmountList() {
        if (orderDetails != null)
            return orderDetails.getAmountList();

        return null;
    }

    public void setAmount(List<Integer> amountList) {
        orderDetails.setAmountList(amountList);
    }
}
