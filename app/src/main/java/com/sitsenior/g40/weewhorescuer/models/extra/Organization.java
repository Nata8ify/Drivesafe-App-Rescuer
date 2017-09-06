package com.sitsenior.g40.weewhorescuer.models.extra;

import com.google.gson.Gson;

/**
 * Created by PNattawut on 07-Aug-17.
 */

public class Organization {
    private int organizationId;
    private String organizationName;
    private String organizationDescription;

    public Organization(int organizationId, String organizationName, String organizationDescription) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.organizationDescription = organizationDescription;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationDescription() {
        return organizationDescription;
    }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }



    @Override
    public String toString() {
        return "Organization{" + "organizationId=" + organizationId + ", organizationName=" + organizationName + ", organizationDescription=" + organizationDescription + '}';
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}
