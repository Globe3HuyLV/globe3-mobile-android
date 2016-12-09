package com.globe3.tno.g3_mobile.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
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
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.ProjectFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.UserFactory;
import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.factory.CompanyFactory;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SyncDownFragment extends DialogFragment {
    Activity parent_activity;

    AuditFactory audit_factory;
    UserFactory user_factory;
    CompanyFactory company_factory;
    ProjectFactory project_factory;
    StaffFactory staff_factory;

    ImageView iv_icon_sync_down;
    TextView tv_sync_down_progress;
    TextView tv_sync_down_desc;
    LinearLayout ll_done;
    TextView tv_done;

    int sync_total = 0;
    int sync_progress = 0;

    ArrayList<UserWriteDB> write_db_user_list;
    ArrayList<CompanyWriteDB> write_db_company_list;
    ArrayList<ProjectWriteDB> write_db_project_list;
    ArrayList<StaffWriteDB> write_db_staff_list;

    int write_db_user_que_num = 0;
    int write_db_company_que_num = 0;
    int write_db_project_que_num = 0;
    int write_db_staff_que_num = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        parent_activity = getActivity();

        audit_factory = new AuditFactory(parent_activity);
        user_factory = new UserFactory(parent_activity);
        company_factory = new CompanyFactory(parent_activity);
        project_factory = new ProjectFactory(parent_activity);
        staff_factory = new StaffFactory(getActivity());

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

        write_db_user_list = new ArrayList<>();
        write_db_company_list = new ArrayList<>();
        write_db_project_list = new ArrayList<>();
        write_db_staff_list = new ArrayList<>();

        new SyncDown().execute();

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
                ((DashboardActivity)getActivity()).showLastSync();
            }
        });
    }

    private class SyncDown extends AsyncTask<Void, Void, Boolean> {
        LogItem logItem;
        JSONArray users;
        JSONArray companies;
        JSONArray projects;
        JSONArray staffs;
        JSONArray staffProjects;

        @Override
        protected Boolean doInBackground(Void... params) {
            logItem = audit_factory.Log(TagTableUsage.DATA_SYNC_DOWN);
            try {
                JSONObject userResultJSON = HttpUtility.requestJSON("usersync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject companyResultJSON = HttpUtility.requestJSON("entitysync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject projectResultJSON = HttpUtility.requestJSON("projectsync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject staffResultJSON = HttpUtility.requestJSON("staffsync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject staffProjectResultJSON = HttpUtility.requestJSON("project_staff_assign_sync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);

                users = userResultJSON.getJSONArray("items");
                companies = companyResultJSON.getJSONArray("items");
                projects = projectResultJSON.getJSONArray("items");
                staffs = staffResultJSON.getJSONArray("items");
                staffProjects = staffProjectResultJSON.getJSONArray("items");
                if(userResultJSON!=null&&companyResultJSON!=null&&projectResultJSON!=null&&staffResultJSON!=null&&staffProjectResultJSON!=null){

                    user_factory.deleteAll();
                    company_factory.deleteAll();
                    project_factory.deleteAll();
                    staff_factory.deleteAll();
                    staff_factory.deleteAllStaffProject();

                    sync_total = users.length()+companies.length()+projects.length()+staffs.length()+staffProjects.length();

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

                        for(int i=0;i<staffProjects.length();i++)
                        {
                            final JSONObject staffJson = staffProjects.getJSONObject(i);
                            write_db_staff_list.add(new StaffWriteDB(new Runnable() {
                                @Override
                                public void run() {
                                    staff_factory.downloadStaffProject(staffJson, logItem);
                                }
                            }));
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
            showSyncStatus(true);
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
                    showSyncStatus(true);
                }
            }else{
                showSyncStatus(false);
            }
        }
    }
}
