package com.sitsenior.g40.weewhorescuer.models.extra;

import com.sitsenior.g40.weewhorescuer.models.Profile;

import io.realm.RealmModel;

/**
 * Created by PNattawut on 25-Sep-17.
 */

public class ReporterProfile {
    private static Profile profile;

    public static Profile getInstance(){
        if(profile == null){
            profile = new Profile();
        }
        return ReporterProfile.profile;
    }

    public static Profile setInstance(Profile profile){
        ReporterProfile.profile = profile;
        return ReporterProfile.profile;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        ReporterProfile.profile = profile;
    }


}
