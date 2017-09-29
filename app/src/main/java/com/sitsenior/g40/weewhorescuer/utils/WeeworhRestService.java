package com.sitsenior.g40.weewhorescuer.utils;

import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by PNattawut on 29-Sep-17.
 */


public interface WeeworhRestService{
    @FormUrlEncoded
    @POST("RescuerIn?opt=get_accbyid")
    Call<Accident> getIncidetById(@Field(Weeworh.Param.accidentId) String accidentId);
}