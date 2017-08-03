package com.sitsenior.g40.weewhorescuer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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

    private ViewPager mainViewPager;

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

    @Override
    protected void onStart() {
        super.onStart();
        final ProgressDialog waitGpsProgressDialog = new ProgressDialog(this);
        waitGpsProgressDialog.setMessage("Waiting Location Service...");
        waitGpsProgressDialog.setCancelable(false);
        waitGpsProgressDialog.show();
        mainHandler = new Handler();
        mainRunnable = (new Runnable() {
            @Override
            public void run() {
                if(!LocationFactory.getInstance(null).isLocationActivated()) {
                    mainHandler.postDelayed(this, 1000);
                    return;
                }
                waitGpsProgressDialog.dismiss();
                setMainViewPager(mainViewPager);
            }
        });
        mainHandler.post(mainRunnable);
    }

    public void setMainViewPager(ViewPager viewPager){
        MainActivityTabSectionAdapter adapter = new MainActivityTabSectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConfigurationFragment(), "Configuration");
        adapter.addFragment(new OverviewFragment(), "Overview");
        adapter.addFragment(new NavigatorFragment(), "Navigator");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

}
