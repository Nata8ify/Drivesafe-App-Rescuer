package com.sitsenior.g40.weewhorescuer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sitsenior.g40.weewhorescuer.R;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class ConfigurationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    public static final int CONFIGURATION_PAGE = 0;
    static class ActivityResultCode{
        private static final int REQCODE_LOCATION_SOURCE = 0x1;
        private static final int REQCODE_INTERNET = 0x2;
    }
}
