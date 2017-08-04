package com.sitsenior.g40.weewhorescuer.cores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sitsenior.g40.weewhorescuer.LoginActivity;
import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.utils.DialogUtils;

/**
 * Created by PNattawut on 04-Aug-17.
 */

public class LoginAsyncTask extends AsyncTask<String, Void, Void> {
    private ProgressDialog loginLoadingProgressDialog;
    private Context context;

    public LoginAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loginLoadingProgressDialog = DialogUtils.getInstance(context).buildSimpleProgressDialog(null, context.getString(R.string.login_loading), false);
        loginLoadingProgressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        Weeworh.with(context).login(params[0], params[1]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        loginLoadingProgressDialog.dismiss();
        if (!(Profile.getInsatance().getUserId() == 0)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            context.startActivity(mainActivityIntent);
        } else {
            DialogUtils.getInstance(null).buildSimpleAlertDialog(context.getString(R.string.login_problem_title), context.getString(R.string.login_problem_message)).show();
        }
    }
}
