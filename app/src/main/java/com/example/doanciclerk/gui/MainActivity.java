package com.example.doanciclerk.gui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Account_DTO;
import com.example.doanciclerk.remote.IAIOAPI;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectAPI();
        db = new DatabaseHelper(getApplicationContext(), "db_a", null, 1);

        SharedPreferences sharedPreferences = getSharedPreferences("Logged Account", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("Account ID") && sharedPreferences.contains("Account Type")) {
            String id = sharedPreferences.getString("Account ID", "");
            int accountType = sharedPreferences.getInt("Account Type", -1);

            Account_DTO acc = db.findAccountByID(db.getReadableDatabase(), id, accountType);

            Intent i;

            if(acc.getAccountType() == 0){
                i = new Intent(this, ShopMainActivity.class);
            }else{
                i = new Intent(this, CustomerMainActivity.class);
            }

            i.putExtra("Account", acc);
            startActivity(i);
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        db.close();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveJson(Map<String, Object> aio, Context context){
        Map<String, Object> map = aio;

        JSONObject bigJsonObject = new JSONObject();

        JSONArray accounts = new JSONArray();
        List<LinkedTreeMap<String, Object>> account_dtoList = (List<LinkedTreeMap<String, Object>>) map.get("accounts");
        account_dtoList.forEach(a -> {
            JSONObject o = new JSONObject();

            try {
                o.put("id", a.get("id"));
                o.put("password", a.get("password"));

                Double d = (Double) a.get("accountType");
                o.put("accountType", Math.round(d));

                accounts.put(o);
            } catch (JSONException e) {}
        });

        JSONArray stores = new JSONArray();
        List<LinkedTreeMap<String, Object>> store_dtoList = (List<LinkedTreeMap<String, Object>>) map.get("stores");
        store_dtoList.forEach(a -> {
            JSONObject o = new JSONObject();

            try {
                o.put("id", a.get("id"));
                o.put("name", a.get("name"));
                o.put("address", a.get("address"));
                o.put("wallet", Double.parseDouble(a.get("wallet").toString()));

                stores.put(o);
            } catch (JSONException e) {}
        });

        JSONArray customers = new JSONArray();
        List<LinkedTreeMap<String, Object>> customer_dtoList = (List<LinkedTreeMap<String, Object>>) map.get("customers");
        customer_dtoList.forEach(a -> {
            JSONObject o = new JSONObject();

            try {
                o.put("id", a.get("id"));
                o.put("name", a.get("name"));
                o.put("address", a.get("address"));
                o.put("wallet", Double.parseDouble(a.get("wallet").toString()));

                customers.put(o);
            } catch (JSONException e) {}
        });

        JSONArray goods = new JSONArray();
        List<LinkedTreeMap<String, Object>> goods_dtoList = (List<LinkedTreeMap<String, Object>>) map.get("goods");
        goods_dtoList.forEach(a -> {
            JSONObject o = new JSONObject();

            try {
                o.put("id", a.get("id"));
                o.put("image", a.get("image"));
                o.put("name", a.get("name"));
                o.put("description", a.get("description"));
                o.put("discount", Double.parseDouble(a.get("discount").toString()));
                o.put("price", Double.parseDouble(a.get("price").toString()));
                o.put("storeID", a.get("storeID"));

                goods.put(o);
            } catch (JSONException e) {}
        });

        JSONArray orders = new JSONArray();
        List<LinkedTreeMap<String, Object>> order_dtoList = (List<LinkedTreeMap<String, Object>>) map.get("orders");
        order_dtoList.forEach(a -> {
            JSONObject o = new JSONObject();

            try {
                o.put("id", a.get("id"));
                o.put("storeID", a.get("storeID"));
                o.put("customerID", a.get("customerID"));

                orders.put(o);
            } catch (JSONException e) {}
        });

        JSONArray ordersdetails = new JSONArray();
        List<LinkedTreeMap<String, Object>> ordersdetail_dtoList = (List<LinkedTreeMap<String, Object>>) map.get("ordersdetails");
        ordersdetail_dtoList.forEach(a -> {
            JSONObject o = new JSONObject();

            try {
                o.put("orderID", a.get("orderID"));
                o.put("goodsID", a.get("goodsID"));

                Double d = (Double) a.get("amount");
                o.put("amount", Math.round(d));

                ordersdetails.put(o);
            } catch (JSONException e) {}
        });

        try {
            bigJsonObject.put("accounts", accounts);
            bigJsonObject.put("stores", stores);
            bigJsonObject.put("customers", customers);
            bigJsonObject.put("goods", goods);
            bigJsonObject.put("orders", orders);
            bigJsonObject.put("ordersdetails", ordersdetails);

            FileWriter writer = new FileWriter(this.getFilesDir() +"/base_data1.json");
            writer.write(bigJsonObject.toString());
            writer.close();
        } catch (Exception e) {}
    }

    private void connectAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lythainguyen.bsite.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IAIOAPI service = retrofit.create(IAIOAPI.class);
        Call<Map<String, Object>> call = service.getAIO();
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d("Success", response.body().toString());
                saveJson(response.body(), getApplicationContext());
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.d("Fail", "wot");
            }
        });
    }
}