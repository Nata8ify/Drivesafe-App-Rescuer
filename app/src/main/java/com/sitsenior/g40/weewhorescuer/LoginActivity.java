package com.sitsenior.g40.weewhorescuer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.LoginAsyncTask;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.utils.SettingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

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
    @BindView(R.id.chkbox_rememberme)
    CheckBox chkboxRememberme;

    private Handler loginHandler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Realm.init(this);readyForRememberMe();
        SettingUtils.requestPermission(this);
        LocationFactory.getInstance(this).activatedLocation();
        loginHandler = new Handler();
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        if (!SettingUtils.isNetworkConnected(this)) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_login:
                new LoginAsyncTask(this, chkboxRememberme.isChecked()).execute(edtxtLoginUsername.getText().toString(), edtxtLoginPassword.getText().toString());
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.btn_register:
                //TODO
                break;
        }
    }

    public void readyForRememberMe(){
        Realm realm = Realm.getDefaultInstance();
        Profile rememberProfile = realm.where(Profile.class)
                .findFirst();
        Profile.set(rememberProfile);
        if(Profile.getInsatance().getUserId() != 0){
            Log.d("LOGIN", Profile.getInsatance().toString());
        } else {
            Log.d("LOGIN", "No Remember Now");
        }
    }

}
