package com.sitsenior.g40.weewhorescuer;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sitsenior.g40.weewhorescuer.adapters.MainActivityTabSectionAdapter;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.fragments.ConfigurationFragment;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Accident;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewPager = (ViewPager)findViewById(R.id.vwpgr_main);
        setMainViewPager(mainViewPager);
    }


    @Override
    protected void onStart() {
        super.onStart();

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
