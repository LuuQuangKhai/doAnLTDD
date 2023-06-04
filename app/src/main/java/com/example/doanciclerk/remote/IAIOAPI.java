package com.example.doanciclerk.remote;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IAIOAPI {
    @GET("aio")
    Call<Map<String, Object>>  getAIO();
}
