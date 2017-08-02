package com.sitsenior.g40.weewhorescuer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PNattawut on 23-Apr-17.
 */

public class SettingUtils {

    /* Checking there is the Connectivity on This Application. */
    public static boolean isNetworkConnected(Context context){
        return ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    /* Aasking User For Granting Some Permission  */
    public static void requestPermission(Context context){
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, REQ_CODE_ACCESS_FINE_LOCATION);
    }
        public static final int REQ_CODE_ACCESS_FINE_LOCATION = 0xf0;
        public static final int REQ_CODE_ACCESS_COARSE_LOCATION = 0xf1;

}
