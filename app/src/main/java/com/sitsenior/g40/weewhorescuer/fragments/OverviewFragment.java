package com.sitsenior.g40.weewhorescuer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.models.Accident;
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
        accidentListView = (ListView) getView().findViewById(R.id.listvw_a_acclist);
        emptyAccidentResultLayout = (LinearLayout) getView().findViewById(R.id.linrlout_emptyacc);

        new AccidentResultAsyncTask(Profile.getInsatance(), getContext(), emptyAccidentResultLayout, accidentListView, accidentListAdapter).execute();
        setListener();
    }

    public void setListener() {
        /* accidentListView's Listener */
        accidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Accident accident = AccidentFactory.getInstance(null).getAccidentList().get(position);
                AccidentFactory.getInstance(null).setSelectAccident(accident);
                MainActivity.mainViewPager.setCurrentItem(NavigatorFragment.NAVIGATOR_PAGE);
                ((MainActivity)getActivity()).getNavigatorFragment().viewAccidentDataandLocation(accident);
            }
        });
    }

    public static final int OVERVIEW_PAGE = 1;
}
