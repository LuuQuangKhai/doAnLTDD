package com.example.doanciclerk.bll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;

import java.util.ArrayList;
import java.util.List;

public class Orders_BLL {
    DatabaseHelper db;

    Goods_BLL goods_bll;

    public Orders_BLL(Context context) {
        db = new DatabaseHelper(context, "db_a", null, 1);

        goods_bll = new Goods_BLL(context);
    }

    public Order_DTO findOrderByID(String id){
        String sql = "SELECT * FROM Orders WHERE ID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {id});

        if(c.moveToFirst()) {
            Goods_DTO g = goods_bll.findGoodsByID(c.getString(1), c.getString(3));

            return new Order_DTO(c.getString(0), c.getString(4), g, c.getInt(2));
        }

        db.close();
        return null;
    }

    public List<Order_DTO> getOrders_List(String storeID){
        List<Order_DTO> list = null;

        String sql = "SELECT * FROM Orders WHERE StoreID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {storeID});

        if(c.moveToFirst()){
            list = new ArrayList<>();

            Goods_DTO g = goods_bll.findGoodsByID(c.getString(2), c.getString(5));
            list.add(new Order_DTO(c.getString(0), c.getString(6), g, c.getInt(3)));

            while (c.moveToNext()){
                Goods_DTO gg = goods_bll.findGoodsByID(c.getString(2), c.getString(5));
                list.add(new Order_DTO(c.getString(0), c.getString(6), gg, c.getInt(3)));
            }
        }

        db.close();
        return list;
    }

    public void deleteOrder(String id){
        db.getWritableDatabase().delete("Orders", "ID = ?", new String[] {id});
        db.close();
    }

    public void addOrder(Order_DTO o){
        ContentValues values = new ContentValues();
        values.put("ID", o.getId());
        values.put("GoodsID", o.getGoodsID());
        values.put("GoodsAmount", o.getGoodsAmount());
        values.put("StoreID", o.getStoreID());
        values.put("CustomerID", o.getCustomerID());

        db.getWritableDatabase().insert("Orders", null, values);
        db.close();
    }
}
