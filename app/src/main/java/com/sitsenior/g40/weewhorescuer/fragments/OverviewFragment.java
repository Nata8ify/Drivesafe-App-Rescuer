package com.sitsenior.g40.weewhorescuer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class OverviewFragment extends Fragment {
    private LinearLayout emptyAccidentResultLayout;
    private ArrayAdapter accidentListAdapter;
    private ListView accidentListView;

    private AccidentResultAsyncTask accAsyTask;

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
        accAsyTask = new AccidentResultAsyncTask(Profile.getInsatance(), getContext(), emptyAccidentResultLayout, accidentListView, accidentListAdapter);
        accAsyTask.execute();
        overviewHandler = new Handler();
        overviewRunnable = new Runnable() {
            @Override
            public void run() {
                AccidentFactory.getInstance(Weeworh.with(getContext()).getInBoundTodayIncidents(Profile.getInsatance().getUserId()));
                Log.d("accs-1", AccidentFactory.getInstance(null).getAccidentList().toString());
                overviewHandler.postDelayed(this, 3000L);
                //accAsyTask.getAccidentListAdapter().notifyDataSetChanged();
            }
        };
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        overviewHandler.post(overviewRunnable);
    }

    public void setListener() {
        /* accidentListView's Listener */
        accidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Accident accident = AccidentFactory.getInstance(null).getAccidentList().get(position);
                AccidentFactory.getInstance(null).setSelectAccident(accident);
                ((NavigatorFragment)getFragmentManager().findFragmentByTag("android:switcher:".concat(String.valueOf(R.id.vwpgr_main)).concat(":2"))).viewAccidentDataandLocation(accident);
                MainActivity.mainViewPager.setCurrentItem(NavigatorFragment.NAVIGATOR_PAGE);
            }
        });
    }

    public static final int OVERVIEW_PAGE = 1;
}
