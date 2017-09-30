package com.sitsenior.g40.weewhorescuer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.WaitLocationAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.services.GoingService;
import com.sitsenior.g40.weewhorescuer.utils.DialogUtils;
import com.sitsenior.g40.weewhorescuer.utils.SettingUtils;

public class MainActivity extends AppCompatActivity {

    public static ViewPager mainViewPager;
    public static TabLayout tabPage;

    private AlertDialog noConnectionAlertDialog;
    private BroadcastReceiver closeIncidentReceiver;

    private Runnable conenctivityRunnable;
    private Handler mainActivityHandler;

    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        closeIncidentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                makeToastText("onReceive");
                if (intent.getAction() != null) {
                    makeToastText(intent.getAction());
                }
            }
        };
        registerReceiver(closeIncidentReceiver, new IntentFilter(GoingService.CLOSE_INCIDENT));
        // /Broadcast Receiver / Register
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(closeIncidentReceiver);
    }

    /* Override Other Listener */
    boolean confirmOne = false;

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
