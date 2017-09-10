package com.sitsenior.g40.weewhorescuer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AccidentRefreshAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.AccidentResultAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class OverviewFragment extends Fragment {
    private LinearLayout emptyAccidentResultLayout;
    private LinearLayout viewIncidentOptionLayout;
    private LinearLayout viewIncidentPanelLayout;
    private ArrayAdapter accidentListAdapter;
    private ListView accidentListView;

    private Button btnViewAwaitingRequest;
    private Button btnViewGoing;
    private Button btnViewRescuing;
    private Button btnViewClosed;

    private AccidentResultAsyncTask accResultAsyTask;
    private AccidentRefreshAsyncTask accRefreshAsyTask;

    private Handler overviewHandler;
    private Runnable overviewRunnable;

    public static byte accCodeButtonState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        accCodeButtonState = Accident.ACC_CODE_A;
        super.onCreate(savedInstanceState);
    }

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
        viewIncidentOptionLayout = (LinearLayout) getView().findViewById(R.id.linrlout_incident_view_option);
        viewIncidentPanelLayout = (LinearLayout) getView().findViewById(R.id.linrlout_incident_view_panel);
        btnViewAwaitingRequest = (Button)getView().findViewById(R.id.btn_view_a);
        btnViewGoing = (Button)getView().findViewById(R.id.btn_view_g);
        btnViewRescuing = (Button)getView().findViewById(R.id.btn_view_r);
        btnViewClosed = (Button)getView().findViewById(R.id.btn_view_c);
        accResultAsyTask = new AccidentResultAsyncTask(Profile.getInsatance(), getContext(), emptyAccidentResultLayout, viewIncidentPanelLayout, accidentListView, accidentListAdapter);
        accResultAsyTask.execute();
        accRefreshAsyTask = new AccidentRefreshAsyncTask(getContext(), accidentListAdapter);
        overviewHandler = new Handler();
        /*overviewRunnable = new Runnable() {
            @Override
            public void run() {
                //Refresh incident list
                accRefreshAsyTask.execute();
                overviewHandler.postDelayed(this, 3000L);
            }
        };*/
/*        overviewRunnable = new Runnable() {
            @Override
            public void run() {
                //Refresh incident list (none 'U', 'S' and 'C')
                AccidentFactory.getInstance(Weeworh.with(getContext()).getInBoundTodayIncidents(Profile.getInsatance().getUserId())).update(); // Contains Latest Incident List
                overviewHandler.postDelayed(this, 3000L);
                //accAsyTask.getAccidentListAdapter().notifyDataSetChanged();
            }
        };*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(">>>>>", AccidentFactory.getInstance(Weeworh.with(getContext()).getInBoundTodayIncidents(Profile.getInsatance().getUserId())).update().toString()); // Contains Latest Incident List
                overviewHandler.postDelayed(this, 3000L);
            }
        }).run();
        overviewHandler.post(overviewRunnable);
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        btnViewAwaitingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accCodeButtonState = Accident.ACC_CODE_A;
            }
        });

        btnViewGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accCodeButtonState = Accident.ACC_CODE_G;
            }
        });

        btnViewRescuing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accCodeButtonState = Accident.ACC_CODE_R;
            }
        });

        btnViewClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accCodeButtonState = Accident.ACC_CODE_C;
            }
        });
    }

    public static final int OVERVIEW_PAGE = 1;
}
