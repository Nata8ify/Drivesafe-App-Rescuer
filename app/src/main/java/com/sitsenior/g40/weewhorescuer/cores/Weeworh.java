package com.sitsenior.g40.weewhorescuer.cores;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class Weeworh {

    private static Context context;
    public static Weeworh weeworh;
    public static Weeworh with(Context context){
        if(weeworh == null){
            weeworh = new Weeworh();
        }
        Weeworh.context = context;
        return weeworh;
    }

    public Profile login(String username, String password){
        Profile rescuerProfile = null;
        try {
            rescuerProfile = Ion.with(context)
                    .load(Url.RESCUER_LOGIN)
                    .progressDialog(new ProgressDialog(context))
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

    public List<Accident> getInBoundTodayIncidents(long userId){
        List<Accident> accidents = null;
        try {
            accidents = (List<Accident>)Ion.with(context)
                    .load(Url.GET_TODAY_INBOUND_ACCIDENTS)
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

    static class Url{
        public static final String HOST = "http://54.169.83.168:8080/WeeWorh-1.0-SNAPSHOT/";
        public static final String RESCUER_LOGIN = HOST.concat("RescuerIn?opt=login&utyp=t");
        public static final String GET_TODAY_INBOUND_ACCIDENTS = HOST.concat("RescuerIn?opt=get_boundactacc");
    }

    class Param{
        /*Login*/
        public static final String usrn = "usrn";
        public static final String pswd = "pswd";

        /* User & Profile Attrs */
        public static final String userId = "userId";


    }

}
