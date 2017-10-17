/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitsenior.g40.weewhorescuer.models.extra;

/**
 *
 * @author PNattawut
 */
public class Hospital {
    private int hospitalId;
    private String name;
    private double latitude;
    private double longitude;
    private int score;

    public Hospital(int hospitalId, String name, double latitude, double longitude, int score) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
    }

    public Hospital(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    

    public Hospital() {
    }
    
    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Hospital{" + "hospitalId=" + hospitalId + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + ", score=" + score + '}';
    }
    
    
}
