package com.sitsenior.g40.weewhorescuer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.adapters.MainActivityTabSectionAdapter;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.fragments.ConfigurationFragment;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.utils.DialogUtils;

public class MainActivity extends AppCompatActivity {

    public static ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationFactory.getInstance(this);
        AddressFactory.getInstance(this);
        mainViewPager = (ViewPager)findViewById(R.id.vwpgr_main);
        new AccidentResultAsyncTask(Profile.getInsatance(), MainActivity.this).execute();

    }


    private Handler mainHandler;
    private Runnable mainRunnable;
    private boolean waitSetting = false;
    private ProgressDialog mainProgressDialog;
    @Override
    protected void onStart() {
        super.onStart();
        if(mainProgressDialog == null){
        mainProgressDialog = new ProgressDialog(this);
        mainProgressDialog.setMessage(getResources().getString(R.string.main_waiting_gps));
        mainProgressDialog.setCancelable(false);
        mainProgressDialog.show();}
        mainHandler = new Handler();
        mainRunnable = (new Runnable() {
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
                            waitSetting = true;
                        }
                    }
                    mainHandler.postDelayed(this, 2000);
                    return;
                }
                if(reqLocationDialog!=null){reqLocationDialog.dismiss();}
                setMainViewPager(mainViewPager);
                mainProgressDialog.dismiss();
                //If Open from fcm then it shuld be redirected automatically.
                /*if(AccidentFactory.getInstance(null).getSelectAccident() != null){
                    navigatorFragment.viewAccidentDataandLocation(AccidentFactory.getInstance(null).getSelectAccident());
                }*/
                mainHandler.removeCallbacks(this);
            }
        });
        mainHandler.post(mainRunnable);
    }

    private static final int REQCODE_LOCATION_SOURCE = 0x1;
    private static final int REQCODE_INTERNET = 0x2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != RESULT_OK){
            waitSetting = false;
            return;
        }
        switch(requestCode){
            case REQCODE_LOCATION_SOURCE : break;
        }
    }

    /* Pager Set up */
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
        viewPager.setOffscreenPageLimit(3);
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


    /* Override Other Listener */
    boolean confirmOne = false;
    @Override
    public void onBackPressed() {
        if(confirmOne){
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
}
