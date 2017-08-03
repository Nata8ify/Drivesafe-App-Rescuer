package com.sitsenior.g40.weewhorescuer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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
            @Override
            public void run() {
                if(!LocationFactory.getInstance(null).isLocationActivated()) {
                    mainHandler.postDelayed(this, 1000);
                    return;
                }
                setMainViewPager(mainViewPager);

                mainProgressDialog.dismiss();
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
