package com.example.doanciclerk.dto.models;

import com.example.doanciclerk.dto.Account_DTO;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.dto.Order_Details_DTO;
import com.example.doanciclerk.dto.Store_DTO;
import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AIOModel
{
    public List<Account_DTO> accountModels;
    public List<Store_DTO> storeModels;
    public List<Customer_DTO> customerModels;
    public List<Goods_DTO> goodsModels;
    public List<Order_DTO_Model> orderModels;
    public List<OrdersDetail_DTO_Model> ordersDetailModels;

    public Map<String, Object> Dict;

    public AIOModel()
    {
        Dict = new LinkedTreeMap<>();
    }

    public void setDict()
    {
        Dict.put("accounts", accountModels);
        Dict.put("stores", storeModels);
        Dict.put("customers", customerModels);
        Dict.put("goods", goodsModels);
        Dict.put("orders", orderModels);
        Dict.put("ordersdetails", ordersDetailModels);
    }
}
