package com.artificial.brain.quantumwheel;

import android.app.Application;

import com.artificial.brain.quantumwheel.api.ApiManager;

public class MainApplication extends Application {

    public static ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance();
    }
}
