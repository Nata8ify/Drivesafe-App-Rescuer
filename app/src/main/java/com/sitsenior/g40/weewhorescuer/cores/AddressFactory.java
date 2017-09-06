package com.sitsenior.g40.weewhorescuer.cores;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class AddressFactory {
    private static Geocoder geocoder;
    private static Context context;
    private static AddressFactory addressFactory;
    /* Goddamn Mother f;cker Hardest Algorithm involed on Mathematic's Formular  */
    private final double DR = Math.PI / 180; //DEG_TO_RAD
    private final int RADIAN_OF_EARTH_IN_KM = 6371;
    private final DecimalFormat kmDecimalFormat = new DecimalFormat("###.00");

    public static AddressFactory getInstance(Context context){
        if(addressFactory == null) {
            AddressFactory.context = context;
            geocoder = new Geocoder(context, Locale.getDefault());
            addressFactory = new AddressFactory();
        }
        return addressFactory;
    }

    public String getBriefLocationAddress(LatLng latLng){
        try {
            List<Address> addrs = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            return addrs.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "na";
    }

    public double getEstimateDistanceFromCurrentPoint(LatLng current, LatLng des){
        double dLat = DR * (current.latitude - des.latitude);
        double dLng = DR * (current.longitude - des.longitude);
        double a = (Math.sin(dLat / 2) * Math.sin(dLat / 2))
                + (Math.cos(des.latitude * DR) * Math.cos(current.latitude * DR))
                * (Math.sin(dLng / 2) * Math.sin(dLng / 2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = c * RADIAN_OF_EARTH_IN_KM;
        Log.d("Distance is ", distance+" Km(s)");
        return Double.valueOf(kmDecimalFormat.format(distance));
    }
}
