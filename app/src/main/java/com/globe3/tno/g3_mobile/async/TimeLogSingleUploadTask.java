package com.globe3.tno.g3_mobile.async;

import android.os.AsyncTask;

import com.globe3.tno.g3_mobile.app_objects.TimeRecord;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;

import org.json.JSONObject;

import java.util.HashMap;

import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class TimeLogSingleUploadTask extends AsyncTask<Void, Void, Boolean> {
    StaffFactory staff_factory;

    TimeRecord time_record;
    LogItem log_item;

    public TimeLogSingleUploadTask(StaffFactory staffFactory, TimeRecord timeRecord, LogItem logItem){
        this.staff_factory = staffFactory;
        this.time_record = timeRecord;
        this.log_item = logItem;
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
            detail.put("project_unique", newTimeRecord.getProject().getUniquenumPri());
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
