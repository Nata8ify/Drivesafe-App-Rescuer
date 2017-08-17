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
import android.util.Log;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.adapters.MainActivityTabSectionAdapter;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.WaitLocationAsyncTask;
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
        mainHandler = new Handler();
        new AccidentResultAsyncTask(Profile.getInsatance(), MainActivity.this).execute();
        new WaitLocationAsyncTask(MainActivity.this, mainViewPager).execute();
    }


    private Handler mainHandler;
    private Runnable mainRunnable;
    private boolean waitSetting = false;
    private ProgressDialog mainProgressDialog;
    @Override
    protected void onStart() {
        super.onStart();

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


    /* Override Other Listener */
    boolean confirmOne = false;
    @Override
    public void onBackPressed() {
        if(mainViewPager.getCurrentItem() != OverviewFragment.OVERVIEW_PAGE){
            mainViewPager.setCurrentItem(OverviewFragment.OVERVIEW_PAGE);
            return;
        }
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
