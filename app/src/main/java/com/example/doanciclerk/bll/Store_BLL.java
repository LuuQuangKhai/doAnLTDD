package com.example.doanciclerk.bll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Store_DTO;

import java.util.ArrayList;
import java.util.List;

public class Store_BLL {
    DatabaseHelper db;

    public Store_BLL(Context context) {
        db = new DatabaseHelper(context, "db_a", null, 1);
    }

    public Store_DTO findStoreByID(String id){
        String sql = "SELECT * FROM Stores WHERE ID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {id});

        if(c.moveToFirst()){
            Store_DTO store_dto = new Store_DTO(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3));
            c.close();
            db.close();
            return store_dto;

        }

        c.close();
        db.close();
        return null;
    }

    public void updateStore(Store_DTO store_dto){
        ContentValues values = new ContentValues();

        values.put("ID", store_dto.getId());
        values.put("Name", store_dto.getName());
        values.put("Address", store_dto.getAddress());
        values.put("Wallet", store_dto.getWallet());

        db.getWritableDatabase().update("Stores", values, "ID = ?", new String[]{store_dto.getId()});
        db.close();
    }

    public void addStore(Store_DTO store_dto){
        ContentValues values = new ContentValues();
        values.put("ID", store_dto.getId());
        values.put("Name", store_dto.getName());
        values.put("Address", store_dto.getAddress());
        values.put("Wallet", store_dto.getWallet());

        db.getWritableDatabase().insert("Stores", null, values);
        db.close();
    }

    public List<Store_DTO> getStore_List_All(){
        List<Store_DTO> list = new ArrayList<>();

        String sql = "SELECT * FROM Stores;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext())
            list.add(new Store_DTO(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3)));

        c.close();
        db.close();
        return list;
    }
}
