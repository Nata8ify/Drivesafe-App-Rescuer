/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitsenior.g40.weewhorescuer.models.extra;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import io.realm.RealmObject;

/**
 *
 * @author PNattawut
 */
public class OperatingLocation extends RealmObject{
    private double latitude;
    private double longitude;
    private int neutralBound;
    private int mainBound;
    private int organizationId;

    public static OperatingLocation operatingLocation;

    public static OperatingLocation getInstance(){
        if(operatingLocation == null){
            operatingLocation = new OperatingLocation();
        }
        return operatingLocation;
    }

    public OperatingLocation() {
    }

    public OperatingLocation(double latitude, double longitude, int neutralBound, int mainBound, int organizationId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.neutralBound = neutralBound;
        this.mainBound = mainBound;
        this.organizationId = organizationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNeutralBound() {
        return neutralBound;
    }

    public void setNeutralBound(int neutralBound) {
        this.neutralBound = neutralBound;
    }

    public int getMainBound() {
        return mainBound;
    }

    public void setMainBound(int mainBound) {
        this.mainBound = mainBound;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }



    public String toJSON(){
        return new Gson().toJson(this);
    }
}
