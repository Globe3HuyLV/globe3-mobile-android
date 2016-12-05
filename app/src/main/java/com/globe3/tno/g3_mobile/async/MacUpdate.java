package com.globe3.tno.g3_mobile.async;

import android.content.Intent;
import android.os.AsyncTask;


import com.globe3.tno.g3_mobile.activities.DashboardActivity;
import com.globe3.tno.g3_mobile.activities.LoginActivity;
import com.globe3.tno.g3_mobile.app_objects.User;
import com.globe3.tno.g3_mobile.app_objects.factory.UserFactory;
import com.globe3.tno.g3_mobile.util.HttpUtility;

import org.json.JSONObject;

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
            JSONObject json = HttpUtility.requestJSON("user_mac", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&userloginid="+USERLOGINID);


            if(json!=null){
                currentUser.setMAC(json.getString("items"));
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
