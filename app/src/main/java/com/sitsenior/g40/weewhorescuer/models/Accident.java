/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitsenior.g40.weewhorescuer.models;

import java.sql.Date;

/**
 *
 * @author PNattawut
 */
public class Accident {

    private long accidentId;
    private long userId;
    private Date date;
    private String time;
    private double latitude;
    private double longitude;
    private double forceDetect;
    private float speedDetect;
    /*-- accCode & accType is have very importance role.*/
    private byte accType;
    private char accCode;

    public static final byte ACC_TYPE_TRAFFIC = 1;
    public static final byte ACC_TYPE_FIRE = 2;
    public static final byte ACC_TYPE_BRAWL = 3;
    public static final byte ACC_TYPE_ANIMAL = 4;
    public static final byte ACC_TYPE_PATIENT = 5;
    public static final byte ACC_TYPE_OTHER = 99;
    
    /** A[Accident]: Pending for rescue */
    public static final char ACC_CODE_A = 'A';
    
    /**G[Going]: Rescuer is on the way,  */
    public static final char ACC_CODE_G = 'G';
    
    /**R[Resecue]: Rescuer is rescuing,  */
    public static final char ACC_CODE_R = 'R';
    
    /**C[Clear]: Rescue received, marking will be cleared next time.   */
    public static final char ACC_CODE_C = 'C';
    
    /**1[False on User] */
    public static final char ACC_CODE_ERRU = 'U';
    
    /**2[False on System] */
    public static final char ACC_CODE_ERRS = 'S';
    
    
    
    private static Accident accident;

    public Accident() {
    }

    public Accident(long accidentId, long userId, Date date, String time, double latitude, double longitude, double forceDetect, float speedDetect, byte accType, char accCode) {
        this.accidentId = accidentId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.forceDetect = forceDetect;
        this.speedDetect = speedDetect;
        this.accType = accType;
        this.accCode = accCode;
    }

    /** Dedicating Crash Detection. */
    public Accident(long userId, Date date, String time, float latitude, float longtitude, float forceDetect, float speedDetect) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longtitude;
        this.forceDetect = forceDetect;
        this.speedDetect = speedDetect;
    }

    /** Dedicating Crash Detection. */
    public Accident(long userId, Date date, String time, float latitude, float longtitude, float forceDetect, float speedDetect, char accCode ,  byte accType) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longtitude;
        this.forceDetect = forceDetect;
        this.speedDetect = speedDetect;
        this.accCode = accCode;
        this.accType = accType;
    }
    
    /** Dedicating Non-Crash Detection. */
        public Accident( long userId, Date date, String time, double latitude, double longitude,  byte accType, char accCode) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accType = accType;
        this.accCode = accCode;
    }
        
    public static Accident getInsatance() {
        if (accident == null) {
            accident = new Accident();
        }
        return accident;
    }


    public long getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(long accidentId) {
        this.accidentId = accidentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongtitude(float longitude) {
        this.longitude = longitude;
    }

    public double getForceDetect() {
        return forceDetect;
    }

    public void setForceDetect(float forceDetect) {
        this.forceDetect = forceDetect;
    }

    public float getSpeedDetect() {
        return speedDetect;
    }

    public void setSpeedDetect(float speedDetect) {
        this.speedDetect = speedDetect;
    }

    public char getAccCode() {
        return accCode;
    }

    public void setAccCode(char accCode) {
        this.accCode = accCode;
    }

    public byte getAccType() {
        return accType;
    }

    public void setAccType(byte accType) {
        this.accType = accType;
    }

    @Override
    public String toString() {
        return "Accident{" + "accidentId=" + accidentId + ", userId=" + userId + ", date=" + date + ", time=" + time + ", latitude=" + latitude + ", longitude=" + longitude + ", forceDetect=" + forceDetect + ", speedDetect=" + speedDetect + ", accType=" + accType + ", accCode=" + accCode + '}';
    }
}
