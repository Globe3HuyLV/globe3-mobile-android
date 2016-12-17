package com.globe3.tno.g3_mobile.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import com.globe3.tno.g3_mobile.activities.DashboardActivity;
import com.globe3.tno.g3_mobile.activities.LoginActivity;
import com.globe3.tno.g3_mobile.app_objects.User;
import com.globe3.tno.g3_mobile.app_objects.factory.UserFactory;
import com.globe3.tno.g3_mobile.util.HttpUtility;

import org.json.JSONObject;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.ACTIVE_FEATURE_TIMESHEET_PROJECT;
import static com.globe3.tno.g3_mobile.globals.Globals.ACTIVE_FEATURE_TIMESHEET_SALES_ORDER;
import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.MAC;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class MacUpdate extends AsyncTask<Void, Integer, Void>
{
    LoginActivity login_activity;
    public UserFactory user_factory;

    public MacUpdate(LoginActivity loginActivity, UserFactory userFactory){
        this.login_activity = loginActivity;
        this.user_factory = userFactory;
    }

    @Override
    protected Void doInBackground(Void... param) {
        User currentUser = user_factory.getUser(USERLOGINUNIQ);
        try {
            JSONObject macJson = HttpUtility.requestJSON("user_mac", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&userloginid="+USERLOGINID);

            if(macJson!=null){
                currentUser.setMAC(macJson.getString("items"));
                user_factory.updateUser(currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MAC = currentUser.getMAC();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        login_activity.startActivity(new Intent(login_activity, DashboardActivity.class));
    }
}
