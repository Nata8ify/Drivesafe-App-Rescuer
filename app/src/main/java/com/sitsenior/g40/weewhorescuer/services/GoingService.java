package com.sitsenior.g40.weewhorescuer.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.GsonBuilder;
import com.sitsenior.g40.weewhorescuer.CloseIncidentActivity;
import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.ReporterFalseActivity;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.extra.AccidentBrief;
import com.sitsenior.g40.weewhorescuer.models.extra.ReporterProfile;
import com.sitsenior.g40.weewhorescuer.utils.WeeworhRestService;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PNattawut on 25-Sep-17.
 */

public class GoingService extends IntentService {

    Context context;
    RemoteViews notificationGoingRemoteViews;

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

    private NotificationCompat.Builder notificationBuilder;

    private double estimatedDistance;

    public static final String ACTION_RESCUED = "com.g40.ww.action.RESCUED";
    public static final String ACTION_CALL = "com.g40.ww.action.CALL";
    public static final String ACTION_TO_MAIN = "com.g40.ww.action.TO_MAIN";
    public static final String CLOSE_INCIDENT = "com.g40.ww.action.CLOSE_INCIDENT";
    //-- KEY
    public static final String RESPONSIBLE_INCIDENT_KEY = "resIncidentId";

    Retrofit retrofit;
    WeeworhRestService weeworhRestService;
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
        retrofit = new Retrofit.Builder()
                .baseUrl(Weeworh.Url.HOST)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                .build();
        weeworhRestService = retrofit.create(WeeworhRestService.class);

