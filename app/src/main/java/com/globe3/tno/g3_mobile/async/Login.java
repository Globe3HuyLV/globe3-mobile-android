package com.globe3.tno.g3_mobile.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.globe3.tno.g3_mobile.activities.DashboardActivity;
import com.globe3.tno.g3_mobile.activities.LoginActivity;
import com.globe3.tno.g3_mobile.app_objects.User;
import com.globe3.tno.g3_mobile.app_objects.factory.UserFactory;
import com.globe3.tno.g3_mobile.constants.App;
import com.globe3.tno.g3_mobile.constants.ConnectionStatus;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.app_objects.LoginDetails;
import com.globe3.tno.g3_mobile.app_objects.factory.CompanyFactory;

import org.json.JSONObject;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANY_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.MAC;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.PASSWORD;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class Login extends AsyncTask<Void, Void, Boolean>  {
    private CompanyFactory companyFactory;
    private UserFactory userFactory;

    private LoginActivity loginActivity;
    private LoginDetails loginDetails;

    private int server_status = ConnectionStatus.SERVER_CONNECTED;

    int activeUsers;

    public Login(LoginActivity loginActivity, LoginDetails loginDetails){
        userFactory = new UserFactory(loginActivity);
        companyFactory = new CompanyFactory(loginActivity);
        this.loginActivity = loginActivity;
        this.loginDetails = loginDetails;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            activeUsers = userFactory.getActiveUsers().size();
            if(activeUsers == 0){
                JSONObject loginResultJSON = HttpUtility.requestJSON("login", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&company="+loginDetails.getCompany()+"&userloginid="+loginDetails.getUserid()+"&password="+loginDetails.getPassword());

                server_status = ConnectionStatus.SERVER_CONNECTED;
                if(loginResultJSON.getBoolean("login_valid")){
                    COMPANYFN = loginResultJSON.getString("cfnunique");
                    COMPANY_NAME = loginResultJSON.getString("entity_name");
                    USERLOGINUNIQ = loginResultJSON.getString("unique");
                    USERLOGINID = loginResultJSON.getString("userloginid");
                    PASSWORD = loginResultJSON.getString("password");
                    MAC = loginResultJSON.getString("mac");
                    return true;
                }else{
                    return false;
                }
            }else{
                User user = new User();
                user.setCompany(loginDetails.getCompany());
                user.setUserid(loginDetails.getUserid());
                user.setPassword(loginDetails.getPassword());

                if(userFactory.authenticate(user)){
                    JSONObject macResultJSON = HttpUtility.requestJSON("user_mac", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&userloginid="+user.getUserid());
                    if(macResultJSON!=null){
                        user.setMAC(macResultJSON.getString("items"));
                        userFactory.updateUser(user);
                    }
                    COMPANY_NAME = companyFactory.getCompany(COMPANYFN).getName();
                    return true;
                }else{
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            server_status = ConnectionStatus.SERVER_DISCONNECTED;
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean loginSuccess) {
        if(loginSuccess){
            if(activeUsers > 0){
                new MacUpdate(loginActivity, userFactory).execute();
            }else{
                loginActivity.startActivity(new Intent(loginActivity, DashboardActivity.class));
            }
        }else{
            loginActivity.onLoginFailed(server_status);
        }
    }
}
