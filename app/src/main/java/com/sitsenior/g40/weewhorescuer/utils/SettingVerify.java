package com.sitsenior.g40.weewhorescuer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PNattawut on 23-Apr-17.
 */

public class SettingVerify {
    public static boolean isNetworkConnected(Context context){
        return ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
