package com.sitsenior.g40.weewhorescuer.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by PNattawut on 25-Sep-17.
 */

public class GoingService extends IntentService {

    public GoingService() {
        this("GoingService");
    }

    public GoingService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
