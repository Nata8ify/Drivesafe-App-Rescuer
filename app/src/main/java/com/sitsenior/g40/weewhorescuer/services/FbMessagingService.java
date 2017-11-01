package com.sitsenior.g40.weewhorescuer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.models.extra.AccidentBrief;

import java.sql.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

/**
 * Created by PNattawut on 01-May-17.
 */

public class FbMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("onMessageReceived ", "onMessageReceivedonMessageReceivedonMessageReceivedonMessageReceivedonMessageReceived" );
        String clickAction = remoteMessage.getNotification().getClickAction();
        String bLocation =(remoteMessage.getData().get("report_from"));
        Log.d("clickAction ", clickAction );
        Intent notiIntent = new Intent(clickAction);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setSmallIcon(R.mipmap.ic_launcher);
        nBuilder.setContentTitle(getString(R.string.fbservice_incidet_alert));
        nBuilder.setContentText(getString(R.string.fbservice_incidet_report_from).concat(bLocation));
        nBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT));
        nBuilder.setVibrate(new long[] { 1000, 1000, 1000});
        nBuilder.setSound(Uri.parse("android.resource://".concat(getPackageName()).concat("/")+R.raw.bongo));
        Notification notification = nBuilder.build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
        //if(Profile.getInsatance().getFirstName() == null){return;}
        /*if(realm == null){
            Realm.init(this);
            realm = Realm.getDefaultInstance();
        }
        for (Map.Entry<String, String> data : remoteMessage.getData().entrySet()){
            Log.d("Entry ", data.getKey()+" : "+data.getValue());
        }


        selectedAccident = new Accident();
        selectedAccident.setAccCode(remoteMessage.getData().get("accCode").charAt(0));
        selectedAccident.setAccidentId(Long.valueOf(remoteMessage.getData().get("accidentId")));
        selectedAccident.setAccType(Byte.valueOf(remoteMessage.getData().get("accType")));
        selectedAccident.setDate(Date.valueOf(remoteMessage.getData().get("date")));
        selectedAccident.setLatitude(Float.valueOf(remoteMessage.getData().get("latitude")));
        selectedAccident.setLongtitude(Float.valueOf(remoteMessage.getData().get("longitude")));
        selectedAccident.setUserId(Long.valueOf(remoteMessage.getData().get("userId")));
        selectedAccident.setTime(remoteMessage.getData().get("time"));

        Log.d("selectedAccident", selectedAccident.toString());
        AccidentFactory.setSelectAccident(selectedAccident);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(AccidentBrief.class);
                realm.insert(new AccidentBrief(selectedAccident));
            }
        });*/
        vibrate();
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {

        super.onMessageSent(s);
    }

    Vibrator vibrator;
    public void vibrate(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000L);
        try {
            TimeUnit.MILLISECONDS.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vibrator.vibrate(2000L);
    }
}
