package com.sitsenior.g40.weewhorescuer.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by PNattawut on 01-May-17.
 */

public class FbInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("incident");
        Log.d("TOKEN GEN ", "Refreshed token: " + refreshedToken);
    }
}
