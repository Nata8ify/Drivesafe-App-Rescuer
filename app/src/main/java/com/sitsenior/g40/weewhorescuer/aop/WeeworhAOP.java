package com.sitsenior.g40.weewhorescuer.aop;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by PNattawut on 12-Sep-17.
 */

@Aspect
public class WeeworhAOP {

    @Before("execution(* com.sitsenior.g40.weewhorescuer.cores.Weeworh.get*(..))")
    public void connectivityCheck(JoinPoint joinPoint){
        Log.d("connectivityCheck", "connectivityCheck");
    }
}
