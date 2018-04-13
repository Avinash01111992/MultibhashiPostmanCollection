package com.example.avinash.postmancollection.Utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by avinash on 13/4/18.
 */

public class Connectivity {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void disableReceiver(Context context, ComponentName componentName) {

        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager
                .COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public static void enableReceiver(Context context, ComponentName componentName) {

//        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//        context.registerReceiver(networkStateReceiver, intentFilter);

        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager
                .COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

}