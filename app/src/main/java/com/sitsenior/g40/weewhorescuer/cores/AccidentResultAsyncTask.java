package com.sitsenior.g40.weewhorescuer.cores;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.adapters.AccidentListAdapter;
import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by PNattawut on 02-Aug-17.
 */

public class AccidentResultAsyncTask extends AsyncTask {

    private Context context;
    private LinearLayout emptyAccidentResultLayout;
    private ListView accidentListView;
    private ListAdapter accidentListAdapter;
    private Profile profile;

    public AccidentResultAsyncTask(Profile profile, Context context, LinearLayout emptyAccidentResultLayout, ListView accidentListView, ListAdapter accidentListAdapter) {
        this.profile = profile;
        this.context = context;
        this.emptyAccidentResultLayout = emptyAccidentResultLayout;
        this.accidentListView = accidentListView;
        this.accidentListAdapter = accidentListAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("profile ", profile.toString());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        AccidentFactory.getInstance(Weeworh.with(this.context).getInBoundTodayIncidents(1));
            accidentListAdapter = new AccidentListAdapter(this.context, R.layout.row_accident, AccidentFactory.getInstance(null).getAccidentList());
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try {
            accidentListView.setAdapter(accidentListAdapter);
            accidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(context, AccidentFactory.getInstance(null).getAccidentList().get(position).toString(), Toast.LENGTH_LONG).show();
                }
            });
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
}