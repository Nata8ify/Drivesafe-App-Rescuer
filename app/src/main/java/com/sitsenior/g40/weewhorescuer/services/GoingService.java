package com.sitsenior.g40.weewhorescuer.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.sitsenior.g40.weewhorescuer.CloseIncidentActivity;
import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.extra.AccidentBrief;
import com.sitsenior.g40.weewhorescuer.models.extra.ReporterProfile;

import io.realm.Realm;

/**
 * Created by PNattawut on 25-Sep-17.
 */

public class GoingService extends IntentService {

    Realm realm;

    RemoteViews notificationGoingRemoteViews;
    private AlertDialog reqCancelAlertDialog;

    private Handler handler;
    private Runnable onGoingRunnable;
    private Runnable updateSelectedIncidentRunnable;
/*
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("$$$$$", "Received");
            Toast.makeText(GoingService.this,  "play button was clicked", Toast.LENGTH_LONG).show();
        }
    };*/

    public static NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    public static final String ACTION_RESCUED = "com.g40.ww.action.RESCUED";
    public static final String ACTION_CALL = "com.g40.ww.action.CALL";
    public static final String CLOSE_INCIDENT = "com.g40.ww.action.CLOSE_INCIDENT";
    //-- KEY
    public static final String RESPONSIBLE_INCIDENT_KEY = "resIncidentId";



    public GoingService() {
        this("GoingService");
    }

    public GoingService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //handler.post(onGoingRunnable);
        // -Component
        /*registerReceiver(receiver, new IntentFilter("CloseIncident"));*/
        // /Component
        // -View
        notificationGoingRemoteViews = new RemoteViews(getPackageName(), R.layout.noti_going);
        PendingIntent rescuedPendingIntent = PendingIntent.getService(this, 0, new Intent(ACTION_RESCUED), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent callPendingIntent = PendingIntent.getService(this, 0, new Intent(ACTION_CALL), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationGoingRemoteViews.setOnClickPendingIntent(R.id.btn_set_rescued, rescuedPendingIntent);
        notificationGoingRemoteViews.setOnClickPendingIntent(R.id.btn_call, callPendingIntent);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(false)
                .setWhen(System.currentTimeMillis())
                .setCustomBigContentView(notificationGoingRemoteViews);
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notificationBuilder.build());
        // /View
        // -AlertDialog
        reqCancelAlertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.request_cancelled))
                .setMessage(getString(R.string.warn_user_request_cancel))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.call), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:".concat(ReporterProfile.getInstance().getPhoneNumber())));
                        startActivity(callIntent);
                    }
                })
                .setNegativeButton(getString(R.string.acknowledge), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        // /AlertDialog
        // -Runnable
        handler = new Handler();
        onGoingRunnable = new Runnable() {
            private final double CLOSEST_DISTANCE = 0.01; // 10 Meters

            @Override
            public void run() {

                if (AddressFactory.getInstance(null).getEstimateDistanceFromCurrentPoint(LocationFactory.getInstance(null).getLatLng(), new LatLng(AccidentFactory.getResponsibleAccident().getLatitude(), AccidentFactory.getResponsibleAccident().getLongitude())) <= CLOSEST_DISTANCE) {
                    handler.removeCallbacks(this);
                    if (Weeworh.with(GoingService.this).setRescuingCode(AccidentFactory.getResponsibleAccident().getAccidentId())) {
                        Toast.makeText(GoingService.this, getString(R.string.mainnav_incident_is_near), Toast.LENGTH_LONG).show();
                    }
                    Log.d("CLOES", "YEA");
                } else {
                    Log.d("NOT CLOESTE", "NOT");
                    handler.postDelayed(this, 10000L);
                }
            }
        };
        updateSelectedIncidentRunnable = new Runnable() {
            @Override
            public void run() {
                AccidentFactory.setResponsibleAccident(Weeworh.with(GoingService.this).getIncidentById(realm.where(AccidentBrief.class).findFirst().getAccidentId()));
                Log.d("$$$Acc", AccidentFactory.getResponsibleAccident().toString());
                if (AccidentFactory.getResponsibleAccident().getAccCode() == Accident.ACC_CODE_ERRU) {
                    handler.removeCallbacks(this);
                    reqCancelAlertDialog.show();
                } else {
                    handler.postDelayed(this, 5000L);
                }
            }
        };
        // /Runnable
        startForeground(1, notificationBuilder.build());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_RESCUED:
                    Intent mainIntent = new Intent(this, CloseIncidentActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.putExtra(RESPONSIBLE_INCIDENT_KEY, AccidentFactory.getResponsibleAccident().getAccidentId());
                    mainIntent.setAction(CLOSE_INCIDENT);
                    startActivity(mainIntent);
                    //closeIncidentAlertDialog.show();
                    break;
                case ACTION_CALL:
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:".concat(ReporterProfile.getInstance().getPhoneNumber())));
                    startActivity(callIntent);
                    break;
            }
            dismissStatusBar();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        /*unregisterReceiver(receiver);*/
        super.onDestroy();
    }


    // -Function
    private void dismissStatusBar() {
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void makeToastText(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    // /Function


    // -Getter / Setter

    // /Getter / Setter
}