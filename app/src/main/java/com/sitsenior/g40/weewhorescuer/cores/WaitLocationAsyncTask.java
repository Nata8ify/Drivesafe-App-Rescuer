package com.sitsenior.g40.weewhorescuer.cores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.adapters.MainActivityTabSectionAdapter;
import com.sitsenior.g40.weewhorescuer.fragments.ConfigurationFragment;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.models.extra.AccidentBrief;
import com.sitsenior.g40.weewhorescuer.services.FbMessagingService;

import io.realm.Realm;
import retrofit2.Retrofit;

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
        try {
            if (mainProgressDialog == null) {
                mainProgressDialog = new ProgressDialog(context);
                mainProgressDialog.setMessage(context.getResources().getString(R.string.main_waiting_gps));
                mainProgressDialog.setCancelable(false);
                mainProgressDialog.show();
            } else {
                mainProgressDialog.dismiss();
                mainProgressDialog.show();
            }
        } catch (Exception e){
            onPreExecute();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
/*        try {
            if (LocationFactory.getInstance(null).getLatLng().latitude != 0) {
                if (!LocationFactory.getInstance(null).isGPSProviderEnabled()) {
                    if (!waitSetting) {
                        reqLocationDialog.show();
                        waitSetting = true;
                    }
                }

            }
        } catch (Exception exp) {
            try {
                Thread.sleep(500L);
                doInBackground(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        while(LocationFactory.getInstance(null).getLatLng() == null){if(LocationFactory.getInstance(null).getLatLng() != null){break;}}
            if (LocationFactory.getInstance(null).getLatLng().latitude != 0) {
                if (!LocationFactory.getInstance(null).isGPSProviderEnabled()) {
                    if (!waitSetting) {
                        reqLocationDialog.show();
                        waitSetting = true;
                    }
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
        //this.configurationFragment = new ConfigurationFragment();
        this.overviewFragment = new OverviewFragment();
        this.navigatorFragment = new NavigatorFragment();
       // adapter.addFragment(configurationFragment, context.getString(R.string.configuration));
        adapter.addFragment(overviewFragment, context.getString(R.string.overview));
        adapter.addFragment(navigatorFragment, context.getString(R.string.navigator));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        MainActivity.tabPage.setupWithViewPager(viewPager);
//        viewPager.setCurrentItem(OverviewFragment.OVERVIEW_PAGE);
//        Accident selectedAccident = AccidentFactory.getSelectAccident();
//        if(selectedAccident == null){return;}
//        AccidentFactory.setSelectAccident(AccidentFactory.getInstance(null).findByAccidentId(realm.where(AccidentBrief.class).findFirst().getAccidentId()));

    }

}
