package com.sitsenior.g40.weewhorescuer.services;

import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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

    private AlertDialog reqCancelAlertDialog;

    private Handler handler;
    private Runnable onGoingRunnable;
    private Runnable updateSelectedIncidentRunnable;

    public GoingService() {
        this("GoingService");
    }

    public GoingService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //---- AlertDialog
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
        // /---- AlertDialog
        // ---- Runnable
        handler = new Handler();
        onGoingRunnable = new Runnable() {
            private final double CLOSEST_DISTANCE = 0.01; // 10 Meters

            @Override
            public void run() {
                if(AccidentFactory.getResponsibleAccident() == null){
                    handler.removeCallbacks(this);
                    return;
                }
                if (AddressFactory.getInstance(null).getEstimateDistanceFromCurrentPoint(LocationFactory.getInstance(null).getLatLng(), new LatLng(AccidentFactory.getResponsibleAccident().getLatitude(), AccidentFactory.getResponsibleAccident().getLongitude())) <= CLOSEST_DISTANCE) {
                    handler.removeCallbacks(this);
                    if (Weeworh.with(GoingService.this).setRescuingCode(AccidentFactory.getResponsibleAccident().getAccidentId())) {
                        Toast.makeText(GoingService.this, getString(R.string.mainnav_incident_is_near), Toast.LENGTH_LONG).show();
                    }
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
                if(AccidentFactory.getResponsibleAccident().getAccCode() == Accident.ACC_CODE_ERRU){
                    handler.removeCallbacks(this);
                    reqCancelAlertDialog.show();
                } else {
                    handler.postDelayed(this, 5000L);
                }
            }
        };
        // /---- AlertDialog

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        handler.post(onGoingRunnable);
        return super.onStartCommand(intent, flags, startId);
    }

}
