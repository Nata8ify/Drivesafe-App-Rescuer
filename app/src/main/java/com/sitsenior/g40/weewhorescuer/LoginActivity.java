package com.sitsenior.g40.weewhorescuer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.utils.SettingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.edtxt_loginUsername)
    EditText edtxtLoginUsername;
    @BindView(R.id.edtxt_loginPassword)
    EditText edtxtLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    @BindView(R.id.txt_headMessage)
    TextView txtHeadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SettingUtils.requestPermission(this);
        LocationFactory.getInstance(this).activatedLocation();

}

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        if (!SettingUtils.isNetworkConnected(this)) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_login:
                Weeworh.with(this).login(edtxtLoginUsername.getText().toString(), edtxtLoginPassword.getText().toString());
                if (!(Profile.getInsatance().getUserId() == 0)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    txtHeadMessage.setVisibility(View.VISIBLE);
                    txtHeadMessage.setText(getResources().getString(R.string.login_nouser));
                }
                break;
            case R.id.btn_register:
                //TODO
                break;
        }
    }
}
