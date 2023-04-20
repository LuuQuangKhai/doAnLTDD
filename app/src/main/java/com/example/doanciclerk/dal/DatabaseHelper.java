package com.example.doanciclerk.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.doanciclerk.dto.Account_DTO;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlAccount = "CREATE TABLE IF NOT EXISTS Account (ID Text Not Null, Password Text Not Null, AccountType Integer, Primary Key(ID, AccountType));";
        String sqlStore = "CREATE TABLE IF NOT EXISTS Store (ID Text Not Null Primary Key, Name Text Not Null, Address Text,  Wallet Real);";
        String sqlGoods = "CREATE TABLE IF NOT EXISTS Goods (ID Text Not Null Primary Key, Image Integer, Name Text Not Null, Amount Integer, Price Real, StoreID Text Not Null, Constraint fk_store_goods Foreign Key (StoreID) References Store(ID));";
        String sqlOrders = "CREATE TABLE IF NOT EXISTS Orders (ID Text Not Null Primary Key, PriceSum Real, GoodsID Text Not Null, GoodsAmount Integer Not Null, GoodsSum Real, StoreID Text Not Null, Constraint fk_store_orders Foreign Key (StoreID) References Store(ID), Constraint fk_goods Foreign Key (GoodsID) References Goods(ID));";
        String sqlCustomer = "CREATE TABLE IF NOT EXISTS Customer (ID Text Not Null Primary Key, Name Text Not Null, Address Text, OrderID Text Not Null, Wallet Real, Constraint fk_order Foreign Key (OrderID) References Orders(ID));";


        db.execSQL(sqlAccount + sqlStore + sqlGoods + sqlOrders + sqlCustomer);

        initData_Account(getWritableDatabase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlAccount = "DROP TABLE IF EXISTS Account;";
        String sqlStore = "DROP TABLE IF EXISTS Store;";
        String sqlGoods = "DROP TABLE IF EXISTS Goods;";
        String sqlOrders = "DROP TABLE IF EXISTS Orders;";
        String sqlCustomer = "DROP TABLE IF EXISTS Customer;";

        db.execSQL(sqlCustomer + sqlOrders + sqlGoods + sqlStore + sqlAccount);

        onCreate(db);
    }


    //Login
    private int count_Account(SQLiteDatabase db){
        String sql = "SELECT * FROM Account;";
        Cursor c = db.rawQuery(sql, null);

        return c.getCount();
    }

    public void initData_Account(SQLiteDatabase db){
        if(count_Account(db) == 0){
            ContentValues values = new ContentValues();

            values.put("ID", "asd");
            values.put("Password", "123");
            values.put("AccountType", "0");

            db.insert("Account", null, values);
        }
    }

    public boolean checkLogin(SQLiteDatabase db, Account_DTO account){
        String sql = "SELECT * FROM Account WHERE ID = ? AND Password = ? AND AccountType = ?;";
        Cursor c = db.rawQuery(sql, new String[] {account.getId(), account.getPassword(), String.valueOf(account.getAccountType())});

        if(c.moveToFirst())
            return true;

        return false;
    }
}
