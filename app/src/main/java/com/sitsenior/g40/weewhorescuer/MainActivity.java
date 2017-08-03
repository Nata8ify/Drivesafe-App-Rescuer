package com.sitsenior.g40.weewhorescuer;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sitsenior.g40.weewhorescuer.adapters.MainActivityTabSectionAdapter;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.fragments.ConfigurationFragment;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewPager = (ViewPager)findViewById(R.id.vwpgr_main);
        setMainViewPager(mainViewPager);
        LocationFactory.getInstance(this);
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
