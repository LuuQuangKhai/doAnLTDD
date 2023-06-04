package com.example.doanciclerk.bll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.dto.Order_Details_DTO;

import java.util.ArrayList;
import java.util.List;

public class OrderDetails_BLL {
    DatabaseHelper db;

    Goods_BLL goods_bll;

    public OrderDetails_BLL(Context context) {
        db = new DatabaseHelper(context, "db_a", null, 1);

        goods_bll = new Goods_BLL(context);
    }

    public void addToOrderDetails(Order_DTO o, Goods_DTO goods_dto, int amount){
        o.getGoodsList().add(goods_dto);
        o.getAmountList().add(amount);

        o.sum();
    }
    public void deleteFromOrderDetails(String id, String goods_id){
        String sql = "SELECT * FROM OrdersDetails WHERE OrderID = ? AND GoodsID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {id, goods_id});

        if(c.moveToFirst()){
            db.getWritableDatabase().delete("OrdersDetails", "OrderID = ? AND GoodsID = ?", new String[] {id, goods_id});
        }

        c.close();
        db.close();
    }

    public Order_Details_DTO findOrderDetailsByID(String id, String storeID){
        String sql = "SELECT * FROM OrdersDetails WHERE OrderID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {id});

        Order_Details_DTO o = null;

        if(c.getCount() > 0){
            o = new Order_Details_DTO(id);
            while(c.moveToNext()){
                o.getGoodslist().add(goods_bll.findGoodsByID(c.getString(1), storeID));
                o.getAmountList().add(c.getInt(2));
            }
        }

        c.close();
        db.close();
        return o;
    }
}
