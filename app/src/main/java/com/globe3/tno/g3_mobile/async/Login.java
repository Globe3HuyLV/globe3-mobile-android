package com.globe3.tno.g3_mobile.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.globe3.tno.g3_mobile.activities.DashboardActivity;
import com.globe3.tno.g3_mobile.activities.LoginActivity;
import com.globe3.tno.g3_mobile.app_objects.User;
import com.globe3.tno.g3_mobile.app_objects.factory.UserFactory;
import com.globe3.tno.g3_mobile.constants.ConnectionStatus;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.app_objects.LoginDetails;
import com.globe3.tno.g3_mobile.app_objects.factory.CompanyFactory;

import org.json.JSONObject;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.ACTIVE_FEATURE_TIMESHEET_PROJECT;
import static com.globe3.tno.g3_mobile.globals.Globals.ACTIVE_FEATURE_TIMESHEET_SALES_ORDER;
import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANY_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.MAC;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.PASSWORD;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class Login extends AsyncTask<Void, Void, Boolean>  {
    private CompanyFactory company_factory;
    private UserFactory user_factory;

    private LoginActivity login_activity;
    private LoginDetails login_details;

    private int server_status = ConnectionStatus.SERVER_CONNECTED;

    int activeUsers;

    public Login(LoginActivity loginActivity, LoginDetails loginDetails){
        user_factory = new UserFactory(loginActivity);
        company_factory = new CompanyFactory(loginActivity);
        this.login_activity = loginActivity;
        this.login_details = loginDetails;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            activeUsers = user_factory.getActiveUsers().size();

            JSONObject masterJson = HttpUtility.requestJSON("master_setting", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&userloginid="+USERLOGINID);

            if(masterJson!=null){
                ACTIVE_FEATURE_TIMESHEET_PROJECT = masterJson.getBoolean("timesheet_by_project");
                ACTIVE_FEATURE_TIMESHEET_SALES_ORDER = masterJson.getBoolean("timesheet_by_sales_order");

                user_factory.updateMasterSettings();
            }

            if(activeUsers == 0){
                JSONObject loginResultJSON = HttpUtility.requestJSON("login", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&company="+ login_details.getCompany()+"&userloginid="+ login_details.getUserid()+"&password="+ login_details.getPassword());



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
                user_factory.setMasterSetting();
                User user = new User();
                user.setCompany(login_details.getCompany());
                user.setUserid(login_details.getUserid());
                user.setPassword(login_details.getPassword());

                if(user_factory.authenticate(user)){
                    JSONObject macResultJSON = HttpUtility.requestJSON("user_mac", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&userloginid="+user.getUserid());
                    if(macResultJSON!=null){
                        user.setMAC(macResultJSON.getString("items"));
                        user_factory.updateUser(user);
                    }

                    if(company_factory.getCompany(COMPANYFN) != null){
                        COMPANY_NAME = company_factory.getCompany(COMPANYFN).getName();
                    }else{
                        COMPANY_NAME = "";
                    }

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
                new MacUpdate(login_activity, user_factory).execute();
            }else{
                login_activity.startActivity(new Intent(login_activity, DashboardActivity.class));
            }
        }else{
            login_activity.onLoginFailed(server_status);
        }
    }
}
