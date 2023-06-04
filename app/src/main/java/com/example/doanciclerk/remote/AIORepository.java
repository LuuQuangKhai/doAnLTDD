package com.example.doanciclerk.remote;

import android.content.Context;
import android.util.JsonWriter;

import androidx.lifecycle.MutableLiveData;

import com.example.doanciclerk.dto.Account_DTO;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.dto.Order_Details_DTO;
import com.example.doanciclerk.dto.Store_DTO;
import com.example.doanciclerk.dto.models.Order_DTO_Model;
import com.example.doanciclerk.dto.models.OrdersDetail_DTO_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIORepository {
    public static volatile AIORepository instance;

    public final AIOApiManager manager;

    public final MutableLiveData<Map<String, Object>> aio = new MutableLiveData<>();

    public AIORepository(AIOApiManager manager) {
        this.manager = manager;
    }

    public static AIORepository getInstance(AIOApiManager manager){
        if(instance == null){
            instance = new AIORepository(manager);
        }
        return instance;
    }

    public MutableLiveData<Map<String, Object>> getAIO(){
        manager.getAIO(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if(response.isSuccessful()){
                    aio.setValue(response.body());
                }
                else
                    aio.postValue(null);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                aio.postValue(null);
            }
        });

        return aio;
    }
}
