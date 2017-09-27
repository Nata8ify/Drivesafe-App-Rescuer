package com.sitsenior.g40.weewhorescuer;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.services.GoingService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloseIncidentActivity extends AppCompatActivity {

    @BindView(R.id.btn_close_yes)
    Button btnCloseYes;
    @BindView(R.id.btn_close_no)
    Button btnCloseNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_incident);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeToastText(AccidentFactory.getResponsibleAccident().toString());
//        makeToastText(getIntent().getExtras().getLong(GoingService.RESPONSIBLE_INCIDENT_KEY)+"");
    }

    // -Function

    private void makeToastText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.btn_close_yes, R.id.btn_close_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close_yes:
                if (true) {
                    Weeworh.with(CloseIncidentActivity.this).setRescuedCode(AccidentFactory.getResponsibleAccident().getAccidentId());
                    makeToastText(getString(R.string.mrservice_close_result_success));
                    PendingIntent closeGoingService = PendingIntent.getService(this, 0, new Intent(GoingService.CLOSE_INCIDENT), PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent goingService = new Intent(this, GoingService.class);
                    goingService.setAction(GoingService.CLOSE_INCIDENT);
                    startService(goingService);
                } else {
                    makeToastText(getString(R.string.mrservice_close_result_fail));
                }
                break;
            case R.id.btn_close_no:
                break;
        }
    }
    // /Function

}
