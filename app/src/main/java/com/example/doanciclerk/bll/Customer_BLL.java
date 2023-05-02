package com.example.doanciclerk.bll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Goods_DTO;

import java.util.ArrayList;
import java.util.List;

public class Customer_BLL {
    DatabaseHelper db;

    public Customer_BLL(Context context) {
        db = new DatabaseHelper(context, "db_a", null, 1);
    }

    public Customer_DTO findCustomerByID(String id){
        String sql = "SELECT * FROM Customers WHERE ID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {id});

        if(c.moveToFirst())
            return new Customer_DTO(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3));

        db.close();
        return null;
    }

    public void addCustomer(Customer_DTO customer_dto){
        ContentValues values = new ContentValues();
        values.put("ID", customer_dto.getId());
        values.put("Name", customer_dto.getName());
        values.put("Address", customer_dto.getAddress());
        values.put("Wallet", customer_dto.getWallet());

        db.getWritableDatabase().insert("Customers", null, values);
    }
}
