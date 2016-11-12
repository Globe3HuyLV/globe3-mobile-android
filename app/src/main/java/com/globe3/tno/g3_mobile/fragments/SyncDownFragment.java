package com.globe3.tno.g3_mobile.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
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
import org.json.JSONObject;

public class SyncDownFragment extends DialogFragment {
    Context parentContext;

    ImageView iv_icon_sync_down;
    TextView tv_sync_down_progress;
    TextView tv_sync_down_desc;
    LinearLayout ll_done;
    TextView tv_done;

    int syncTotal = 0;
    int syncProgress = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View syncDownFragment = inflater.inflate(R.layout.fragment_sync_down, viewGroup, false);
        parentContext = syncDownFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        iv_icon_sync_down = (ImageView) syncDownFragment.findViewById(R.id.iv_icon_sync_down);
        tv_sync_down_progress = (TextView) syncDownFragment.findViewById(R.id.tv_sync_down_progress);
        tv_sync_down_desc = (TextView) syncDownFragment.findViewById(R.id.tv_sync_down_desc);
        ll_done = (LinearLayout) syncDownFragment.findViewById(R.id.ll_done);
        tv_done = (TextView) syncDownFragment.findViewById(R.id.tv_done);

        iv_icon_sync_down.setAnimation(AnimationUtils.loadAnimation(parentContext, R.anim.animate_rotate_counter_clockwise));
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        new SyncDown(getActivity()).execute();
        return syncDownFragment;
    }

    private void updateProgress(){
        syncProgress++;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_sync_down_progress.setText(String.valueOf((int)Math.floor(((float)syncProgress/(float)syncTotal)*100)) + "%");
            }
        });
    }

    public class SyncDown extends AsyncTask<Void, Void, Boolean> {
        AuditFactory auditFactory;
        UserFactory userFactory;
        CompanyFactory companyFactory;
        ProjectFactory projectFactory;
        StaffFactory staffFactory;

        public SyncDown(Activity activity){
            auditFactory = new AuditFactory(activity);
            userFactory = new UserFactory(activity);
            companyFactory = new CompanyFactory(activity);
            projectFactory = new ProjectFactory(activity);
            staffFactory = new StaffFactory(activity);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            LogItem logItem = auditFactory.Log(TagTableUsage.DATA_SYNC_DOWN);
            try {
                JSONObject userResultJSON = HttpUtility.requestJSON("usersync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject companyResultJSON = HttpUtility.requestJSON("entitysync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject projectResultJSON = HttpUtility.requestJSON("projectsync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject staffResultJSON = HttpUtility.requestJSON("staffsync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);
                JSONObject staffProjectResultJSON = HttpUtility.requestJSON("project_staff_assign_sync", "cfsqlfilename="+ Globals.CFSQLFILENAME+"&masterfn="+ Globals.MASTERFN);

                JSONArray users = userResultJSON.getJSONArray("items");
                JSONArray companies = companyResultJSON.getJSONArray("items");
                JSONArray projects = projectResultJSON.getJSONArray("items");
                JSONArray staffs = staffResultJSON.getJSONArray("items");
                JSONArray staffProjects = staffProjectResultJSON.getJSONArray("items");

                if(userResultJSON!=null&&companyResultJSON!=null&&projectResultJSON!=null&&staffResultJSON!=null&&staffProjectResultJSON!=null){
                    syncTotal = users.length()+companies.length()+projects.length()+staffs.length()+staffProjects.length();

                    for(int i=0;i<users.length();i++)
                    {
                        JSONObject userJson = users.getJSONObject(i);
                        userFactory.createUser(userJson, logItem);
                        updateProgress();
                    }

                    for(int i=0;i<companies.length();i++)
                    {
                        JSONObject companyJson = companies.getJSONObject(i);
                        companyFactory.createCompany(companyJson, logItem);
                        updateProgress();
                    }

                    for(int i=0;i<projects.length();i++)
                    {
                        JSONObject projectJson = projects.getJSONObject(i);
                        projectFactory.createProject(projectJson, logItem);
                        updateProgress();
                    }

                    for(int i=0;i<staffs.length();i++)
                    {
                        JSONObject staffJson = staffs.getJSONObject(i);
                        staffFactory.downloadStaff(staffJson, logItem);
                        updateProgress();
                    }

                    for(int i=0;i<staffProjects.length();i++)
                    {
                        JSONObject staffJson = staffProjects.getJSONObject(i);
                        staffFactory.downloadStaffProject(staffJson, logItem);
                        updateProgress();
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
        protected void onPostExecute(Boolean syncSuccess) {
            if(syncSuccess){
                tv_sync_down_progress.setTextColor(ContextCompat.getColor(parentContext, R.color.colorBlackLight));
                tv_sync_down_desc.setText(parentContext.getString(R.string.msg_sync_down_complete));
                tv_sync_down_desc.setVisibility(View.VISIBLE);
                tv_done.setText(parentContext.getString(R.string.label_done));
            }else{
                tv_sync_down_progress.setText(parentContext.getString(R.string.msg_sync_failed));
                tv_sync_down_progress.setTextColor(ContextCompat.getColor(parentContext, R.color.colorFailed));
                tv_done.setText(parentContext.getString(R.string.label_cancel));
                tv_sync_down_desc.setVisibility(View.GONE);
            }
            iv_icon_sync_down.setAnimation(null);
            ll_done.setVisibility(View.VISIBLE);
            ((DashboardActivity)getActivity()).showLastSync();
        }
    }
}
