package com.dan.traderevmobilechallenge.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.dan.traderevmobilechallenge.components.NetworkStateChangeReceiver;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Created by Dan Kim on 2019-04-28
 */
public class CustomApp extends Application {

    private static Context applicationContext;
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        registerForNetworkChangeEvents(this);
    }

    public static Context getContext(){
        return applicationContext;
    }
    private static void registerForNetworkChangeEvents(final Context context) {
        NetworkStateChangeReceiver networkStateChangeReceiver = new NetworkStateChangeReceiver();
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
    }
}
