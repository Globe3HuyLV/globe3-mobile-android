package com.globe3.tno.g3_mobile.async;

import android.os.AsyncTask;

import android.util.Base64;

import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;

import org.json.JSONObject;

import java.util.HashMap;

import static com.globe3.tno.g3_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;

public class StaffSingleUploadTask extends AsyncTask<Void, Void, Boolean> {
    StaffFactory staff_factory;
    Staff staff;
    LogItem log_item;

    public StaffSingleUploadTask(StaffFactory staffFactory, Staff staff, LogItem logItem){
        this.staff_factory = staffFactory;
        this.staff = staff;
        this.log_item = logItem;
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
}
