package com.dan.traderevmobilechallenge.components;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * This is designed to broadcast a change of network status to MainActivity's ViewModel
 * {@link com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel}
 * so  MainActivity will show Toasty message accordingly
 *
 * Created by Dan Kim on 2019-05-01
 */
public class NetworkStateChangeReceiver extends BroadcastReceiver {

    public static final String NETWORK_AVAILABLE_ACTION = "com.dan.traderevmobilechallenge.components.NETWORK_AVAILABLE_ACTION";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnect(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
    }

    private boolean isConnect(Context context) {

        try {
            if (context != null) {

                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
