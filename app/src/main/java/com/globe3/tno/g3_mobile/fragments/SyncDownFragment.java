package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
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

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;

public class SyncDownFragment extends DialogFragment {
    Context parent_context;

    AuditFactory auditFactory;
    UserFactory userFactory;
    CompanyFactory companyFactory;
    ProjectFactory projectFactory;
    StaffFactory staffFactory;

    ImageView iv_icon_sync_down;
    TextView tv_sync_down_progress;
    TextView tv_sync_down_desc;
    LinearLayout ll_done;
    TextView tv_done;

    int sync_total = 0;
    int sync_progress = 0;

    ArrayList<WriteDB> write_db_list;
    int write_db_que_num = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        auditFactory = new AuditFactory(getActivity());
        userFactory = new UserFactory(getActivity());
        companyFactory = new CompanyFactory(getActivity());
        projectFactory = new ProjectFactory(getActivity());
        staffFactory = new StaffFactory(getActivity());

        View syncDownFragment = inflater.inflate(R.layout.fragment_sync_down, viewGroup, false);
        parent_context = syncDownFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        iv_icon_sync_down = (ImageView) syncDownFragment.findViewById(R.id.iv_icon_sync_down);
        tv_sync_down_progress = (TextView) syncDownFragment.findViewById(R.id.tv_sync_down_progress);
        tv_sync_down_desc = (TextView) syncDownFragment.findViewById(R.id.tv_sync_down_desc);
        ll_done = (LinearLayout) syncDownFragment.findViewById(R.id.ll_done);
        tv_done = (TextView) syncDownFragment.findViewById(R.id.tv_done);

        iv_icon_sync_down.setAnimation(AnimationUtils.loadAnimation(parent_context, R.anim.animate_rotate_counter_clockwise));
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        write_db_list = new ArrayList<>();

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
                    tv_sync_down_progress.setTextColor(ContextCompat.getColor(parent_context, R.color.colorBlackLight));
                    tv_sync_down_desc.setText(parent_context.getString(R.string.msg_sync_down_complete));
                    tv_sync_down_desc.setVisibility(View.VISIBLE);
                    tv_done.setText(parent_context.getString(R.string.label_done));
                }else{
                    tv_sync_down_progress.setText(parent_context.getString(R.string.msg_sync_failed));
                    tv_sync_down_progress.setTextColor(ContextCompat.getColor(parent_context, R.color.colorFailed));
                    tv_done.setText(parent_context.getString(R.string.label_cancel));
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
            logItem = auditFactory.Log(TagTableUsage.DATA_SYNC_DOWN);
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

                    auditFactory.journalOff();

                    userFactory.deleteAll();
                    companyFactory.deleteAll();
                    projectFactory.deleteAll();
                    staffFactory.deleteAll();
                    staffFactory.deleteStaffProject();

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
                        sync_total = users.length()+companies.length()+projects.length()+staffs.length()+staffProjects.length();

                        try {
                            for(int i=0;i<users.length();i++)
                            {
                                final JSONObject userJson = users.getJSONObject(i);
                                write_db_list.add(new WriteDB(new Runnable() {
                                    @Override
                                    public void run() {
                                        userFactory.createUser(userJson, logItem);
                                    }
                                }));
                            }

                            for(int i=0;i<companies.length();i++)
                            {
                                final JSONObject companyJson = companies.getJSONObject(i);
                                write_db_list.add(new WriteDB(new Runnable() {
                                    @Override
                                    public void run() {
                                        companyFactory.createCompany(companyJson, logItem);
                                    }
                                }));
                            }

                            for(int i=0;i<projects.length();i++)
                            {
                                final JSONObject projectJson = projects.getJSONObject(i);
                                write_db_list.add(new WriteDB(new Runnable() {
                                    @Override
                                    public void run() {
                                        projectFactory.createProject(projectJson, logItem);
                                    }
                                }));

                            }

                            for(int i=0;i<staffs.length();i++)
                            {
                                final JSONObject staffJson = staffs.getJSONObject(i);
                                write_db_list.add(new WriteDB(new Runnable() {
                                    @Override
                                    public void run() {
                                        staffFactory.downloadStaff(staffJson, logItem);
                                    }
                                }));
                            }

                            for(int i=0;i<staffProjects.length();i++)
                            {
                                final JSONObject staffJson = staffProjects.getJSONObject(i);
                                write_db_list.add(new WriteDB(new Runnable() {
                                    @Override
                                    public void run() {
                                        staffFactory.downloadStaffProject(staffJson, logItem);
                                    }
                                }));
                            }

                            write_db_list.get(0).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showSyncStatus(false);
                        }
                    }
                });
            }else{
                showSyncStatus(false);
            }
        }
    }

    private class WriteDB extends AsyncTask<Void, Void, Boolean> {
        private Runnable task;

        public WriteDB(Runnable task){
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
                write_db_que_num++;
                if(write_db_que_num<write_db_list.size()){
                    write_db_list.get(write_db_que_num).execute();
                }else if(write_db_que_num==write_db_list.size()){
                    showSyncStatus(true);
                }
            }else{
                showSyncStatus(false);
            }

        }
    }
}
