package com.example.doanciclerk.remote;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AIOApiManager {
    private static IAIOAPI service;
    private static AIOApiManager aioApiManager;

    private AIOApiManager(){
        service = RetrofitService.Create();
    }

    public static AIOApiManager getInstance(){
        if(aioApiManager == null){
            aioApiManager = new AIOApiManager();
        }
        return aioApiManager;
    }

    public void getAIO(Callback<Map<String, Object>> callback){
        Call<Map<String, Object>> call = service.getAIO();
        call.enqueue(callback);
    }
}
