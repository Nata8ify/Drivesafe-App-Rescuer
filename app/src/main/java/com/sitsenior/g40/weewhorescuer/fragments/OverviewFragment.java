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
import com.sitsenior.g40.weewhorescuer.adapters.AccidentListAdapter;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class OverviewFragment extends Fragment {

    private ListAdapter accidentListAdapter;
    private ListView accidentListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        accidentListView = (ListView)getView().findViewById(R.id.listvw_a_acclist);
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
            accList = Weeworh.with(this.context).getInBoundTodayIncidents(1);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            accidentListAdapter = new AccidentListAdapter(getView().getContext(), R.layout.row_accident, accList);
            accidentListView.setAdapter(accidentListAdapter);
            super.onPostExecute(o);
        }
    }
}
