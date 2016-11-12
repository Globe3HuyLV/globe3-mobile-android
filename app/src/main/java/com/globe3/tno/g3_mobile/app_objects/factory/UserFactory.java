package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;

import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.User;
import com.globe3.tno.g3_mobile.model.EntityRepo;
import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.model.entities.useraccess;
import com.globe3.tno.g3_mobile.util.DateUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.globe3.tno.g3_mobile.model.UseraccessRepo;
import com.globe3.tno.g3_mobile.model.TabledataRepo;
import com.globe3.tno.g3_mobile.util.Encryption;
import com.globe3.tno.g3_mobile.util.Uniquenum;

import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MAC;
import static com.globe3.tno.g3_mobile.globals.Globals.PASSWORD;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class UserFactory {
    UseraccessRepo useraccess_repo;
    EntityRepo entity_repo;
    TabledataRepo tabledata_repo;

    public UserFactory(Context context) {
        useraccess_repo = new UseraccessRepo(context);
        entity_repo = new EntityRepo(context);
        tabledata_repo = new TabledataRepo(context);
    }

    public void createUser(User user) {
        useraccess_repo.open();
        useraccess_repo.create_useraccess(convertToEntity(user));
        useraccess_repo.close();
    }

    public void createUser(JSONObject userJson, LogItem logItem) {

        useraccess useraccess = new useraccess();
        tabledata tabledata = new tabledata();

        try {
            useraccess.tag_table_usage = userJson.getString("tag_table_usage");
            useraccess.uniquenum_pri = userJson.getString("uniquenum_pri");
            useraccess.uniquenum_sec = userJson.getString("uniquenum_sec");
            useraccess.active_yn = userJson.getString("tag_deleted_yn").equals("y") ? "n" : "y";
            useraccess.date_post = DateUtility.getStringDate(userJson.getString("date_post"));
            useraccess.date_submit = DateUtility.getStringDate(userJson.getString("date_submit"));
            useraccess.date_lastupdate = DateUtility.getStringDate(userJson.getString("date_lastupdate"));
            useraccess.sync_unique = logItem.getLogUnique();
            useraccess.date_sync = logItem.getLogDate();
            useraccess.cfsqlfilename = userJson.getString("cfsqlfilename");
            useraccess.masterfn = userJson.getString("masterfn");
            useraccess.companyfn = userJson.getString("companyfn");
            useraccess.companyid = userJson.getString("companyloginid");
            useraccess.userid = userJson.getString("userloginid");
            useraccess.password = userJson.getString("password");
            useraccess.date_valid_to = DateUtility.getStringDate(userJson.getString("date_valid_to"));
            useraccess.date_valid_from = DateUtility.getStringDate(userJson.getString("date_valid_from"));
            useraccess.staff_code = userJson.getString("staff_code");
            useraccess.staff_desc = userJson.getString("staff_desc");
            useraccess.staff_unique = userJson.getString("staff_unique");
            useraccess.user_specialnum = userJson.getString("mac");

            tabledata.tag_table_usage = "co_list";
            tabledata.uniquenum_pri = Uniquenum.Generate();
            tabledata.uniquenum_sec = userJson.getString("uniquenum_pri");
            tabledata.companyfn = userJson.getString("companyfn");
            tabledata.masterfn = userJson.getString("masterfn");
            tabledata.sync_unique = logItem.getLogUnique();
            tabledata.nvar100_01 = userJson.getString("company_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        useraccess_repo.open();
        useraccess_repo.create_useraccess(useraccess);
        useraccess_repo.close();

        tabledata_repo.open();
        tabledata_repo.create_tabledata(tabledata);
        tabledata_repo.close();
    }

    public void deleteUser(User user) {
        useraccess_repo.open();
        useraccess_repo.delete_useraccess(convertToEntity(user));
        useraccess_repo.close();
    }

    public void deleteAll() {
        useraccess_repo.open();
        useraccess_repo.delete_useraccess_all();
        useraccess_repo.close();
    }

    public void updateUser(User user) {
        useraccess_repo.open();
        useraccess_repo.update_useraccess(convertToEntity(user));
        useraccess_repo.close();
    }

    public ArrayList<User> getActiveUsers() {
        ArrayList<User> users = new ArrayList<User>();
        useraccess_repo.open();
        for(useraccess useraccess : useraccess_repo.get_active_useraccess()){
            users.add(convertEntity(useraccess));
        }
        useraccess_repo.close();
        return users;
    }

    public User getUser(String pUniquenum){
        useraccess_repo.open();
        User user = convertEntity(useraccess_repo.get_useraccess(pUniquenum));
        useraccess_repo.close();
        return user;
    }

    public User getUserByName(String pUserLoginId){
        useraccess_repo.open();
        User user = convertEntity(useraccess_repo.get_useraccess_by_name(pUserLoginId));
        useraccess_repo.close();
        return user;
    }

    private User convertEntity(useraccess useraccess){
        User user = new User();
        user.setCompany(useraccess.companyfn);
        user.setUserid(useraccess.userid);
        user.setPassword(useraccess.password);
        user.setMAC(useraccess.user_specialnum);

        tabledata_repo.open();
        tabledata tabledata = tabledata_repo.get_tabledata_user_co_list(useraccess.uniquenum_pri);
        if(tabledata!=null){
            user.setCompanies(tabledata.nvar100_01);
        }
        tabledata_repo.close();
        return user;
    }

    public boolean authenticate(User user){
        boolean res;
        useraccess_repo.open();
        res = useraccess_repo.useraccess_authenticate(user.getCompany(), user.getUserid(), Encryption.md5(user.getPassword()));
        if(res){
            useraccess useraccess = useraccess_repo.useraccess_get_by_login(user.getCompany(), user.getUserid(), Encryption.md5(user.getPassword()));
            COMPANYFN = useraccess.companyfn;
            USERLOGINUNIQ = useraccess.uniquenum_pri;
            USERLOGINID = useraccess.userid;
            PASSWORD = useraccess.password;
            MAC = useraccess.user_specialnum;
        }
        useraccess_repo.close();
        return res;
    }

    private useraccess convertToEntity(User user){
        useraccess useraccess = new useraccess();
        useraccess.companyfn = user.getCompany();
        useraccess.userid = user.getUserid();
        useraccess.password = user.getPassword();
        useraccess.user_specialnum = user.getMAC();
        return useraccess;
    }
}
