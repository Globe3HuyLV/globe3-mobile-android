package com.globe3.tno.g3_mobile.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ActionProvider;


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
    LoginActivity loginActivity;
    public UserFactory userFactory;

    public MacUpdate(LoginActivity loginActivity, UserFactory userFactory){
        this.loginActivity = loginActivity;
        this.userFactory = userFactory;
    }

    @Override
    protected Void doInBackground(Void... param) {
        User currentUser = userFactory.getUser(USERLOGINUNIQ);
        try {
            JSONObject json = HttpUtility.requestJSON("user_mac", "cfsqlfilename="+CFSQLFILENAME+"&masterfn="+MASTERFN+"&userloginid="+USERLOGINID);


            if(json!=null){
                currentUser.setMAC(json.getString("items"));
                userFactory.updateUser(currentUser);
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
        loginActivity.startActivity(new Intent(loginActivity, DashboardActivity.class));
    }
}
