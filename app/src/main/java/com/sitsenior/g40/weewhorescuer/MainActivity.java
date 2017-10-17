package com.sitsenior.g40.weewhorescuer;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.WaitLocationAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.models.extra.Hospital;
import com.sitsenior.g40.weewhorescuer.models.extra.HospitalDistance;
import com.sitsenior.g40.weewhorescuer.utils.DialogUtils;
import com.sitsenior.g40.weewhorescuer.utils.SettingUtils;
import com.sitsenior.g40.weewhorescuer.utils.WeeworhRestService;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static ViewPager mainViewPager;
    public static TabLayout tabPage;

    private AlertDialog noConnectionAlertDialog;
    private BroadcastReceiver closeIncidentReceiver;

    private Runnable conenctivityRunnable;
    private Handler mainActivityHandler;

    private Handler mainHandler;

    Realm realm;

    private Retrofit retrofit;
    private WeeworhRestService weeworh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        retrofit = new Retrofit.Builder()
                .baseUrl(Weeworh.Url.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weeworh = retrofit.create(WeeworhRestService.class);

        LocationFactory.getInstance(this);
        AddressFactory.getInstance(this);
        mainViewPager = (ViewPager) findViewById(R.id.vwpgr_main);
        tabPage = (TabLayout) findViewById(R.id.tab_page);
        mainHandler = new Handler();
        new WaitLocationAsyncTask(MainActivity.this, mainViewPager).execute();
// -AlertDialog
        noConnectionAlertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.warn_no_network))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        // /AlertDialog
        mainActivityHandler = new Handler();
        conenctivityRunnable = new Runnable() {
            @Override
            public void run() {
                if (!SettingUtils.isNetworkConnected(MainActivity.this)) {
                    noConnectionAlertDialog.show();
                }
                mainActivityHandler.postDelayed(this, 3000);
            }
        };
        conenctivityRunnable.run();
        // -Broadcast Receiver / Register
        // /Broadcast Receiver / Register
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /* Override Other Listener */
    boolean confirmOne = false;

    private AlertDialog registerHospitalAlertDialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_register_hospital:
                View view = LayoutInflater.from(this).inflate(R.layout.view_register_hospital, null);
                final TextView messageTextView = ((TextView) view.findViewById(R.id.txt_register_hospital_message));
                final TextView currentGeoTextView = ((TextView) view.findViewById(R.id.txtCurrentGeo));
                final EditText hospitalNameEditText = ((EditText) view.findViewById(R.id.edtxt_hospital_name));
                final Button submitButton = ((Button) view.findViewById(R.id.btn_submit_regis_hospital));
                weeworh.getNearestHospitalOne( LocationFactory.latitude, LocationFactory.longitude).enqueue(new Callback<Hospital>() {
                    @Override
                    public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                        Log.d("%$%$", response.raw().toString());
                        Hospital hospital = response.body();
                        if(hospital != null){
                            messageTextView.setText(getString(R.string.mainnav_register_hospital_alreadey_regis1).concat(" \"").concat(hospital.getName()).concat("\" ").concat(getString(R.string.mainnav_register_hospital_alreadey_regis2)));
                            hospitalNameEditText.setText(hospital.getName());
                        } else {
                            hospitalNameEditText.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Hospital> call, Throwable t) {
                        Log.d("%$%$#", call.request().url().toString());
                    }
                });
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        weeworh.registerHospital(hospitalNameEditText.getText().toString(), LocationFactory.latitude, LocationFactory.longitude).enqueue(new Callback<Hospital>() {
                            @Override
                            public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                                Hospital hospital = response.body();
                                if (hospital.getScore() == 0) {
                                    makeToastText(getString(R.string.mainnav_register_hospital_success));
                                } else {
                                    makeToastText(getString(R.string.mainnav_register_hospital_fail));
                                }

                            }
                            @Override
                            public void onFailure(Call<Hospital> call, Throwable t) {
                                makeToastText(getString(R.string.warn_unhandler_exception));
                            }
                        });
                    }
                });
                currentGeoTextView.setText(getString(R.string.mainnav_current_geo).concat(String.valueOf(LocationFactory.latitude)).concat(" , ").concat(String.valueOf(LocationFactory.longitude)));
                registerHospitalAlertDialog = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.mainnav_register_hospital))
                        .setView(view)
                        .create();
                registerHospitalAlertDialog.show();
                return true;
            case R.id.menu_screen_on:
                item.setChecked(!item.isChecked());
                if (!item.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                return false;
            case R.id.menu_logout:
                if (AccidentFactory.getResponsibleAccident() != null) {
                    Toast.makeText(this, getString(R.string.warn_responsible_logout), Toast.LENGTH_LONG).show();
                    return true;
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(Profile.class);
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mainViewPager.getCurrentItem() != OverviewFragment.OVERVIEW_PAGE) {
            mainViewPager.setCurrentItem(OverviewFragment.OVERVIEW_PAGE);
            return;
        }
        if (confirmOne) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish();
            }
        } else {
            confirmOne = !confirmOne;
            DialogUtils.getInstance(MainActivity.this).shortToast(getResources().getString(R.string.main_double_tap_exit));
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirmOne = !confirmOne;
                }
            }, 2000);
        }
    }


    private void makeToastText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
