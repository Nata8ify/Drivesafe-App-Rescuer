package com.sitsenior.g40.weewhorescuer.cores;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class Weeworh {

    public static Weeworh weeworh;
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
        Profile reportUserProfile = null;
        try {
            reportUserProfile = Ion.with(context)
                    .load(Url.GET_REPORT_USER_INFO)
                    .setBodyParameter("userId", String.valueOf(userId))
                    .as(Profile.class)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reportUserProfile;
    }

    public List<Accident> getInBoundTodayIncidents(long userId){
        List<Accident> accidents = null;
        try {
            accidents = (List<Accident>)Ion.with(context)
                    .load(Url.GET_TODAY_INBOUND_ACCIDENTS)
                    .setBodyParameter(Param.userId, String.valueOf(userId))
                    .as(TypeToken.get(new TypeToken<List<Accident>>(){}.getType()))
                    .get();
            Log.d("accs", accidents.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return accidents;
    }

    static class Url{
        public static final String HOST = "http://54.169.83.168:8080/WeeWorh-1.0-SNAPSHOT/";
        public static final String RESCUER_LOGIN = HOST.concat("RescuerIn?opt=login&utyp=t");
        public static final String GET_TODAY_INBOUND_ACCIDENTS = HOST.concat("RescuerIn?opt=get_boundactacc");
        public static final String GET_REPORT_USER_INFO = HOST.concat("RescuerIn?opt=get_userinfo");
    }

    class Param{
        /*Login*/
        public static final String usrn = "usrn";
        public static final String pswd = "pswd";

        /* User & Profile Attrs */
        public static final String userId = "userId";


    }

}
