package com.sitsenior.g40.weewhorescuer.models.extra;

import com.sitsenior.g40.weewhorescuer.models.Accident;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PNattawut on 07-Sep-17.
 */

public class AccidentBrief extends RealmObject {
    @PrimaryKey
    private long accidentId;

    private long responsibleRescr;

    private long userId;
    private double latitude;
    private double longitude;

    public AccidentBrief(Accident accident) {
        this.accidentId = accident.getAccidentId();
        this.userId = accident.getUserId();
        this.latitude = accident.getLatitude();
        this.longitude = accident.getLongitude();
        this.responsibleRescr = accident.getResponsibleRescr();
    }

    public AccidentBrief() {
    }

    public long getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(long accidentId) {
        this.accidentId = accidentId;
    }

    public long getResponsibleRescr() {
        return responsibleRescr;
    }

    public void setResponsibleRescr(long responsibleRescr) {
        this.responsibleRescr = responsibleRescr;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
}
