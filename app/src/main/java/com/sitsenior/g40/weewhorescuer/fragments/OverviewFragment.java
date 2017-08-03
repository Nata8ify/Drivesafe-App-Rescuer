package com.sitsenior.g40.weewhorescuer.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class OverviewFragment extends Fragment {

    private LinearLayout emptyAccidentResultLayout;
    private ListAdapter accidentListAdapter;
    private ListView accidentListView;
    private Handler overviewHandler;
    private Runnable overviewRunnable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        accidentListView = (ListView)getView().findViewById(R.id.listvw_a_acclist);
        emptyAccidentResultLayout = (LinearLayout)getView().findViewById(R.id.linrlout_emptyacc);
        final ProgressDialog waitGpsProgressDialog = new ProgressDialog(getContext());
        waitGpsProgressDialog.setMessage("Waiting Location Service...");
        waitGpsProgressDialog.setCancelable(false);
        waitGpsProgressDialog.show();
        overviewHandler = new Handler();
        overviewRunnable = (new Runnable() {
            @Override
            public void run() {
                if(!LocationFactory.getInstance(null).isLocationActivated()) {
                    overviewHandler.postDelayed(this, 1000);
                    return;
                }
                waitGpsProgressDialog.dismiss();
                new AccidentResultAsyncTask(Profile.getInsatance(), getContext(), emptyAccidentResultLayout, accidentListView, accidentListAdapter).execute();
            }
        });
        overviewHandler.post(overviewRunnable);
    }


}
