/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitsenior.g40.weewhorescuer.models.extra;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

/**
 *
 * @author PNattawut
 */
public class OperatingLocation {
    private LatLng latLng;
    private int neutralBound;
    private int mainBound;
    private int organizationId;

    public OperatingLocation(LatLng latLng, int neutralBound) {
        this.latLng = latLng;
        this.neutralBound = neutralBound;
    }

    public OperatingLocation(LatLng latLng, int neutralBound, int mainBound) {
        this.latLng = latLng;
        this.neutralBound = neutralBound;
        this.mainBound = mainBound;
    }

    public OperatingLocation(LatLng latLng, int neutralBound, int mainBound, int organizationId) {
        this.latLng = latLng;
        this.neutralBound = neutralBound;
        this.mainBound = mainBound;
        this.organizationId = organizationId;
    }



    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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
