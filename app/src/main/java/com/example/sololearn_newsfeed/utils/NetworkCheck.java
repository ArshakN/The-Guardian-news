package com.example.sololearn_newsfeed.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public final class NetworkCheck {

    public NetworkCheck() {
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();


    }
}
