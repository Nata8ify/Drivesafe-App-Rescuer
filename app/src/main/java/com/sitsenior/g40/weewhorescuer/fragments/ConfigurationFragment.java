package com.sitsenior.g40.weewhorescuer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private Realm realm;
    private Button logoutButton;
    private AlertDialog logoutAlertDialog;
    private Spinner languageSpinner;
    private CheckBox chkboxKeepScreenOn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        logoutAlertDialog = new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.warn_youre_logout))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Profile inAppProfile = realm.where(Profile.class).findFirst();
                                if (inAppProfile != null) {
                                    inAppProfile.deleteFromRealm();
                                }
                            }
                        });
                        getActivity().finish();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        this.context = getContext();
        return inflater.inflate(R.layout.fragment_config, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        logoutButton = (Button) getView().findViewById(R.id.btn_logout);
        languageSpinner = (Spinner) getView().findViewById(R.id.spnr_langs);
        chkboxKeepScreenOn = (CheckBox) getView().findViewById(R.id.chkbox_keep_screen_on);
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
        setListener();
    }

    private void setListener() {
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAlertDialog.show();
            }
        });

        chkboxKeepScreenOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
    }

    public static final int CONFIGURATION_PAGE = 0;

}
