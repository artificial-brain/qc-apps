package com.artificial.brain.quantumwheel;

import android.app.Application;

import com.artificial.brain.quantumwheel.api.ApiManager;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainApplication extends Application {

    public static ApiManager apiManager;
    public static FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
