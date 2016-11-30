package com.globe3.tno.g3_mobile.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.constants.ConnectionStatus;
import com.globe3.tno.g3_mobile.util.ConfigUtility;
import com.globe3.tno.g3_mobile.util.PermissionUtility;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.LoginDetails;
import com.globe3.tno.g3_mobile.async.AppUpdate;
import com.globe3.tno.g3_mobile.async.Login;
import com.globe3.tno.g3_mobile.util.FileUtility;
import com.neurotec.lang.NCore;
import com.neurotec.licensing.LicensingManager;
import com.neurotec.licensing.NLicense;

import java.io.IOException;

import static com.globe3.tno.g3_mobile.constants.App.REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICES_LICENSE_OBTAINED;
import static com.globe3.tno.g3_mobile.globals.Globals.EXTRACT_LICENSE_OBTAINED;
import static com.globe3.tno.g3_mobile.globals.Globals.MATCHER_LICENSE_OBTAINED;

public class LoginActivity extends BaseActivity{
    LoginActivity loginActivity;

    EditText tv_company;
    EditText tv_user;
    EditText tv_password;
    LinearLayout ll_login_button;
    Button btn_login;
    LinearLayout ll_login_loader;
    ImageView iv_login_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        loginActivity = this;

        tv_company = (EditText) findViewById(R.id.tv_company);
        tv_user = (EditText) findViewById(R.id.tv_userid);
        tv_password = (EditText) findViewById(R.id.tv_password);
        ll_login_button = (LinearLayout) findViewById(R.id.ll_login_button);
        btn_login = (Button) findViewById(R.id.btn_login);
        ll_login_loader = (LinearLayout) findViewById(R.id.ll_login_loader);
        iv_login_loader = (ImageView) findViewById(R.id.iv_login_loader);
    }

    @Override
    public void onResume(){
        super.onResume();
        tv_company.setText("");
        tv_user.setText("");
        tv_password.setText("");
        enableLogin(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    PermissionUtility.requestWriteStorage(loginActivity);
                }else{
                    if(FileUtility.createAppFolders(loginActivity)){
                        new LoadConfig().execute();
                    }else{
                        onActivityError();
                    }
                }
            }
        }
    }

    public void onActivityLoading(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(loginActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if(FileUtility.createAppFolders(loginActivity)){
                    if(!ConfigUtility.loadConfig(loginActivity)){
                        onActivityError();
                    }
                }else{
                    onActivityError();
                }
            }else{
                PermissionUtility.requestWriteStorage(loginActivity);
            }
        }

        NCore.setContext(this);
        try {
            EXTRACT_LICENSE_OBTAINED = NLicense.obtainComponents("/local", 5000, LicensingManager.LICENSE_FINGER_EXTRACTION);
            MATCHER_LICENSE_OBTAINED = NLicense.obtainComponents("/local", 5000, LicensingManager.LICENSE_FINGER_MATCHING);
            DEVICES_LICENSE_OBTAINED = NLicense.obtainComponents("/local", 5000, LicensingManager.LICENSE_FINGER_DEVICES_SCANNERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActivityReady(){
        iv_login_loader.setAnimation(AnimationUtils.loadAnimation(loginActivity, R.anim.animate_rotate_clockwise));
    }

    public void doLogin(View view){
        enableLogin(false);
        Runnable loginProcess = new Runnable() {
            @Override
            public void run() {
                LoginDetails loginDetails = new LoginDetails();
                loginDetails.setCompany(tv_company.getText().toString());
                loginDetails.setUserid(tv_user.getText().toString());
                loginDetails.setPassword(tv_password.getText().toString());
                Login login = new Login(loginActivity, loginDetails);
                login.execute();
            }
        };

        new AppUpdate(loginActivity, loginProcess).execute();
    }

    public void onLoginFailed(int server_status){
        Snackbar loginSnackbar = Snackbar.make(layout_main, getString(server_status== ConnectionStatus.SERVER_CONNECTED?R.string.msg_authentication_failed:R.string.msg_cannot_connect_to_server), Snackbar.LENGTH_LONG);
        TextView loginSnackbarText = (TextView) loginSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        loginSnackbarText.setTextColor(ContextCompat.getColor(loginActivity, R.color.colorFailed));
        loginSnackbar.show();
        enableLogin(true);
    }

    public void enableLogin(boolean enable){
        tv_company.setEnabled(enable);
        tv_user.setEnabled(enable);
        tv_password.setEnabled(enable);
        ll_login_button.setVisibility(enable ? View.VISIBLE : View.GONE);
        ll_login_loader.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private class LoadConfig extends AsyncTask<Void, Void, Boolean> {
        protected void onPreExecute(){
            layout_base_loader.setVisibility(View.VISIBLE);
            image_loader.setAnimation(AnimationUtils.loadAnimation(baseActivity, R.anim.animate_rotate_clockwise));
            layout_main.setVisibility(View.INVISIBLE);
        }

        protected Boolean doInBackground(Void... params) {
            return ConfigUtility.loadConfig(loginActivity);
        }
        protected void onPostExecute(Boolean loadSuccess) {
            if(loadSuccess){
                layout_base_loader.setVisibility(View.INVISIBLE);
                layout_main.setAnimation(AnimationUtils.loadAnimation(baseActivity, R.anim.animate_fade_in));
                layout_main.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout_main.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layout_main.startAnimation(layout_main.getAnimation());
            }else{
                onActivityError();
            }
        }
    }
}
