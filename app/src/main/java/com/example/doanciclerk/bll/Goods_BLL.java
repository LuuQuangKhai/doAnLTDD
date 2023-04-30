package com.example.doanciclerk.bll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Goods_DTO;

import java.util.ArrayList;
import java.util.List;

public class Goods_BLL {
    DatabaseHelper db;

    public Goods_BLL(Context context) {
        db = new DatabaseHelper(context, "db_a", null, 1);
    }

    public String getGoodsID_Auto(){
        String sql = "SELECT * FROM Goods;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, null);
        String newID;

        int i = 1;
        while(true)
        {
            newID = "GO_" + i;

            while(c.moveToNext()){
                if(newID.equals(c.getString(0))) {
                    break;
                }

                if(c.isLast())
                    return newID;
            }

            c.moveToFirst();
            i++;
        }
    }

    public Goods_DTO findGoodsByID(String id, String storeID){
        String sql = "SELECT * FROM Goods WHERE ID = ? AND StoreID = ?;";
        Cursor c = db.getReadableDatabase().rawQuery(sql, new String[] {id, storeID});

        if(c.moveToFirst())
            return new Goods_DTO(c.getString(0), c.getString(1), c.getString(2), c.getString(5), c.getString(6), c.getInt(3), c.getDouble(4));

        db.close();
        return null;
    }

    public List<Goods_DTO> getGoods_List(){
        List<Goods_DTO> list = null;

        String sql = "SELECT * FROM Goods";
        Cursor c = db.getReadableDatabase().rawQuery(sql, null);

        if(c.moveToFirst()){
            list = new ArrayList<>();
            list.add(new Goods_DTO(c.getString(0), c.getString(1), c.getString(2), c.getString(5), c.getString(6), c.getInt(3), c.getDouble(4)));

            while (c.moveToNext())
                list.add(new Goods_DTO(c.getString(0), c.getString(1), c.getString(2), c.getString(5), c.getString(6), c.getInt(3), c.getDouble(4)));
        }

        db.close();
        return list;
    }

    public void updateGoods(Goods_DTO g){
        ContentValues values = new ContentValues();
        values.put("ID", g.getId());
        values.put("Image", g.getImage());
        values.put("Name", g.getName());
        values.put("Description", g.getDescription());
        values.put("StoreID", g.getStoreID());
        values.put("Amount", g.getAmount());
        values.put("Price", g.getPrice());

        db.getWritableDatabase().update("Goods", values, "ID = ?", new String[]{g.getId()});
        db.close();
    }

    public void deleteGoods(String id){
        db.getWritableDatabase().delete("Goods", "ID = ?", new String[] {id});
        db.close();
    }

    public void addGoods(Goods_DTO g){
        ContentValues values = new ContentValues();
        values.put("ID", g.getId());
        values.put("Image", g.getImage());
        values.put("Name", g.getName());
        values.put("Description", g.getDescription());
        values.put("StoreID", g.getStoreID());
        values.put("Amount", g.getAmount());
        values.put("Price", g.getPrice());

        db.getWritableDatabase().insert("Goods", null, values);
        db.close();
    }
}
