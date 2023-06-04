package com.example.doanciclerk.dto.models;

public class Order_DTO_Model {
    private String id, storeID, customerID;

    public Order_DTO_Model() {}

    public Order_DTO_Model(String id, String storeID, String customerID) {
        this.id = id;
        this.storeID = storeID;
        this.customerID = customerID;
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
}
