package com.sitsenior.g40.weewhorescuer.cores;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.adapters.AccidentListAdapter;
import com.sitsenior.g40.weewhorescuer.fragments.OverviewFragment;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;

import java.util.List;

/**
 * Created by PNattawut on 02-Aug-17.
 */

public class AccidentResultAsyncTask extends AsyncTask {

    private Context context;
    private boolean isIgnoreView;
    private LinearLayout emptyAccidentResultLayout;
    private LinearLayout viewIncidentPanelLayout;
    private ListView accidentListView;
    private Profile profile;
    private long userId;


    public AccidentResultAsyncTask(Profile profile, Context context, LinearLayout emptyAccidentResultLayout, LinearLayout viewIncidentPanelLayout, ListView accidentListView) {
        this.profile = profile;
        this.userId = profile.getUserId();
        this.context = context;
        this.emptyAccidentResultLayout = emptyAccidentResultLayout;
        this.viewIncidentPanelLayout = viewIncidentPanelLayout;
        this.accidentListView = accidentListView;
    }

    public AccidentResultAsyncTask(Profile profile, Context context){
        this.isIgnoreView = true;
        this.profile = profile;
        this.userId = profile.getUserId();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        AccidentFactory.getInstance(Weeworh.with(this.context).getInBoundTodayIncidents(userId));
        if(isIgnoreView){return null;}
        OverviewFragment.accidentListAdapter = new AccidentListAdapter(this.context, R.layout.row_accident, AccidentFactory.getInstance(null).filterNonCloseIncident().getRescuePendingIncident());
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(isIgnoreView){return;}
        try {
           accidentListView.setAdapter(OverviewFragment.accidentListAdapter);
        } catch (NullPointerException nexcp) {
            ((Activity) (context)).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewIncidentPanelLayout.setVisibility(View.GONE);
                    emptyAccidentResultLayout.setVisibility(View.VISIBLE);
                }
            });
        }
        OverviewFragment.emptyAccidentResultLayout = this.emptyAccidentResultLayout;
        OverviewFragment.viewIncidentPanelLayout = this.viewIncidentPanelLayout;
        OverviewFragment.accidentListView = this.accidentListView;
        super.onPostExecute(o);
    }

}