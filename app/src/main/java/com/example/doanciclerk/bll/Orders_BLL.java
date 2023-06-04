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

public class Orders_BLL {
    DatabaseHelper db;

    OrderDetails_BLL orderDetails_bll;
    Goods_BLL goods_bll;

    public Orders_BLL(Context context) {
        db = new DatabaseHelper(context, "db_a", null, 1);

        goods_bll = new Goods_BLL(context);
        this.orderDetails_bll = new OrderDetails_BLL(context);
    }

    public String getOrderID_Auto(){
        String sql = "SELECT * FROM Orders;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, null);
        String newID;

        int i = 1;
        while(true)
        {
            newID = "OR_" + i;

            while(c.moveToNext()){
                if(newID.equals(c.getString(0))) {
                    break;
                }

                if(c.isLast()) {
                    c.close();
                    db.close();
                    return newID;
                }
            }

            c.moveToFirst();
            i++;
        }
    }
    public Order_DTO findOrderByID(String id){
        String sql = "SELECT * FROM Orders WHERE ID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {id});

        if(c.moveToFirst()) {
            Order_Details_DTO od = orderDetails_bll.findOrderDetailsByID(c.getString(0), c.getString(1));
            Order_DTO order_dto = new Order_DTO(c.getString(0), od, c.getString(1), c.getString(2));

            c.close();
            db.close();
            return order_dto;
        }

        c.close();
        db.close();
        return null;
    }

    public List<Order_DTO> getOrders_List(String storeID){
        List<Order_DTO> list = new ArrayList<>();

        String sql = "SELECT * FROM Orders WHERE StoreID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {storeID});

        if(c.moveToFirst()){
            Order_Details_DTO od = orderDetails_bll.findOrderDetailsByID(c.getString(0), c.getString(1));
            list.add(new Order_DTO(c.getString(0), od, c.getString(1), c.getString(2)));

            while (c.moveToNext()){
                Order_Details_DTO odd = orderDetails_bll.findOrderDetailsByID(c.getString(0), c.getString(1));
                list.add(new Order_DTO(c.getString(0), odd, c.getString(1), c.getString(2)));
            }
        }

        c.close();
        db.close();
        return list;
    }

    public void deleteOrder(String id){
        db.getWritableDatabase().delete("Orders", "ID = ?", new String[] {id});
        db.getWritableDatabase().delete("OrdersDetails", "OrderID = ?", new String[] {id});
        db.close();
    }

    public void addOrder(Order_DTO o) {
        ContentValues values = new ContentValues();
        values.put("ID", o.getId());
        values.put("StoreID", o.getStoreID());
        values.put("CustomerID", o.getCustomerID());

        db.getWritableDatabase().insert("Orders", null, values);

        List<Goods_DTO> goodsList = o.getGoodsList();
        List<Integer> amountList = o.getAmountList();

        for (int i = 0; i < goodsList.size(); i++) {
            ContentValues values1 = new ContentValues();
            values1.put("OrderID", o.getId());
            values1.put("GoodsID", goodsList.get(i).getId());
            values1.put("Amount", amountList.get(i));

            db.getWritableDatabase().insert("OrdersDetails", null, values1);
        }
        db.close();
    }
}
