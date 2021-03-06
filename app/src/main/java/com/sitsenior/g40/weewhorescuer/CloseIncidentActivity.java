package com.sitsenior.g40.weewhorescuer;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.services.GoingService;
import com.sitsenior.g40.weewhorescuer.utils.SettingUtils;
import com.sitsenior.g40.weewhorescuer.utils.WeeworhRestService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CloseIncidentActivity extends AppCompatActivity {

    @BindView(R.id.btn_close_yes)
    Button btnCloseYes;
    @BindView(R.id.btn_close_no)
    Button btnCloseNo;


    private Retrofit retrofit;
    private WeeworhRestService weeworh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_incident);
        ButterKnife.bind(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(Weeworh.Url.HOST)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                .build();
        weeworh = retrofit.create(WeeworhRestService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        makeToastText(getIntent().getExtras().getLong(GoingService.RESPONSIBLE_INCIDENT_KEY)+"");
    }

    // -Function

    private void makeToastText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_close_yes, R.id.btn_close_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close_yes:
                if (!SettingUtils.isNetworkConnected(CloseIncidentActivity.this)) {
                    makeToastText(getString(R.string.warn_no_network));
                    return;
                }
                if (true) {
                    weeworh.setClosed(Profile.getInsatance().getUserId(), getIntent().getLongExtra(GoingService.RESPONSIBLE_INCIDENT_KEY, AccidentFactory.getResponsibleAccident().getAccidentId())).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            makeToastText(getString(R.string.mrservice_close_result_success));
                            Intent goingService = new Intent(CloseIncidentActivity.this, GoingService.class);
                            goingService.setAction(GoingService.CLOSE_INCIDENT);
                            startService(goingService);
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                } else {
                    makeToastText(getString(R.string.mrservice_close_result_fail));
                }
                finish();
                break;
            case R.id.btn_close_no:
                finish();
                break;
        }
    }
    // /Function

}