                /*registerReceiver(receiver, new IntentFilter("CloseIncident"));*/
        // /Component
        // -View
        notificationGoingRemoteViews = new RemoteViews(getPackageName(), R.layout.noti_going);
        PendingIntent rescuedPendingIntent = PendingIntent.getService(this, 0, new Intent(ACTION_RESCUED), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent callPendingIntent = PendingIntent.getService(this, 0, new Intent(ACTION_CALL), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent toMainActivityPendingIntent = PendingIntent.getService(this, 0, new Intent(ACTION_TO_MAIN), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationGoingRemoteViews.setOnClickPendingIntent(R.id.btn_set_rescued, rescuedPendingIntent);
        notificationGoingRemoteViews.setOnClickPendingIntent(R.id.btn_call, callPendingIntent);
        //notificationGoingRemoteViews.setOnClickPendingIntent(R.id.linrout_root_gnoti, toMainActivityPendingIntent);
        //TODO Excp after no network
        notificationGoingRemoteViews.setTextViewText(R.id.txt_breif_location, AddressFactory.getInstance(this).getBriefLocationAddress(new LatLng(AccidentFactory.getResponsibleAccident().getLatitude(), AccidentFactory.getResponsibleAccident().getLongitude())));
        notificationGoingRemoteViews.setTextViewText(R.id.txt_incident_status, getString(R.string.status).concat(" : ").concat(getStatusString(Accident.ACC_CODE_G)));
        notificationGoingRemoteViews.setTextViewText(R.id.txt_incident_type, getString(R.string.mrservice_acc_type).concat(" : ").concat(getIncidentTypeString(AccidentFactory.getResponsibleAccident().getAccType())));
        notificationGoingRemoteViews.setImageViewBitmap(R.id.img_acctype, BitmapFactory.decodeResource(getResources(), getAccidentTypeImage(AccidentFactory.getResponsibleAccident().getAccType())));
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getIncidentTypeString(AccidentFactory.getResponsibleAccident().getAccType()))
                .setContentText(AddressFactory.getInstance(this).getBriefLocationAddress(new LatLng(AccidentFactory.getResponsibleAccident().getLatitude(), AccidentFactory.getResponsibleAccident().getLongitude())))
                .setOngoing(false)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(toMainActivityPendingIntent)
                .setCustomBigContentView(notificationGoingRemoteViews);
        // /View
        // -Runnable
        handler = new Handler();
        onGoingRunnable = new Runnable() {
            private final double CLOSEST_DISTANCE = 0.01; // 10 Meters

            @Override
            public void run() {
                estimatedDistance = AddressFactory.getInstance(null).getEstimateDistanceFromCurrentPoint(LocationFactory.getInstance(null).getLatLng(), new LatLng(AccidentFactory.getResponsibleAccident().getLatitude(), AccidentFactory.getResponsibleAccident().getLongitude()));
                if (estimatedDistance <= CLOSEST_DISTANCE) {
                    handler.removeCallbacks(this);
                        if (Weeworh.with(GoingService.this).setRescuingCode(AccidentFactory.getResponsibleAccident().getAccidentId())) {
                            Toast.makeText(GoingService.this, getString(R.string.mainnav_incident_is_near), Toast.LENGTH_LONG).show();
                            //notificationGoingRemoteViews.setTextViewText(R.id.txt_incident_status, getString(R.string.status).concat(" : ").concat(getStatusString(Accident.ACC_CODE_R)));
                        }
                } else {
                    handler.postDelayed(this, 10000L);
                }
            }
        };
        updateSelectedIncidentRunnable = new Runnable() {
            @Override
            public void run() {
                weeworhRestService.getIncidetById(AccidentFactory.getResponsibleAccident().getAccidentId()).enqueue(new Callback<Accident>() {
                    @Override
                    public void onResponse(Call<Accident> call, Response<Accident> response) {
                        AccidentFactory.setResponsibleAccident(response.body());

                        if (AccidentFactory.getResponsibleAccident().getAccCode() == Accident.ACC_CODE_ERRU) {
                            handler.removeCallbacks(updateSelectedIncidentRunnable);
                            handler.removeCallbacks(onGoingRunnable);
                            Intent reporterCancelIntent = new Intent(GoingService.this, ReporterFalseActivity.class);
                            reporterCancelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(reporterCancelIntent);
                            stop();
                        } else {
                            handler.postDelayed(updateSelectedIncidentRunnable, 1000L);
                        }
                    }

                    @Override
                    public void onFailure(Call<Accident> call, Throwable t) {
                        makeToastText(t.toString());
                    }
                });

            }
        };
        // /Runnable

        startForeground(1, notificationBuilder.build());
        handler.post(onGoingRunnable);
        handler.post(updateSelectedIncidentRunnable);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        context = getApplicationContext();
        Intent restartIntent = new Intent(this, MainActivity.class);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_RESCUED:
                    Intent closeIntent = new Intent(this, CloseIncidentActivity.class);
                    closeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    closeIntent.putExtra(RESPONSIBLE_INCIDENT_KEY, AccidentFactory.getResponsibleAccident().getAccidentId());
                    closeIntent.setAction(CLOSE_INCIDENT);
                    startActivity(closeIntent);
                    break;
                case ACTION_CALL:
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:".concat(ReporterProfile.getInstance().getPhoneNumber())));
                    startActivity(callIntent);
                    break;
                case ACTION_TO_MAIN:
                    startActivity(restartIntent);
                    break;
                case CLOSE_INCIDENT:
                    stop();
                    startActivity(restartIntent);
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
    private void stop(){
        stopForeground(true);
        stopSelf();
        handler.removeCallbacks(onGoingRunnable);
        handler.removeCallbacks(updateSelectedIncidentRunnable);
        AccidentFactory.setResponsibleAccident(null);
        onDestroy();
    }

    private void dismissStatusBar() {
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private String getStatusString(char accCode) {
        switch (accCode) {
            case Accident.ACC_CODE_A:
                return getString(R.string.mrservice_acc_status_a);
            case Accident.ACC_CODE_G:
                return getString(R.string.mrservice_acc_status_g);
            case Accident.ACC_CODE_R:
                return getString(R.string.mrservice_acc_status_r);
            case Accident.ACC_CODE_C:
                return getString(R.string.mrservice_acc_status_c);
            case Accident.ACC_CODE_ERRU:
                return getString(R.string.mrservice_acc_status_erru);
            case Accident.ACC_CODE_ERRS:
                return getString(R.string.mrservice_acc_status_errs);
            default:
        }
        return getString(R.string.mrservice_acc_status_und);
    }

    private String getIncidentTypeString(byte accType) {
        switch (accType) {
            case Accident.ACC_TYPE_TRAFFIC:
                return getString(R.string.mrservice_acc_type_crash);
            case Accident.ACC_TYPE_FIRE:
                return getString(R.string.mrservice_acc_type_fire);
            case Accident.ACC_TYPE_BRAWL:
                return getString(R.string.mrservice_acc_type_brawl);
            case Accident.ACC_TYPE_PATIENT:
                return getString(R.string.mrservice_acc_type_patient);
            case Accident.ACC_TYPE_ANIMAL:
                return getString(R.string.mrservice_acc_type_animal);
            case Accident.ACC_TYPE_OTHER:
                return getString(R.string.mrservice_acc_type_other);
            default:
        }
        return getString(R.string.mrservice_acc_status_und);
    }

    public int getAccidentTypeImage(byte accType) {
        String imgName = null;
        switch (accType) {
            case Accident.ACC_TYPE_TRAFFIC:
                imgName = "acctype_crash";
                break;
            case Accident.ACC_TYPE_FIRE:
                imgName = "acctype_fire";
                break;
            case Accident.ACC_TYPE_ANIMAL:
                imgName = "acctype_animal";
                break;
            case Accident.ACC_TYPE_PATIENT:
                imgName = "acctype_patient";
                break;
            case Accident.ACC_TYPE_BRAWL:
                imgName = "acctype_brawl";
                break;
            case Accident.ACC_TYPE_OTHER:
                imgName = "acctype_other";
                break;
            default:
                imgName = "acctype_other"; //TODO
        }
        return getResources().getIdentifier(imgName, "drawable", getPackageName());
    }

    private void makeToastText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    // /Function


    // -Getter / Setter

    // /Getter / Setter
}
