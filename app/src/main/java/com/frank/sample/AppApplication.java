package com.frank.sample;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {

    public static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
    }
}
