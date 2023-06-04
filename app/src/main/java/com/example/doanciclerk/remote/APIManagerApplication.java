package com.example.doanciclerk.remote;

import android.app.Application;

public class APIManagerApplication extends Application {
    public static AIOApiManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = AIOApiManager.getInstance();
    }
}
