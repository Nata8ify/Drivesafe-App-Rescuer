package com.sitsenior.g40.weewhorescuer.cores;

import com.sitsenior.g40.weewhorescuer.models.Accident;

import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class Weeworh {

    private static final String HOST = "http://54.169.83.168:8080/WeeWorh-1.0-SNAPSHOT/";

    public static List<Accident> getInBoundTodayIncidents(long userId){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(HOST+)
        return null;
    }

    class Param{
        public static final String userId = "userId";
    }

}
