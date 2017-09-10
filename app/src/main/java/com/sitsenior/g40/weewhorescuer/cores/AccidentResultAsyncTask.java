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
import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by PNattawut on 02-Aug-17.
 */

public class AccidentResultAsyncTask extends AsyncTask {

    private Context context;
    private boolean isIgnoreView;
    private LinearLayout emptyAccidentResultLayout;
    private ListView accidentListView;
    private ArrayAdapter accidentListAdapter;
    private Profile profile;
    private long userId;

    public AccidentResultAsyncTask(Profile profile, Context context, LinearLayout emptyAccidentResultLayout, ListView accidentListView, ArrayAdapter accidentListAdapter) {
        this.profile = profile;
        this.userId = profile.getUserId();
        this.context = context;
        this.emptyAccidentResultLayout = emptyAccidentResultLayout;
        this.accidentListView = accidentListView;
        this.accidentListAdapter = accidentListAdapter;
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
        accidentListAdapter = new AccidentListAdapter(this.context, R.layout.row_accident, AccidentFactory.getInstance(null).filterNonCloseIncident().getRescuePendingIncident());
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(isIgnoreView){return;}
        try {
           accidentListView.setAdapter(accidentListAdapter);
        } catch (NullPointerException nexcp) {
            ((Activity) (context)).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    accidentListView.setVisibility(View.GONE);
                    emptyAccidentResultLayout.setVisibility(View.VISIBLE);
                }
            });
        }
        super.onPostExecute(o);
    }

    public ListView getAccidentListView() {
        return accidentListView;
    }

    public void setAccidentListView(ListView accidentListView) {
        this.accidentListView = accidentListView;
    }

    public ArrayAdapter getAccidentListAdapter() {
        return accidentListAdapter;
    }

    public void setAccidentListAdapter(ArrayAdapter accidentListAdapter) {
        this.accidentListAdapter = accidentListAdapter;
    }
}