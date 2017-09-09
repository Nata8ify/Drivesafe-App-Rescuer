package com.sitsenior.g40.weewhorescuer.cores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.models.Profile;

/**
 * Created by nata8ify on 5/8/2560.
 */

public class ViewReportUserInfoAsyncTask extends AsyncTask<Long, Void, Void> {

    private Context context;
    private AlertDialog viewReportUserInfoDialog;
    private Profile userProfile;

    public ViewReportUserInfoAsyncTask(Context contex) {
        this.context = contex;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Long... userId) {
        /* User Data */
        userProfile = Weeworh.with(context).getReportUserInformation(userId[0]);
        Log.d("userProfile", userProfile.toString());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        showReportUserInfoDialog(userProfile);
    }

    public void showReportUserInfoDialog(final Profile userProfile){
        android.support.v7.app.AlertDialog reportUserDialog = new android.support.v7.app.AlertDialog.Builder(context)
                .setPositiveButton(context.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.call), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:".concat(userProfile.getPhoneNumber())));
                        ((Activity)context).startActivity(callIntent);
                    }
                })
                .create();
        View userInfoView = LayoutInflater.from(context).inflate(R.layout.view_userinfo, null);
        TextView txtUInfoName = ((TextView) userInfoView.findViewById(R.id.txt_uinfo_name));
        txtUInfoName.setText(formatName(userProfile.getFirstName(), userProfile.getLastName()));
        TextView txtUInfoPersonalId = ((TextView) userInfoView.findViewById(R.id.txt_uinfo_personno));
        txtUInfoPersonalId.setText(formatPersonalId(userProfile.getPersonalId()));
        TextView txtUInfoAddress = ((TextView) userInfoView.findViewById(R.id.txt_uinfo_address));
        txtUInfoAddress.setText(userProfile.getAddress1().concat(userProfile.getAddress2() != null ? "\n".concat(userProfile.getAddress2()) : ""));
        TextView txtUInfoPhoneNo = ((TextView) userInfoView.findViewById(R.id.txt_uinfo_phoneno));
        txtUInfoPhoneNo.setText(userProfile.getPhoneNumber());
        reportUserDialog.setView(userInfoView);
        reportUserDialog.show();
    }

    public String formatName(String firstName, String lastName){
        return firstName.concat(" ").concat(lastName);
    }

    public String formatPersonalId(long personalId){
        StringBuilder formatPersonalIdBuilder = new StringBuilder(String.valueOf(personalId));
        final String DASH = "-";
        formatPersonalIdBuilder.insert(1,DASH);
        formatPersonalIdBuilder.insert(6,DASH);
        formatPersonalIdBuilder.insert(12,DASH);
        formatPersonalIdBuilder.insert(15,DASH);
        return  formatPersonalIdBuilder.toString();
    }

}
