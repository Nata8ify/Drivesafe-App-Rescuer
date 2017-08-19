package com.sitsenior.g40.weewhorescuer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sitsenior.g40.weewhorescuer.LoginActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.models.Profile;

import io.realm.Realm;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class ConfigurationFragment extends Fragment {

    private Context context;

    private Button logoutButton;
    private Spinner languageSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();
        return inflater.inflate(R.layout.fragment_config, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        logoutButton = (Button) getView().findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Profile.class).findFirst().deleteFromRealm();
                    }
                });
                ((Activity)context).finish();
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        languageSpinner = (Spinner) getView().findViewById(R.id.spnr_langs);
        languageSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.maincon_langs)));
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });
    }

    public static final int CONFIGURATION_PAGE = 0;

}
