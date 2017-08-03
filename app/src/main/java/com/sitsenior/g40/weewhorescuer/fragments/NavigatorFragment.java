package com.sitsenior.g40.weewhorescuer.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;

import org.w3c.dom.Text;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class NavigatorFragment extends Fragment {

    private MapView navMapView;
    private GoogleMap googleMap;

    private TextView txtNavigatorTitle;
    private TextView txtNavigatorDescription;

    private static final String N8IFY_GOOGLE_MAPS_API_KEY = "AIzaSyBz4yyNYqj3KNAl_cn2DpbIEne_45J9KTQ";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigator, container, false);
    }

    @Override
    public void onStart() {
        /* Text setting and How to Display */
        txtNavigatorTitle = (TextView) getView().findViewById(R.id.txt_navtitle);
        txtNavigatorDescription = (TextView) getView().findViewById(R.id.txt_navdesc);
        txtNavigatorDescription.setText(AddressFactory.getInstance(null).getBriefLocationAddress(LocationFactory.getInstance(null).getLatLng().latitude, LocationFactory.getInstance(null).getLatLng().longitude));

        /* Google Map and Map View Setting */
        navMapView = (MapView) getView().findViewById(R.id.map_navmap);
        navMapView.onCreate(getArguments());
        navMapView.onResume();
        MapsInitializer.initialize(getContext());
        navMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                NavigatorFragment.this.googleMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                NavigatorFragment.this.googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng current = new LatLng(LocationFactory.getInstance(null).getLatLng().latitude, LocationFactory.getInstance(null).getLatLng().longitude);
                googleMap.addMarker(new MarkerOptions().draggable(false).position(current).title("Current Place").snippet("Your Current Place"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
        Log.d("onStart", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        navMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navMapView.onDestroy();
    }
}
