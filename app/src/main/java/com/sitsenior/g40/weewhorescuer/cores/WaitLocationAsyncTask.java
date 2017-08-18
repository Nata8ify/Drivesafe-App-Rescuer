package com.sitsenior.g40.weewhorescuer.cores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.adapters.MainActivityTabSectionAdapter;
import com.sitsenior.g40.weewhorescuer.fragments.ConfigurationFragment;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Accident;

/**
 * Created by nata8ify on 11/8/2560.
 */

public class WaitLocationAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private boolean waitSetting = false;
    private ProgressDialog mainProgressDialog;
    private AlertDialog reqLocationDialog;
    private ViewPager mainViewPager;
    /* Pager Set up */
    private ConfigurationFragment configurationFragment;
    private OverviewFragment overviewFragment;
    private NavigatorFragment navigatorFragment;

    public WaitLocationAsyncTask(Context context, ViewPager mainViewPager) {
        this.context = context;
        this.mainViewPager = mainViewPager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mainProgressDialog == null) {
            mainProgressDialog = new ProgressDialog(context);
            mainProgressDialog.setMessage(context.getResources().getString(R.string.main_waiting_gps));
            mainProgressDialog.setCancelable(false);
            mainProgressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            if (LocationFactory.getInstance(null).getLatLng().latitude != 0) {
                if (!LocationFactory.getInstance(null).isGPSProviderEnabled()) {
                    reqLocationDialog = new AlertDialog.Builder(context)
                            .setMessage(context.getResources().getString(R.string.warn_no_location_permission))
                            .setNegativeButton(context.getResources().getString(R.string.main_btn_nope), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((Activity) context).finish();
                                }
                            })
                            .setPositiveButton(context.getResources().getString(R.string.main_btn_tosetting), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setCancelable(false)
                            .create();
                    if (!waitSetting) {
                        reqLocationDialog.show();
                        waitSetting = true;
                    }
                }

            }
        } catch (Exception exp) {
            Log.d("exp", exp.getMessage());
            try {
                Thread.sleep(3000L);
                doInBackground();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (reqLocationDialog != null) {
            reqLocationDialog.dismiss();
        }
        mainProgressDialog.dismiss();
        setMainViewPager(this.mainViewPager);
    }

    public void setMainViewPager(ViewPager viewPager) {
        MainActivityTabSectionAdapter adapter = new MainActivityTabSectionAdapter(((AppCompatActivity) context).getSupportFragmentManager());
        this.configurationFragment = new ConfigurationFragment();
        this.overviewFragment = new OverviewFragment();
        this.navigatorFragment = new NavigatorFragment();
        adapter.addFragment(configurationFragment, "Configuration");
        adapter.addFragment(overviewFragment, "Overview");
        adapter.addFragment(navigatorFragment, "Navigator");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(OverviewFragment.OVERVIEW_PAGE);
        if(AccidentFactory.getInstance(null).getSelectAccident() != null){
            this.navigatorFragment.viewAccidentDataandLocation(AccidentFactory.getInstance(null).getSelectAccident());
        }
    }

}
