package com.example.doanciclerk.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.doanciclerk.dto.Account_DTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlAccount = "CREATE TABLE IF NOT EXISTS Accounts (ID Text Not Null Primary Key, Password Text Not Null, AccountType Integer);";
        String sqlStore = "CREATE TABLE IF NOT EXISTS Stores (ID Text Not Null Primary Key, Name Text Not Null, Address Text,  Wallet Real);";
        String sqlGoods = "CREATE TABLE IF NOT EXISTS Goods (ID Text Not Null Primary Key, Image Integer, Name Text Not Null, Amount Integer, Price Real, Description Text, StoreID Text Not Null, Constraint fk_store_goods Foreign Key (StoreID) References Store(ID));";
        String sqlOrders = "CREATE TABLE IF NOT EXISTS Orders (ID Text Not Null, PriceSum Real, GoodsID Text Not Null, GoodsAmount Integer Not Null, GoodsSum Real, StoreID Text Not Null, CustomerID Text Not Null, Constraint fk_store_orders Foreign Key (StoreID) References Store(ID), Constraint fk_customer_orders Foreign Key (CustomerID) References Customer(ID), Constraint fk_goods Foreign Key (GoodsID) References Goods(ID));";
        String sqlCustomer = "CREATE TABLE IF NOT EXISTS Customers (ID Text Not Null Primary Key, Name Text Not Null, Address Text, Wallet Real);";

        db.execSQL(sqlAccount);
        db.execSQL(sqlStore);
        db.execSQL(sqlGoods);
        db.execSQL(sqlOrders);
        db.execSQL(sqlCustomer);

        if (count_Account(db) == 0)
            readJSON(db, "base_data.json");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlAccount = "DROP TABLE IF EXISTS Accounts;";
        String sqlStore = "DROP TABLE IF EXISTS Stores;";
        String sqlGoods = "DROP TABLE IF EXISTS Goods;";
        String sqlOrders = "DROP TABLE IF EXISTS Orders;";
        String sqlCustomer = "DROP TABLE IF EXISTS Customers;";

        db.execSQL(sqlAccount);
        db.execSQL(sqlStore);
        db.execSQL(sqlGoods);
        db.execSQL(sqlOrders);
        db.execSQL(sqlCustomer);

        onCreate(db);
    }

    public void readJSON(SQLiteDatabase db,String jsonFileName){
        try {
            InputStream inputStream = context.getAssets().open(jsonFileName);
            int size = inputStream.available();
            byte[] b = new byte[size];
            inputStream.read(b);
            JSONObject o = new JSONObject(new String(b));

            //Account
            JSONArray accounts = o.getJSONArray("accounts");

            for (int i = 0; i < accounts.length(); i++) {
                JSONObject g = accounts.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put("ID", g.getString("id"));
                values.put("Password", g.getString("password"));
                values.put("AccountType", g.getInt("accountType"));

                db.insert("Accounts", null, values);
            }

            //Store
            JSONArray stores = o.getJSONArray("stores");

            for (int i = 0; i < stores.length(); i++) {
                JSONObject g = stores.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put("ID", g.getString("id"));
                values.put("Name", g.getString("name"));
                values.put("Address", g.getString("address"));
                values.put("Wallet", g.getDouble("wallet"));

                db.insert("Stores", null, values);
            }

            //Customer
            JSONArray customers = o.getJSONArray("customers");

            for (int i = 0; i < customers.length(); i++) {
                JSONObject g = customers.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put("ID", g.getString("id"));
                values.put("Name", g.getString("name"));
                values.put("Address", g.getString("address"));
                values.put("Wallet", g.getDouble("wallet"));

                db.insert("Customers", null, values);
            }

            //Goods
            JSONArray goods = o.getJSONArray("goods");

            for (int i = 0; i < goods.length(); i++) {
                JSONObject g = goods.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put("ID", g.getString("id"));
                values.put("Image", g.getString("image"));
                values.put("Name", g.getString("name"));
                values.put("Description", g.getString("description"));
                values.put("StoreID", g.getString("storeID"));
                values.put("Amount", g.getInt("amount"));
                values.put("Price", g.getDouble("price"));

                db.insert("Goods", null, values);
            }

            //Orders
            JSONArray orders = o.getJSONArray("orders");

            for (int i = 0; i < orders.length(); i++) {
                JSONObject g = orders.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put("ID", g.getString("id"));
                values.put("GoodsID", g.getString("goodsID"));
                values.put("GoodsAmount", g.getInt("goodsAmount"));
                values.put("StoreID", g.getString("storeID"));
                values.put("CustomerID", g.getString("customerID"));

                db.insert("Orders", null, values);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //Login
    private int count_Account(SQLiteDatabase db){
        String sql = "SELECT * FROM Accounts;";
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
        String sql = "SELECT * FROM Accounts WHERE ID = ? AND Password = ? AND AccountType = ?;";
        Cursor c = db.rawQuery(sql, new String[] {account.getId(), account.getPassword(), String.valueOf(account.getAccountType())});

        if(c.moveToFirst())
            return true;

        return false;
    }

    public Account_DTO findAccountByID(SQLiteDatabase db, String id, int account_type){
        String sql = "SELECT * FROM Accounts WHERE ID = ? AND AccountType = ?;";
        Cursor c = db.rawQuery(sql, new String[] {id, String.valueOf(account_type)});

        if(c.moveToFirst())
            return new Account_DTO(c.getString(0), c.getString(1), c.getInt(2));

        return null;
    }

    //Goods
}
