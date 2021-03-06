package com.sitsenior.g40.weewhorescuer.cores;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.sitsenior.g40.weewhorescuer.R;

/**
 * Created by PNattawut on 02-Aug-17.
 */

public class LocationFactory {

    private static LocationFactory locationFactory;
    private static Context context;

    /* Location Things */
    private static LocationManager locationManager;
    private static LocationListener locationListener;
    private static LatLng latLng;
    private boolean locationActivated;

    //Current Lat&Lng Geo
    public static double latitude;
    public static double longitude;

    public LocationFactory() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                locationActivated = true;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
    }

    public static LocationFactory getInstance(final Context context) {
        if (locationFactory == null) {
            LocationFactory.context = context;
            locationFactory = new LocationFactory();
        }
        return locationFactory;
    }

    public void activatedLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, context.getResources().getString(R.string.warn_no_location_permission), Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        /*this.locationActivated = true;*/
    }
    public void deactivatedLocation(){
        locationManager.removeUpdates(locationListener);
        this.locationActivated = false;
    }


    /* Common getter & Setter */

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        LocationFactory.locationManager = locationManager;
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public void setLocationListener(LocationListener aLocationListener) {
        LocationFactory.locationListener = aLocationListener;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        LocationFactory.latLng = latLng;
    }

    public boolean isLocationActivated() {
        return locationActivated;
    }

    public boolean isGPSProviderEnabled(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void setLocationActivated(boolean locationActivated) {
        this.locationActivated = locationActivated;
    }
}
