package com.sitsenior.g40.weewhorescuer;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.WaitLocationAsyncTask;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.utils.DialogUtils;

public class MainActivity extends AppCompatActivity {

    public static ViewPager mainViewPager;
    public static  TabLayout tabPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationFactory.getInstance(this);
        AddressFactory.getInstance(this);
        mainViewPager = (ViewPager)findViewById(R.id.vwpgr_main);
        tabPage = (TabLayout)findViewById(R.id.tab_page);
        mainHandler = new Handler();
        new AccidentResultAsyncTask(Profile.getInsatance(), MainActivity.this).execute();
        new WaitLocationAsyncTask(MainActivity.this, mainViewPager).execute();
    }


    private Handler mainHandler;
    private Runnable mainRunnable;
    @Override
    protected void onStart() {
        super.onStart();

    }

    /* Override Other Listener */
    boolean confirmOne = false;
    @Override
    public void onBackPressed() {
        if(mainViewPager.getCurrentItem() == NavigatorFragment.NAVIGATOR_PAGE && NavigatorFragment.isOnGoing){
            Log.d(MAIN_TAG, "onBackPressed is not Allow");
            return;
        }
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

    private static final String MAIN_TAG = "MainActivity";
}
