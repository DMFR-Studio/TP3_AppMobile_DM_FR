package com.example.tp3_dm_fr;

import android.app.Application;

public class GlobalClass extends Application {
    public static final String BASE_URL = "http://10.0.2.2:8081";
    private static GlobalClass singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static GlobalClass getInstance() {
        return singleton;
    }
}
