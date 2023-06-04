package com.example.doanciclerk.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    public static IAIOAPI Create(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lythainguyen.bsite.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(IAIOAPI.class);
    }
}
