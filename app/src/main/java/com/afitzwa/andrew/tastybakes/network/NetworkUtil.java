package com.afitzwa.andrew.tastybakes.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andrew on 9/13/17.
 *
 * Used to check network settings
 */

public final class NetworkUtil {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }
}
