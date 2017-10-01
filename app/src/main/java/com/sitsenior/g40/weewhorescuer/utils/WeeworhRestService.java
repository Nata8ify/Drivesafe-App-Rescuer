package com.sitsenior.g40.weewhorescuer.utils;

import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.extra.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by PNattawut on 29-Sep-17.
 */


public interface WeeworhRestService{
    @FormUrlEncoded
    @POST("RescuerIn?opt=get_accbyid")
    Call<Accident> getIncidetById(@Field(Weeworh.Param.accidentId) long accidentId);

    @GET("RescuerIn?opt=get_inresponsibleacc")
    Call<Accident> getInResposibleIncidetByRescuerId(@Query(Weeworh.Param.userId) long userId);

    @GET("RescuerIn?opt=get_userinfo")
    Call<Profile> getReporterByIncidetById(@Query(Weeworh.Param.userId) long userId);

    @GET("RescuerIn?opt=get_boundactacc")
    Call<List<Accident>> getInBoundTodayIncidents(@Query(Weeworh.Param.userId) long userId);

    @GET("RescuerIn?opt=set_ongoing")
    Call<Boolean> setGoing(@Query((Weeworh.Param.responsibleRescr)) long responsibleRescr, @Query((Weeworh.Param.accidentId)) long accidentId);

}