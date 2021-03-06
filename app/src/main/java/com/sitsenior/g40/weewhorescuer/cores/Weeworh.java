package com.sitsenior.g40.weewhorescuer.cores;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sitsenior.g40.weewhorescuer.fragments.NavigatorFragment;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.models.extra.HospitalDistance;
import com.sitsenior.g40.weewhorescuer.models.extra.OperatingLocation;
import com.sitsenior.g40.weewhorescuer.utils.SettingUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class Weeworh {

    private static Weeworh weeworh;
    private static Context context;
    public static Weeworh with(Context context){
        if(weeworh == null){
            weeworh = new Weeworh(context);
        }

        return weeworh;
    }

    public Weeworh(Context context) {
        Weeworh.context = context;
    }
    public Profile login(String username, String password){
        Profile rescuerProfile = null;
        try {
            rescuerProfile = Ion.with(context)
                    .load(Url.RESCUER_LOGIN)
                    .setTimeout(5000)
                    .setBodyParameter(Param.usrn, username)
                    .setBodyParameter(Param.pswd, password)
                    .as(Profile.class)
                    .get();
            Profile.getInsatance().set(rescuerProfile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rescuerProfile;
    }

    public Profile getReportUserInformation(long userId){
        if(!SettingUtils.isNetworkConnected(context)){return null;}
        Profile reportUserProfile = null;
        try {
            reportUserProfile = Ion.with(context)
                    .load(Url.GET_REPORT_USER_INFO)
                    .setBodyParameter(Param.userId, String.valueOf(userId))
                    .as(Profile.class)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reportUserProfile;
    }

    public int getOperatingLocationIdByUserId(long userId){
        int organizationId = 0;
        if(!SettingUtils.isNetworkConnected(context)){return organizationId;}
        try {
            organizationId = Ion.with(context)
                    .load(Url.GET_ORGANIZE_ID)
                    .setBodyParameter(Param.userId, String.valueOf(userId))
                    .as(Integer.class)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return organizationId;
    }

    public List<Accident> getInBoundTodayIncidents(long userId){
        if(!SettingUtils.isNetworkConnected(context)){return null;}
        List<Accident> accidents = null;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Ion ion = Ion.getDefault(context);
        ion.configure().setGson(gson);
        try {
            accidents = (List<Accident>)ion.with(context)
                    .load(Url.GET_TODAY_INBOUND_ACCIDENTS)
                    .setTimeout(1000)
                    .setBodyParameter(Param.userId, String.valueOf(userId))
                    .as(TypeToken.get(new TypeToken<List<Accident>>(){}.getType()))
                    .get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return accidents;
    }

    public Accident getIncidentById(long accidentId){
        if(!SettingUtils.isNetworkConnected(context)){return null;}
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Ion ion = Ion.getDefault(context);
        ion.configure().setGson(gson);
        try {
            return ion.with(context)
                    .load(Url.GET_ACCIDENTS_BY_ID)
                    .setTimeout(10000)
                    .setBodyParameter(Param.accidentId, String.valueOf(accidentId))
                    .as(Accident.class)
                    .get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Set Incident status / Accident status */
    public boolean setGoingCode(long accidentId){
        if(!SettingUtils.isNetworkConnected(context)){return false;}
        try {
            String response = Ion.with(context)
                    .load(Url.SET_GOING_CODE)
                    .setBodyParameter(Param.accidentId, String.valueOf(accidentId))
                    .setBodyParameter(Param.responsibleRescr, String.valueOf(Profile.getInsatance().getUserId()))
                    .asString()
                    .get();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setRescuingCode(long accidentId){ //Code R (Rescuing)
        if(!SettingUtils.isNetworkConnected(context)){return false;}
        try {
            String response = Ion.with(context)
                    .load(Url.SET_RESCUING_CODE)
                    .setBodyParameter(Param.accidentId, String.valueOf(accidentId))
                    .setBodyParameter(Param.responsibleRescr, String.valueOf(Profile.getInsatance().getUserId()))
                    .asString()
                    .get();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setRescuedCode(long accidentId){ //Code C (Closed)
        if(!SettingUtils.isNetworkConnected(context)){return false;}
        try {
            return Boolean.valueOf(Ion.with(context)
                    .load(Url.SET_RESCUED_CODE)
                    .setBodyParameter(Param.accidentId, String.valueOf(accidentId))
                    .setBodyParameter(Param.responsibleRescr, String.valueOf(Profile.getInsatance().getUserId()))
                    .asString()
                    .get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HospitalDistance> getNearestHospital(double latitude, double longitude){
        if(!SettingUtils.isNetworkConnected(context)){return null;}
        List<HospitalDistance> hospitalDistances= null;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Ion ion = Ion.getDefault(context);
        ion.configure().setGson(gson);
        try {
            hospitalDistances = (List<HospitalDistance>)ion.with(context)
                    .load(Url.GET_NEAREST_HOSPITAL)
                    .setTimeout(10000)
                    .setBodyParameter(Param.latitude, String.valueOf(latitude))
                    .setBodyParameter(Param.longitude, String.valueOf(longitude))
                    .as(TypeToken.get(new TypeToken<List<HospitalDistance>>(){}.getType()))
                    .get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return hospitalDistances;
    }

    public static class Url{
        public static final String HOST = "http://wwh.nata8ify.me:8080/WeeWorh-1.0-SNAPSHOT/"; //d
        public static final String RESCUER_LOGIN = HOST.concat("RescuerIn?opt=login&utyp=t");
        public static final String RESCUER_REGISTER = HOST.concat("To?opt=rregis");
        public static final String GET_TODAY_INBOUND_ACCIDENTS = HOST.concat("RescuerIn?opt=get_boundactacc");
        public static final String GET_ACCIDENTS_BY_ID = HOST.concat("RescuerIn?opt=get_accbyid");
        public static final String GET_REPORT_USER_INFO = HOST.concat("RescuerIn?opt=get_userinfo");
        public static final String GET_ORGANIZE_ID = HOST.concat("RescuerIn?opt=get_organization_id");
        public static final String SET_GOING_CODE = HOST.concat("RescuerIn?opt=set_ongoing");
        public static final String SET_RESCUED_CODE = HOST.concat("RescuerIn?opt=set_closed");
        public static final String SET_RESCUING_CODE = HOST.concat("RescuerIn?opt=set_onrescue");
        public static final String GET_NEAREST_HOSPITAL = HOST.concat("RescuerIn?opt=get_nearest_hospital");
    }

    public class Param{
        /*Login*/
        public static final String usrn = "usrn";
        public static final String pswd = "pswd";

        /* User & Profile Attrs */
        public static final String userId = "userId";

        /* Accident / Incident Param */
        public static final String accidentId = "accidentId";
        public static final String responsibleRescr = "responsibleRescr";
        public static final String latitude = "latitude";
        public static final String longitude = "longitude";

        /* Hospital */
        public static final String hospitalName = "name";

    }


}
