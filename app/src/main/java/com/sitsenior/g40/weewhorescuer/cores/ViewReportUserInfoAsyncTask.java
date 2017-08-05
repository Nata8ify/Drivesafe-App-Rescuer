package com.sitsenior.g40.weewhorescuer.cores;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by nata8ify on 5/8/2560.
 */

public class ViewReportUserInfoAsyncTask extends AsyncTask<Long, Void, Void> {

    private Context contex;

    public ViewReportUserInfoAsyncTask(Context contex) {
        this.contex = contex;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Long... userId) {
        /* User Data */
        Profile userProfile = Weeworh.with(contex).getReportUserInformation(userId[0]);
        Log.d("userProfile", userProfile.toString());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}
