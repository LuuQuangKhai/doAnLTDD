package com.example.doanciclerk.dto;

import android.app.Notification;

import java.io.Serializable;
import java.util.Stack;

public class Customer_DTO implements Serializable {
    private String id, name, address;
    private double wallet;

    public Customer_DTO(String id, String name, String address, double wallet) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.wallet = wallet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
}
