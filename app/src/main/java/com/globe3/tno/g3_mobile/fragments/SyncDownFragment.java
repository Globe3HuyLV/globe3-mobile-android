package com.globe3.tno.g3_mobile.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.activities.DashboardActivity;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.TimeRecord;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.ProjectFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.SalesOrderFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.UserFactory;
import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.util.BiometricUtility;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.factory.CompanyFactory;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.BIOMETRIC_DATA;
import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class SyncDownFragment extends DialogFragment {
    Activity parent_activity;

    AuditFactory audit_factory;
    UserFactory user_factory;
    CompanyFactory company_factory;
    ProjectFactory project_factory;
    StaffFactory staff_factory;
    SalesOrderFactory sales_order_factory;

    ImageView iv_icon_sync_down;
    TextView tv_sync_down_progress;
    TextView tv_sync_down_desc;
    LinearLayout ll_done;
    TextView tv_done;

    int sync_total = 0;
    int sync_progress = 0;

    ArrayList<TimeRecord> pending_timesheet;
    ArrayList<Staff> pending_staff;

    ArrayList<UploadStaff> upload_staff_list;
    ArrayList<UploadTimelog> upload_timelog_list;

    ArrayList<UserWriteDB> write_db_user_list;
    ArrayList<CompanyWriteDB> write_db_company_list;
    ArrayList<ProjectWriteDB> write_db_project_list;
    ArrayList<StaffWriteDB> write_db_staff_list;
    ArrayList<SalesOrderWriteDB> write_db_salesorder_list;
    ArrayList<UpdateFinger> update_finger_list;

    int upload_staff_que_num = 0;
    int upload_timelog_que_num = 0;

    int write_db_user_que_num = 0;
    int write_db_company_que_num = 0;
    int write_db_project_que_num = 0;
    int write_db_staff_que_num = 0;
    int write_db_salesorder_que_num = 0;
    int update_finger_que_num = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        parent_activity = getActivity();

        audit_factory = new AuditFactory(parent_activity);
        user_factory = new UserFactory(parent_activity);
        company_factory = new CompanyFactory(parent_activity);
        project_factory = new ProjectFactory(parent_activity);
        staff_factory = new StaffFactory(parent_activity);
        sales_order_factory = new SalesOrderFactory(parent_activity);

        View syncDownFragment = inflater.inflate(R.layout.fragment_sync_down, viewGroup, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        iv_icon_sync_down = (ImageView) syncDownFragment.findViewById(R.id.iv_icon_sync_down);
        tv_sync_down_progress = (TextView) syncDownFragment.findViewById(R.id.tv_sync_down_progress);
        tv_sync_down_desc = (TextView) syncDownFragment.findViewById(R.id.tv_sync_down_desc);
        ll_done = (LinearLayout) syncDownFragment.findViewById(R.id.ll_done);
        tv_done = (TextView) syncDownFragment.findViewById(R.id.tv_done);

        iv_icon_sync_down.setAnimation(AnimationUtils.loadAnimation(parent_activity, R.anim.animate_rotate_counter_clockwise));
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        pending_staff = new ArrayList<>();
        pending_timesheet = new ArrayList<>();

        upload_staff_list = new ArrayList<>();
        upload_timelog_list = new ArrayList<>();
        write_db_user_list = new ArrayList<>();
        write_db_company_list = new ArrayList<>();
        write_db_project_list = new ArrayList<>();
        write_db_staff_list = new ArrayList<>();
        write_db_salesorder_list = new ArrayList<>();
        update_finger_list = new ArrayList<>();

        new SyncUp().execute();

        return syncDownFragment;
    }

    private void updateProgress(){
        sync_progress++;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_sync_down_progress.setText(String.valueOf((int)Math.floor(((float) sync_progress /(float) sync_total)*100)) + "%");
            }
        });
    }

    private void showSyncStatus(final boolean syncSuccess){
        user_factory.closeRepo();
        company_factory.closeRepo();
        project_factory.closeRepo();
        staff_factory.closeRepo();
        sales_order_factory.closeRepo();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(syncSuccess){
                    tv_sync_down_progress.setTextColor(ContextCompat.getColor(parent_activity, R.color.colorBlackLight));
                    tv_sync_down_desc.setText(getString(R.string.msg_sync_down_complete));
                    tv_sync_down_desc.setVisibility(View.VISIBLE);
                    tv_done.setText(getString(R.string.label_done));
                }else{
                    tv_sync_down_progress.setText(getString(R.string.msg_sync_failed));
                    tv_sync_down_progress.setTextColor(ContextCompat.getColor(parent_activity, R.color.colorFailed));
                    tv_done.setText(getString(R.string.label_cancel));
                    tv_sync_down_desc.setVisibility(View.GONE);
                }
                iv_icon_sync_down.setAnimation(null);
                ll_done.setVisibility(View.VISIBLE);
                ((DashboardActivity)getActivity()).updateDashboard();
            }
        });
    }

    private class SyncUp extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            LogItem logItem = audit_factory.Log(TagTableUsage.DATA_SYNC_DOWN);
            try {
                pending_staff = staff_factory.getPendingtaffs();
                pending_timesheet = staff_factory.getPendingTimelogs();

                sync_total = pending_staff.size()+pending_timesheet.size();

                for(Staff staff : pending_staff){
                    upload_staff_list.add(new UploadStaff(staff, logItem));
                }

                for(TimeRecord timeRecord : pending_timesheet){
                    upload_timelog_list.add(new UploadTimelog(timeRecord, logItem));
                }

                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean uploadSuccess) {
            if(uploadSuccess){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syncPendingStaff();
                    }
                });
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class SyncDown extends AsyncTask<Void, Void, Boolean> {
        LogItem logItem;
        JSONArray users;
        JSONArray companies;
        JSONArray projects;
        JSONArray teams;
        JSONArray staffs;
        JSONArray staff_projects;
        JSONArray staff_teams;
        JSONArray saleorders;
        JSONArray saleorder_team;

        @Override
        protected Boolean doInBackground(Void... params) {
            logItem = audit_factory.Log(TagTableUsage.DATA_SYNC_DOWN);
            try {
                JSONObject userResultJSON = HttpUtility.requestJSON("usersync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject companyResultJSON = HttpUtility.requestJSON("entitysync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject projectResultJSON = HttpUtility.requestJSON("projectsync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject teamResultJSON = HttpUtility.requestJSON("teamsync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject staffResultJSON = HttpUtility.requestJSON("staffsync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject staffProjectResultJSON = HttpUtility.requestJSON("project_staff_assign_sync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject staffTeamResultJSON = HttpUtility.requestJSON("team_staff_assign_sync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject salesOrdeResultJSON = HttpUtility.requestJSON("salesorder_sync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject salesOrdeTeamResultJSON = HttpUtility.requestJSON("salesorder_team_sync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);

                users = userResultJSON.getJSONArray("items");
                companies = companyResultJSON.getJSONArray("items");
                projects = projectResultJSON.getJSONArray("items");
                teams = teamResultJSON.getJSONArray("items");
                staffs = staffResultJSON.getJSONArray("items");
                staff_projects = staffProjectResultJSON.getJSONArray("items");
                staff_teams = staffTeamResultJSON.getJSONArray("items");
                saleorders = salesOrdeResultJSON.getJSONArray("items");
                saleorder_team = salesOrdeTeamResultJSON.getJSONArray("items");

                if(     userResultJSON!=null&&
                        companyResultJSON!=null&&
                        projectResultJSON!=null&&
                        teamResultJSON!=null&&
                        staffResultJSON!=null&&
                        staffProjectResultJSON!=null&&
                        staffTeamResultJSON!=null&&
                        salesOrdeResultJSON!=null&&
                        salesOrdeTeamResultJSON!=null
                    ){

                    user_factory.deleteAll();
                    company_factory.deleteAll();
                    project_factory.deleteAll();
                    staff_factory.deleteAllTeam();
                    staff_factory.deleteAll();
                    staff_factory.deleteAllStaffProject();
                    staff_factory.deleteAllStaffTeam();
                    sales_order_factory.deleteAll();
                    sales_order_factory.deleteAllSalesOrderTeam();

                    sync_total = users.length()+companies.length()+projects.length()+teams.length()+(staffs.length()*2)+ staff_projects.length()+staff_teams.length()+saleorders.length()+saleorder_team.length();

                    try {
                        for(int i=0;i<users.length();i++)
                        {
                            final JSONObject userJson = users.getJSONObject(i);
                            write_db_user_list.add(new UserWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    user_factory.downloadUser(userJson, logItem);
                                }
                            }));
                        }

                        for(int i=0;i<companies.length();i++)
                        {
                            final JSONObject companyJson = companies.getJSONObject(i);
                            write_db_company_list.add(new CompanyWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    company_factory.downloadCompany(companyJson, logItem);
                                }
                            }));
                        }

                        for(int i=0;i<projects.length();i++)
                        {
                            final JSONObject projectJson = projects.getJSONObject(i);
                            write_db_project_list.add(new ProjectWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    project_factory.downloadProject(projectJson, logItem);
                                }
                            }));

                        }

                        for(int i=0;i<teams.length();i++){
                            final JSONObject teamJson = teams.getJSONObject(i);
                            write_db_staff_list.add(new StaffWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    staff_factory.downloadTeam(teamJson, logItem);
                                }
                            }));
                        }

                        for(int i=0;i<staffs.length();i++)
                        {
                            final JSONObject staffJson = staffs.getJSONObject(i);
                            write_db_staff_list.add(new StaffWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    staff_factory.downloadStaff(staffJson, logItem);
                                }
                            }));
                        }

                        for(int i = 0; i< staff_projects.length(); i++)
                        {
                            final JSONObject staffJson = staff_projects.getJSONObject(i);
                            write_db_staff_list.add(new StaffWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    staff_factory.downloadStaffProject(staffJson, logItem);
                                }
                            }));
                        }

                        for(int i = 0; i< staff_teams.length(); i++)
                        {
                            final JSONObject staffJson = staff_teams.getJSONObject(i);
                            write_db_staff_list.add(new StaffWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    staff_factory.downloadStaffTeam(staffJson, logItem);
                                }
                            }));
                        }

                        for(int i=0;i<saleorders.length();i++){
                            final JSONObject salesOrderJson = saleorders.getJSONObject(i);
                            write_db_salesorder_list.add(new SalesOrderWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    sales_order_factory.downloadSalesOrder(salesOrderJson, logItem);
                                }
                            }));
                        }

                        for(int i=0;i<saleorder_team.length();i++){
                            final JSONObject salesOrderTeamJson = saleorder_team.getJSONObject(i);
                            write_db_salesorder_list.add(new SalesOrderWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    sales_order_factory.downloadSalesOrderTeam(salesOrderTeamJson, logItem);
                                }
                            }));
                        }

                        for(int i=0;i<staffs.length();i++){
                            String staff_unique = staffs.getJSONObject(i).getString("uniquenum");
                            update_finger_list.add(new UpdateFinger(staff_unique));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showSyncStatus(false);
                    }

                    return true;
                }else{
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean downloadSuccess) {
            if(downloadSuccess){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syncUser();
                    }
                });
            }else{
                showSyncStatus(false);
            }
        }
    }

    private void syncPendingStaff(){
        if(upload_staff_list.size() > 0){
            upload_staff_list.get(0).execute();
        }else{
            syncPendingTimelog();
        }
    }

    private void syncPendingTimelog(){
        if(upload_timelog_list.size() > 0){
            upload_timelog_list.get(0).execute();
        }else{
            new SyncDown().execute();
        }
    }

    private void syncUser(){
        if(write_db_user_list.size() > 0){
            user_factory.openRepo();
            write_db_user_list.get(0).execute();
        }else{
            syncCompany();
        }
    }

    private void syncCompany(){
        if(write_db_company_list.size() > 0){
            company_factory.openRepo();
            write_db_company_list.get(0).execute();
        }else{
            syncProject();
        }
    }

    private void syncProject(){
        if(write_db_project_list.size() > 0){
            project_factory.openRepo();
            write_db_project_list.get(0).execute();
        }else{
            syncStaff();
        }
    }

    private void syncStaff(){
        if(write_db_staff_list.size() > 0){
            staff_factory.openRepo();
            write_db_staff_list.get(0).execute();
        }else{
            syncSalesOrder();
        }
    }

    private void syncSalesOrder(){
        if(write_db_salesorder_list.size() > 0){
            sales_order_factory.openRepo();
            write_db_salesorder_list.get(0).execute();
        }else{
            updateFingers();
        }
    }

    private void updateFingers(){
        if(update_finger_list.size()>0){
            update_finger_list.get(0).execute();
        }else{
            showSyncStatus(true);
        }
    }

    private class UploadStaff extends AsyncTask<Void, Void, Boolean> {
        Staff staff;
        LogItem log_item;

        public UploadStaff(Staff staff, LogItem log_item){
            this.staff = staff;
            this.log_item = log_item;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HashMap<String,String> detail = new HashMap<>();
                detail.put("cfsqlfilename", CFSQLFILENAME);
                detail.put("masterfn", MASTERFN);
                detail.put("companyfn", COMPANYFN);
                detail.put("userloginid", USERLOGINID);
                detail.put("staff_id", staff.getStaff_num());
                detail.put("staff_unique", staff.getUniquenumPri());
                detail.put("sync_unique", log_item.getLogUnique());
                detail.put("date_time_sync", DateUtility.getDateString(log_item.getLogDate(), "yyyy-MM-dd HH:mm:ss"));
                detail.put("date_time_update", DateUtility.getDateString(staff.getDate_update() != null ? staff.getDate_update() : log_item.getLogDate(), "yyyy-MM-dd HH:mm:ss"));
                detail.put("fingerprint_image1", (staff.getFingerprint_image1() != null && staff.getFingerprint_image1().length > 0 ? Base64.encodeToString(staff.getFingerprint_image1(), Base64.DEFAULT) : ""));
                detail.put("fingerprint_image2", (staff.getFingerprint_image2() != null && staff.getFingerprint_image2().length > 0 ? Base64.encodeToString(staff.getFingerprint_image2(), Base64.DEFAULT) : ""));
                detail.put("fingerprint_image3", (staff.getFingerprint_image3() != null && staff.getFingerprint_image3().length > 0 ? Base64.encodeToString(staff.getFingerprint_image3(), Base64.DEFAULT) : ""));
                detail.put("fingerprint_image4", (staff.getFingerprint_image4() != null && staff.getFingerprint_image4().length > 0 ? Base64.encodeToString(staff.getFingerprint_image4(), Base64.DEFAULT) : ""));
                detail.put("fingerprint_image5", (staff.getFingerprint_image5() != null && staff.getFingerprint_image5().length > 0 ? Base64.encodeToString(staff.getFingerprint_image5(), Base64.DEFAULT) : ""));
                detail.put("photo1", (staff.getPhoto1() != null && staff.getPhoto1().length > 0 ? Base64.encodeToString(staff.getPhoto1(), Base64.DEFAULT) : ""));

                String param = HttpUtility.hashMapToUrl(detail);

                JSONObject syncResultJSON = HttpUtility.requestJSON("staff_sync_up", param);
                if(syncResultJSON != null){
                    staff_factory.setStaffSync(staff.getUniquenumPri(), staff.getDate_update(), log_item.getLogDate(), log_item.getLogUnique());
                    return true;
                }else{
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                upload_staff_que_num++;
                if(upload_staff_que_num<upload_staff_list.size()){
                    upload_staff_list.get(upload_staff_que_num).execute();
                }else if(upload_staff_que_num==upload_staff_list.size()){
                    if(upload_timelog_list.size()>0){
                        upload_timelog_list.get(0).execute();
                    }else{
                        showSyncStatus(true);
                    }
                }
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class UploadTimelog extends AsyncTask<Void, Void, Boolean> {
        TimeRecord time_record;
        LogItem log_item;

        public UploadTimelog(TimeRecord time_record, LogItem log_item){
            this.time_record = time_record;
            this.log_item = log_item;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HashMap<String,String> detail = new HashMap<>();
                detail.put("cfsqlfilename", CFSQLFILENAME);
                detail.put("masterfn", MASTERFN);
                detail.put("companyfn", COMPANYFN);
                detail.put("uniquenum_pri", time_record.getUniquenumPri());

                String param = HttpUtility.hashMapToUrl(detail);

                JSONObject timeLogGetJSON = HttpUtility.requestJSON("timelog_get", param);

                if(timeLogGetJSON != null && timeLogGetJSON.getBoolean("exist")){
                    if(DateUtility.getDateString(time_record.getDateTimeIn(), "yyyy-MM-dd HH:mm").equals(timeLogGetJSON.getString("date_in")) && timeLogGetJSON.getString("date_out").trim().equals("")){
                        return upload(time_record);
                    }else{
                        staff_factory.removeDateOut(time_record.getUniquenumPri());
                        return upload(staff_factory.logOut(time_record.getStaff(), time_record.getProject(), time_record.getDateTimeOut()));
                    }
                }else{
                    return upload(time_record);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                upload_timelog_que_num++;
                if(upload_timelog_que_num<upload_timelog_list.size()){
                    upload_timelog_list.get(upload_timelog_que_num).execute();
                }else if(upload_timelog_que_num==upload_timelog_list.size()){
                    new SyncDown().execute();
                }
            }else{
                showSyncStatus(false);
            }
        }

        public boolean upload(TimeRecord newTimeRecord){
            Boolean res;

            try {
                HashMap<String,String> detail = new HashMap<>();
                detail.put("cfsqlfilename", CFSQLFILENAME);
                detail.put("masterfn", MASTERFN);
                detail.put("companyfn", COMPANYFN);
                detail.put("userloginid", USERLOGINID);
                detail.put("userloginuniq", USERLOGINUNIQ);
                detail.put("uniquenum_pri", newTimeRecord.getUniquenumPri());
                detail.put("staff_unique", newTimeRecord.getStaff().getUniquenumPri());
                detail.put("project_unique", newTimeRecord.getProject()!=null?newTimeRecord.getProject().getUniquenumPri():"");
                detail.put("sync_unique", log_item.getLogUnique());
                detail.put("date_time_post", DateUtility.getDateString(newTimeRecord.getDateTimePost(), "yyyy-MM-dd HH:mm:ss"));
                detail.put("date_time_sync", DateUtility.getDateString(log_item.getLogDate(), "yyyy-MM-dd HH:mm:ss"));
                detail.put("log_type", newTimeRecord.getLogType());
                detail.put("date_time_in", newTimeRecord.getDateTimeIn() != null ? DateUtility.getDateString(newTimeRecord.getDateTimeIn(), "yyyy-MM-dd HH:mm:ss") : "");
                detail.put("date_time_out", newTimeRecord.getDateTimeOut() != null ? DateUtility.getDateString(newTimeRecord.getDateTimeOut(), "yyyy-MM-dd HH:mm:ss") : "");
                detail.put("device_id_in", newTimeRecord.getDeviceIdIn() != null ? newTimeRecord.getDeviceIdIn() : "");
                detail.put("device_id_out", newTimeRecord.getDeviceIdOut() != null ? newTimeRecord.getDeviceIdOut() : "");
                detail.put("device_model_in", newTimeRecord.getDeviceModelIn() != null ? newTimeRecord.getDeviceModelIn() : "");
                detail.put("device_model_out", newTimeRecord.getDeviceModelOut() != null ? newTimeRecord.getDeviceModelOut() : "");
                detail.put("device_name_in", newTimeRecord.getDeviceNameIn() != null ? newTimeRecord.getDeviceNameIn() : "");
                detail.put("device_name_out", newTimeRecord.getDeviceNameOut() != null ? newTimeRecord.getDeviceNameOut() : "");
                detail.put("gps_location_in", newTimeRecord.getGPSLocationIn() != null ? newTimeRecord.getGPSLocationIn() : "");
                detail.put("gps_location_out", newTimeRecord.getGPSLocationOut() != null ? newTimeRecord.getGPSLocationOut() : "");
                detail.put("address_in", newTimeRecord.getAddressIn() != null ? newTimeRecord.getAddressIn() : "");
                detail.put("address_out", newTimeRecord.getAddressOut() != null ? newTimeRecord.getAddressOut() : "");

                String param = HttpUtility.hashMapToUrl(detail);

                JSONObject syncResultJSON = HttpUtility.requestJSON("timelog_sync_up", param);
                if(syncResultJSON != null){
                    staff_factory.setTimeLogSync(newTimeRecord.getUniquenumPri(), log_item.getLogDate(), log_item.getLogUnique());
                    res = true;
                }else{
                    res = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = false;
            }

            return res;
        }
    }

    private class UserWriteDB extends AsyncTask<Void, Void, Boolean> {
        private Runnable task;

        public UserWriteDB(Runnable task){
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                task.run();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                write_db_user_que_num++;
                if(write_db_user_que_num<write_db_user_list.size()){
                    write_db_user_list.get(write_db_user_que_num).execute();
                }else if(write_db_user_que_num==write_db_user_list.size()){
                    user_factory.closeRepo();
                    syncCompany();
                }
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class CompanyWriteDB extends AsyncTask<Void, Void, Boolean> {
        private Runnable task;

        public CompanyWriteDB(Runnable task){
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                task.run();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                write_db_company_que_num++;
                if(write_db_company_que_num<write_db_company_list.size()){
                    write_db_company_list.get(write_db_company_que_num).execute();
                }else if(write_db_company_que_num==write_db_company_list.size()){
                    company_factory.closeRepo();
                    syncProject();
                }
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class ProjectWriteDB extends AsyncTask<Void, Void, Boolean> {
        private Runnable task;

        public ProjectWriteDB(Runnable task){
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                task.run();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                write_db_project_que_num++;
                if(write_db_project_que_num<write_db_project_list.size()){
                    write_db_project_list.get(write_db_project_que_num).execute();
                }else if(write_db_project_que_num==write_db_project_list.size()){
                    project_factory.closeRepo();
                    syncStaff();
                }
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class StaffWriteDB extends AsyncTask<Void, Void, Boolean> {
        private Runnable task;

        public StaffWriteDB(Runnable task){
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                task.run();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                write_db_staff_que_num++;
                if(write_db_staff_que_num<write_db_staff_list.size()){
                    write_db_staff_list.get(write_db_staff_que_num).execute();
                }else if(write_db_staff_que_num==write_db_staff_list.size()){
                    staff_factory.closeRepo();
                    syncSalesOrder();
                }
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class SalesOrderWriteDB extends AsyncTask<Void, Void, Boolean> {
        private Runnable task;

        public SalesOrderWriteDB(Runnable task){
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                task.run();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                write_db_salesorder_que_num++;
                if(write_db_salesorder_que_num<write_db_salesorder_list.size()){
                    write_db_salesorder_list.get(write_db_salesorder_que_num).execute();
                }else if(write_db_salesorder_que_num==write_db_salesorder_list.size()){
                    sales_order_factory.closeRepo();
                    updateFingers();
                }
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class UpdateFinger extends AsyncTask<Void, Void, Boolean> {
        private String staff_unique;

        public UpdateFinger(String staff_unique){
            this.staff_unique = staff_unique;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Staff staff = staff_factory.getStaff(staff_unique);

                BiometricUtility.updateFinger(staff.getFingerprint_image1(), staff.getUniquenumPri() + "_1");
                BiometricUtility.updateFinger(staff.getFingerprint_image2(), staff.getUniquenumPri() + "_2");
                BiometricUtility.updateFinger(staff.getFingerprint_image3(), staff.getUniquenumPri() + "_3");
                BiometricUtility.updateFinger(staff.getFingerprint_image4(), staff.getUniquenumPri() + "_4");
                BiometricUtility.updateFinger(staff.getFingerprint_image5(), staff.getUniquenumPri() + "_5");

                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean writeSuccess) {
            if(writeSuccess){
                updateProgress();
                update_finger_que_num++;
                if(update_finger_que_num<update_finger_list.size()){
                    update_finger_list.get(update_finger_que_num).execute();
                }else if(update_finger_que_num==update_finger_list.size()){
                    showSyncStatus(true);
                }
            }else{
                showSyncStatus(false);
            }
        }
    }
}
