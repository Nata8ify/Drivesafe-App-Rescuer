package com.sitsenior.g40.weewhorescuer.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by PNattawut on 23-Apr-17.
 */

public class SettingUtils {

    public static final int REQ_CODE_ACCESS_FINE_LOCATION = 0xf0;
    public static final int REQ_CODE_ACCESS_COARSE_LOCATION = 0xf1;

    /* Checking there is the Connectivity on This Application. */
    public static boolean isNetworkConnected(Context context){
        return ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    /* Aasking User For Granting Some Permission  */
    public static void requestPermission(Context context){
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, REQ_CODE_ACCESS_FINE_LOCATION);
    }

    /* Check if Service is Running or not*/
    public static boolean isServiceRunning(Class<?> clazz, Context context){
        for(ActivityManager.RunningServiceInfo info : ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)){
            if(clazz.getName().equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }

}
