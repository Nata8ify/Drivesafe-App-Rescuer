package com.sitsenior.g40.weewhorescuer.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.sitsenior.g40.weewhorescuer.R;

/**
 * Created by PNattawut on 04-Aug-17.
 */

/** Just for Simple Dialog and Toast */
public class DialogUtils {
    private static Context context;
    private static DialogUtils dialogUtils;

    public static DialogUtils getInstance(@NonNull  Context context){
        if(DialogUtils.dialogUtils == null){
            DialogUtils.dialogUtils = new DialogUtils(context);
        }
        return DialogUtils.dialogUtils;
    }

    public DialogUtils(Context context) {
        DialogUtils.context = context;
    }

    private AlertDialog alertDialog;
    public AlertDialog buildSimpleAlertDialog(@Nullable String title, @NonNull String message){
       return alertDialog = new AlertDialog.Builder(context)
                .setTitle(title!=null?title:null)
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }).create();
    }



    private ProgressDialog progressDialog;
    public ProgressDialog buildSimpleProgressDialog(@Nullable String title, @NonNull String message, boolean cancelable){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title!=null?title:null);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }


    public void shortToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public void longToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
