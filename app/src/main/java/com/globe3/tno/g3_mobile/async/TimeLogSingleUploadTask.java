package com.globe3.tno.g3_mobile.async;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.globe3.tno.g3_mobile.app_objects.DailyTime;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;

import org.json.JSONObject;

import java.util.HashMap;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;

public class TimeLogSingleUploadTask extends AsyncTask<Void, Void, Boolean> {
    StaffFactory staffFactory;

    DailyTime dailyTime;
    LogItem logItem;

    public TimeLogSingleUploadTask(StaffFactory staffFactory, DailyTime dailyTime, LogItem logItem){
        this.staffFactory = staffFactory;
        this.dailyTime = dailyTime;
        this.logItem = logItem;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            HashMap<String,String> detail = new HashMap<>();
            detail.put("cfsqlfilename", CFSQLFILENAME);
            detail.put("masterfn", MASTERFN);
            detail.put("companyfn", COMPANYFN);
            detail.put("uniquenum_pri", dailyTime.getUniquenum_pri());

            String param = HttpUtility.hashMapToUrl(detail);

            JSONObject timeLogGetJSON = HttpUtility.requestJSON("timelog_get", param);

            if(timeLogGetJSON != null && timeLogGetJSON.getBoolean("exist")){
                if(DateUtility.getDateString(dailyTime.getDateTimeIn(), "yyyy-MM-dd HH:mm").equals(timeLogGetJSON.getString("date_in")) && timeLogGetJSON.getString("date_out").trim().equals("")){
                    return upload(dailyTime);
                }else{
                    staffFactory.removeDateOut(dailyTime.getUniquenum_pri());
                    return upload(staffFactory.logOut(dailyTime.getStaff(), dailyTime.getProject(), dailyTime.getDateTimeOut()));
                }
            }else{
                return upload(dailyTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upload(DailyTime newDailyTime){
        Boolean res;

        try {
            HashMap<String,String> detail = new HashMap<>();
            detail.put("cfsqlfilename", CFSQLFILENAME);
            detail.put("masterfn", MASTERFN);
            detail.put("companyfn", COMPANYFN);
            detail.put("userloginid", USERLOGINID);
            detail.put("userloginuniq", USERLOGINUNIQ);
            detail.put("uniquenum_pri", newDailyTime.getUniquenum_pri());
            detail.put("staff_unique", newDailyTime.getStaff().getUniquenum());
            detail.put("project_unique", newDailyTime.getProject().getUniquenum());
            detail.put("sync_unique", logItem.getLogUnique());
            detail.put("date_time_post", DateUtility.getDateString(newDailyTime.getDateTimePost(), "yyyy-MM-dd HH:mm:ss"));
            detail.put("date_time_sync", DateUtility.getDateString(logItem.getLogDate(), "yyyy-MM-dd HH:mm:ss"));
            detail.put("log_type", newDailyTime.getLogType());
            detail.put("date_time_in", newDailyTime.getDateTimeIn() != null ? DateUtility.getDateString(newDailyTime.getDateTimeIn(), "yyyy-MM-dd HH:mm:ss") : "");
            detail.put("date_time_out", newDailyTime.getDateTimeOut() != null ? DateUtility.getDateString(newDailyTime.getDateTimeOut(), "yyyy-MM-dd HH:mm:ss") : "");
            detail.put("device_id_in", newDailyTime.getDeviceIdIn() != null ? newDailyTime.getDeviceIdIn() : "");
            detail.put("device_id_out", newDailyTime.getDeviceIdOut() != null ? newDailyTime.getDeviceIdOut() : "");
            detail.put("device_model_in", newDailyTime.getDeviceModelIn() != null ? newDailyTime.getDeviceModelIn() : "");
            detail.put("device_model_out", newDailyTime.getDeviceModelOut() != null ? newDailyTime.getDeviceModelOut() : "");
            detail.put("device_name_in", newDailyTime.getDeviceNameIn() != null ? newDailyTime.getDeviceNameIn() : "");
            detail.put("device_name_out", newDailyTime.getDeviceNameOut() != null ? newDailyTime.getDeviceNameOut() : "");
            detail.put("gps_location_in", newDailyTime.getGPSLocationIn() != null ? newDailyTime.getGPSLocationIn() : "");
            detail.put("gps_location_out", newDailyTime.getGPSLocationOut() != null ? newDailyTime.getGPSLocationOut() : "");
            detail.put("address_in", newDailyTime.getAddressIn() != null ? newDailyTime.getAddressIn() : "");
            detail.put("address_out", newDailyTime.getAddressOut() != null ? newDailyTime.getAddressOut() : "");

            String param = HttpUtility.hashMapToUrl(detail);

            JSONObject syncResultJSON = HttpUtility.requestJSON("timelog_sync_up", param);
            if(syncResultJSON != null){
                staffFactory.setTimeLogSync(newDailyTime.getUniquenum_pri(), logItem.getLogDate(), logItem.getLogUnique());
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
