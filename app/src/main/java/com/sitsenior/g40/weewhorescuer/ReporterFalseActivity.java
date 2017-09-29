package com.sitsenior.g40.weewhorescuer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sitsenior.g40.weewhorescuer.models.extra.ReporterProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReporterFalseActivity extends AppCompatActivity {

    @BindView(R.id.btn_call)
    Button btnCall;
    @BindView(R.id.btn_acknowledge)
    Button btnAcknowledge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporter_false);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_call, R.id.btn_acknowledge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_call:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:".concat(ReporterProfile.getInstance().getPhoneNumber())));
                startActivity(callIntent);
                break;
            case R.id.btn_acknowledge:
                finish();
                break;
        }
    }
}
