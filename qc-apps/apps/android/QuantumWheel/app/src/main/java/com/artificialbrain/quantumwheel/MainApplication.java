package com.artificialbrain.quantumwheel;

import android.app.Application;

import com.artificialbrain.quantumwheel.api.ApiManager;

public class MainApplication extends Application {

    public static ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance();
    }
}
