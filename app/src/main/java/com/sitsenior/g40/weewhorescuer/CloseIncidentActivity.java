package com.sitsenior.g40.weewhorescuer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.services.GoingService;

public class CloseIncidentActivity extends AppCompatActivity {

    private AlertDialog closeIncidentAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_incident);
        // -AlertDialog
        closeIncidentAlertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.mrservice_close_title))
                .setMessage(getString(R.string.mrservice_close_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Weeworh.with(CloseIncidentActivity.this).setRescuingCode(AccidentFactory.getResponsibleAccident().getAccidentId())) {
                            makeToastText(getString(R.string.mrservice_close_result_success));
                        } else {
                            makeToastText(getString(R.string.mrservice_close_result_fail));
                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        // /AlertDialog
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeToastText(getIntent().getExtras().getLong(GoingService.RESPONSIBLE_INCIDENT_KEY)+"");
    }

    // -Function

    private void makeToastText(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    // /Function

}
