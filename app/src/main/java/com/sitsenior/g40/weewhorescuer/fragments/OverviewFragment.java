package com.sitsenior.g40.weewhorescuer.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class OverviewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        new AccidentResultAsyncTask().execute();
    }

    class AccidentResultAsyncTask extends AsyncTask {

        private List<Accident> accList;
        private Context context;
        @Override
        protected void onPreExecute() {
            this.context = getView().getContext();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            accList = Weeworh.with(this.context).getInBoundTodayIncidents(1l);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Toast.makeText(context, accList.toString(), Toast.LENGTH_LONG).show();
            super.onPostExecute(o);
        }
    }
}
