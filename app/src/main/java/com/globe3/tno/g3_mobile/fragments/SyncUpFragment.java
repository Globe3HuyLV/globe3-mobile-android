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

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.activities.DashboardActivity;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.TimeRecord;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class SyncUpFragment extends DialogFragment {
    Activity parent_activity;

    AuditFactory audit_factory;
    StaffFactory staff_factory;

    ImageView iv_icon_sync_up;
    TextView tv_sync_up_progress;
    TextView tv_sync_up_desc;
    LinearLayout ll_done;
    TextView tv_done;

    int sync_total = 0;
    int sync_progress = 0;

    ArrayList<TimeRecord> pending_timesheet;
    ArrayList<Staff> pending_staff;

    ArrayList<UploadStaff> upload_staff_list;
    ArrayList<UploadTimelog> upload_timelog_list;

    int upload_staff_que_num = 0;
    int upload_timelog_que_num = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        parent_activity = getActivity();

        audit_factory = new AuditFactory(parent_activity);
        staff_factory = new StaffFactory(parent_activity);

        pending_timesheet = new ArrayList<>();
        pending_staff = new ArrayList<>();

        upload_staff_list = new ArrayList<>();
        upload_timelog_list = new ArrayList<>();

        View syncUpFragment = inflater.inflate(R.layout.fragment_sync_up, viewGroup, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        iv_icon_sync_up = (ImageView) syncUpFragment.findViewById(R.id.iv_icon_sync_up);
        tv_sync_up_progress = (TextView) syncUpFragment.findViewById(R.id.tv_sync_up_progress);
        tv_sync_up_desc = (TextView) syncUpFragment.findViewById(R.id.tv_sync_up_desc);
        ll_done = (LinearLayout) syncUpFragment.findViewById(R.id.ll_done);
        tv_done = (TextView) syncUpFragment.findViewById(R.id.tv_done);

        iv_icon_sync_up.setAnimation(AnimationUtils.loadAnimation(parent_activity, R.anim.animate_rotate_counter_clockwise));
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        new SyncUp().execute();

        return syncUpFragment;
    }

    private void updateProgress(){
        sync_progress++;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_sync_up_progress.setText(String.valueOf((int)Math.floor(((float) sync_progress /(float) sync_total)*100)) + "%");
            }
        });
    }

    private void showSyncStatus(final boolean syncSuccess){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(syncSuccess){
                    tv_sync_up_progress.setTextColor(ContextCompat.getColor(parent_activity, R.color.colorBlackLight));
                    tv_sync_up_desc.setText(getString(R.string.msg_sync_up_complete));
                    tv_sync_up_desc.setVisibility(View.VISIBLE);
                    tv_done.setText(getString(R.string.label_done));
                }else{
                    tv_sync_up_progress.setText(getString(R.string.msg_sync_failed));
                    tv_sync_up_progress.setTextColor(ContextCompat.getColor(parent_activity, R.color.colorFailed));
                    tv_done.setText(getString(R.string.label_cancel));
                    tv_sync_up_desc.setVisibility(View.GONE);
                }
                iv_icon_sync_up.setAnimation(null);
                ll_done.setVisibility(View.VISIBLE);
                ((DashboardActivity)getActivity()).updateDashboard();
            }
        });
    }

    private class SyncUp extends AsyncTask<Void, Void, Boolean> {
        LogItem logItem;

        @Override
        protected Boolean doInBackground(Void... params) {
            logItem = audit_factory.Log(TagTableUsage.DATA_SYNC_UP);

            pending_staff = staff_factory.getPendingtaffs();
            pending_timesheet = staff_factory.getPendingTimelogs();

            try {
                sync_total = pending_timesheet.size()+pending_staff.size();

                for(Staff staff : pending_staff){
                    upload_staff_list.add(new UploadStaff(staff, logItem));
                }

                for(TimeRecord timeRecord : pending_timesheet){
                    upload_timelog_list.add(new UploadTimelog(timeRecord, logItem));
                }

                return true;
            } catch (Exception e) {
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
                        if(upload_staff_list.size()>0){
                            upload_staff_list.get(0).execute();
                        }else if(upload_timelog_list.size()>0){
                            upload_timelog_list.get(0).execute();
                        }else{
                            showSyncStatus(true);
                        }
                    }
                });
            }else{
                showSyncStatus(false);
            }
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
                    showSyncStatus(true);
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
}
