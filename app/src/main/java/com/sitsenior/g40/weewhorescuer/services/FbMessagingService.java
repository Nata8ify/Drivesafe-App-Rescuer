package com.sitsenior.g40.weewhorescuer.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;

import java.sql.Date;
import java.util.Map;

/**
 * Created by PNattawut on 01-May-17.
 */

public class FbMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(Profile.getInsatance() == null){return;}
        super.onMessageReceived(remoteMessage);
        for (Map.Entry<String, String> data : remoteMessage.getData().entrySet()){
            Log.d("Entry ", data.getKey()+" : "+data.getValue());
        }
        Accident selectedAccident = new Accident();
        selectedAccident.setAccCode(remoteMessage.getData().get("accCode").charAt(0));
        selectedAccident.setAccidentId(Long.valueOf(remoteMessage.getData().get("accidentId")));
        selectedAccident.setAccType(Byte.valueOf(remoteMessage.getData().get("accType")));
        selectedAccident.setDate(Date.valueOf(remoteMessage.getData().get("date")));
        selectedAccident.setLatitude(Float.valueOf(remoteMessage.getData().get("latitude")));
        selectedAccident.setLongtitude(Float.valueOf(remoteMessage.getData().get("longitude")));
        selectedAccident.setUserId(Long.valueOf(remoteMessage.getData().get("userId")));
        selectedAccident.setTime(remoteMessage.getData().get("time"));
        Log.d("selectedAccident", selectedAccident.toString());
        AccidentFactory.getInstance(null).setSelectAccident(selectedAccident);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
