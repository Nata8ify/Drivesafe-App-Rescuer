package com.sitsenior.g40.weewhorescuer.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
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
import com.sitsenior.g40.weewhorescuer.models.Accident;

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
    private static final String N8IFY_GOOGLE_MAPS_DIRECTION_KEY = "AIzaSyAUyVikwoN9vvsV8vHvqj98g-Nxq0WtDAg";

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
        txtNavigatorTitle.setText(getContext().getResources().getString(R.string.mainnav_curposition));

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
                CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
        Log.d("nav onStart", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("nav onResume", "onResume");
        super.onResume();
        navMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("nav on pause", "D");
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


    public void viewAccidentDataandLocation(Accident accident){
        googleMap.clear();
        LatLng current = new LatLng(LocationFactory.getInstance(null).getLatLng().latitude, LocationFactory.getInstance(null).getLatLng().longitude);
        LatLng des = new LatLng(accident.getLatitude(), accident.getLongitude());
        Log.d("selected acc" , current.toString()+" : "+des.toString());
        googleMap.addMarker(new MarkerOptions().draggable(false).position(current).title(getString(R.string.mainnav_marker_curposition_title)));
        googleMap.addMarker(new MarkerOptions().draggable(false).position(des).title(getString(R.string.mainnav_marker_desposition_title)));
        GoogleDirection.withServerKey(N8IFY_GOOGLE_MAPS_DIRECTION_KEY)
                .from(LocationFactory.getInstance(null).getLatLng())
                .to(new LatLng(accident.getLatitude(), accident.getLongitude()))
                .transportMode(TransportMode.TRANSIT)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()){
                            Log.d("rawBody", rawBody);
                            googleMap.addPolyline(DirectionConverter.createPolyline(getContext(), direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint(), 5, Color.RED));
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }

    public static final int NAVIGATOR_PAGE = 2;
}
