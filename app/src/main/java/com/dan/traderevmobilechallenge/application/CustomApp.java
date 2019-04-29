package com.dan.traderevmobilechallenge.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dan Kim on 2019-04-28
 */
public class CustomApp extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }

    public static Context getContext(){
        return applicationContext;
    }
}
