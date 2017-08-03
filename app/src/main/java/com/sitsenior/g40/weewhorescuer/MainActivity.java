package com.sitsenior.g40.weewhorescuer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.adapters.MainActivityTabSectionAdapter;
import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.fragments.ConfigurationFragment;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Profile;

public class MainActivity extends AppCompatActivity {

    public static ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationFactory.getInstance(this);
        AddressFactory.getInstance(this);
        mainViewPager = (ViewPager)findViewById(R.id.vwpgr_main);

    }


    private Handler mainHandler;
    private Runnable mainRunnable;

    private ProgressDialog mainProgressDialog;
    @Override
    protected void onStart() {
        super.onStart();
        mainProgressDialog = new ProgressDialog(this);
        mainProgressDialog.setMessage(getResources().getString(R.string.main_waiting_gps));
        mainProgressDialog.setCancelable(false);
        mainProgressDialog.show();
        mainHandler = new Handler();
        mainRunnable = (new Runnable() {
            private boolean waitSetting = false;
            private AlertDialog reqLocationDialog;
            @Override
            public void run() {
                if(!LocationFactory.getInstance(null).isLocationActivated()) {
                    if(!LocationFactory.getInstance(null).isGPSProviderEnabled() && !waitSetting) {
                        reqLocationDialog = new AlertDialog.Builder(MainActivity.this)
                                .setMessage(MainActivity.this.getResources().getString(R.string.warn_no_location_permission))
                                .setNegativeButton(MainActivity.this.getResources().getString(R.string.main_btn_nope), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.this.finish();
                                    }
                                })
                                .setPositiveButton(MainActivity.this.getResources().getString(R.string.main_btn_tosetting), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })
                                .setCancelable(false)
                                .create();
                        if (!waitSetting) {
                            reqLocationDialog.show();
                        }
                        waitSetting = true;
                    }
                    mainHandler.postDelayed(this, 2000);
                    return;
                }
                setMainViewPager(mainViewPager);
                mainProgressDialog.dismiss();
                mainHandler.removeCallbacks(this);
            }
        });
        mainHandler.post(mainRunnable);
    }

    private ConfigurationFragment configurationFragment;
    private OverviewFragment overviewFragment;
    private NavigatorFragment navigatorFragment;
    public void setMainViewPager(ViewPager viewPager){
        MainActivityTabSectionAdapter adapter = new MainActivityTabSectionAdapter(getSupportFragmentManager());
        this.configurationFragment = new ConfigurationFragment();
        this.overviewFragment = new OverviewFragment();
        this.navigatorFragment = new NavigatorFragment();
        adapter.addFragment(configurationFragment, "Configuration");
        adapter.addFragment(overviewFragment, "Overview");
        adapter.addFragment(navigatorFragment, "Navigator");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(OverviewFragment.OVERVIEW_PAGE);
    }

    public ConfigurationFragment getConfigurationFragment() {
        return configurationFragment;
    }

    public OverviewFragment getOverviewFragment() {
        return overviewFragment;
    }

    public NavigatorFragment getNavigatorFragment() {
        return navigatorFragment;
    }
}
