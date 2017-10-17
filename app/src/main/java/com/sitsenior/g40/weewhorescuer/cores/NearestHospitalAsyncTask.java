package com.sitsenior.g40.weewhorescuer.cores;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sitsenior.g40.weewhorescuer.R;

/**
 * Created by PNattawut on 17-Oct-17.
 */

public class NearestHospitalAsyncTask extends AsyncTask {

    private Context context;
    private ProgressDialog progressDialog;

    public NearestHospitalAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.login_loading));
        progressDialog.setCancelable(false);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        progressDialog.cancel();
    }

}
