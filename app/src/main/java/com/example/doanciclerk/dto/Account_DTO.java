package com.example.doanciclerk.dto;

import java.io.Serializable;

public class Account_DTO implements Serializable {
    private String id, password;
    private int accountType;

    public Account_DTO(String id, String password, int accountType){
        this.id = id;
        this.password = password;
        this.accountType = accountType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}
