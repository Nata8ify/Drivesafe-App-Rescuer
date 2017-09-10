package com.sitsenior.g40.weewhorescuer.cores;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by PNattawut on 11-Sep-17.
 */

public class AccidentRefreshAsyncTask extends AsyncTask {

    private ArrayAdapter<Accident> accidentArrayAdapter;
    private Context context;
    private Profile profile;

    public AccidentRefreshAsyncTask( Context context, ArrayAdapter<Accident> accidentArrayAdapter) {
        this.accidentArrayAdapter = accidentArrayAdapter;
        this.context = context;
        this.profile = Profile.getInsatance();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        AccidentFactory.getInstance(Weeworh.with(context).getInBoundTodayIncidents(profile.getUserId())); // Contains Latest Incident List
        AccidentFactory.getInstance(null).update();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

}
