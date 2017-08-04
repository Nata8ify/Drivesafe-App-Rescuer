package com.sitsenior.g40.weewhorescuer.utils;

/**
 * Created by PNattawut on 04-Aug-17.
 */

public class AnimationUtils {
    private static AnimationUtils animationUtils;

    public static AnimationUtils getInstance(){
        if(animationUtils == null){
            animationUtils = new AnimationUtils();
        }
        return animationUtils;
    }
}
