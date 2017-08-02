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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.adapters.AccidentListAdapter;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
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

        private Context context;
        @Override
        protected void onPreExecute() {
            this.context = getView().getContext();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            AccidentFactory.getInstance(Weeworh.with(this.context).getInBoundTodayIncidents(1));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            accidentListAdapter = new AccidentListAdapter(getView().getContext(), R.layout.row_accident, AccidentFactory.getInstance(null).getAccidentList());
            accidentListView.setAdapter(accidentListAdapter);
            accidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(context, AccidentFactory.getInstance(null).getAccidentList().get(position).toString(), Toast.LENGTH_LONG).show();
                }
            });
            super.onPostExecute(o);
        }
    }
}
