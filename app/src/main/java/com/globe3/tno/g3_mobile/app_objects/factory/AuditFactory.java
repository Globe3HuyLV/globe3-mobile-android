package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.model.entities.auditlog;
import com.globe3.tno.g3_mobile.util.Uniquenum;
import com.globe3.tno.g3_mobile.app_objects.LogItem;

import java.util.ArrayList;
import java.util.Date;

import com.globe3.tno.g3_mobile.model.AuditlogRepo;
import com.globe3.tno.g3_mobile.view_objects.RecentActivity;

import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;

public class AuditFactory {
    AuditlogRepo auditlog_repo;
    StaffFactory staff_factory;

    public AuditFactory(Context context) {
        auditlog_repo = new AuditlogRepo(context);
        staff_factory = new StaffFactory(context);
    }

    public LogItem Log(String tableUsage){
        Date now = new Date();
        auditlog_repo.open();
        auditlog auditlog = new auditlog();

        auditlog.uniquenum_pri = Uniquenum.Generate();
        auditlog.date_post = now;
        auditlog.date_submit = now;
        auditlog.tag_table_usage = tableUsage;
        auditlog.active_yn = "y";
        auditlog.date_post = new Date();
        auditlog.masterfn = MASTERFN;
        auditlog.companyfn = COMPANYFN;
        auditlog.userid = USERLOGINID;

        auditlog_repo.create_auditlog(auditlog);

        auditlog_repo.close();

        LogItem logItem = new LogItem();
        logItem.setTableUsage(auditlog.tag_table_usage);
        logItem.setLogUnique(auditlog.uniquenum_pri);
        logItem.setLogDate(auditlog.date_post);

        return logItem;
    }

    public LogItem Log(String tableUsage, String staffUnique){
        Date now = new Date();
        auditlog_repo.open();
        auditlog auditlog = new auditlog();

        auditlog.uniquenum_pri = Uniquenum.Generate();
        auditlog.uniquenum_sec = staffUnique;
        auditlog.date_post = now;
        auditlog.date_submit = now;
        auditlog.tag_table_usage = tableUsage;
        auditlog.active_yn = "y";
        auditlog.date_post = new Date();
        auditlog.masterfn = MASTERFN;
        auditlog.companyfn = COMPANYFN;
        auditlog.userid = USERLOGINID;

        auditlog_repo.create_auditlog(auditlog);

        auditlog_repo.close();

        LogItem logItem = new LogItem();
        logItem.setTableUsage(auditlog.tag_table_usage);
        logItem.setLogUnique(auditlog.uniquenum_pri);
        logItem.setLogDate(auditlog.date_post);

        return logItem;
    }

    public Date getLastSync(String usage){
        auditlog auditlog;
        auditlog_repo.open();
        auditlog = auditlog_repo.get_auditlog_latest_sync(usage);
        auditlog_repo.close();
        if(auditlog != null){
            return auditlog.date_post;
        }else{
            return null;
        }
    }

    public ArrayList<RecentActivity> getRecentActivity(){
        ArrayList<RecentActivity> activity_list = new ArrayList<>();

        auditlog_repo.open();

        for(auditlog auditlog : auditlog_repo.get_staff_recent_activity()){
            RecentActivity recentActivity = new RecentActivity();
            Staff staff = staff_factory.getStaff(auditlog.uniquenum_sec);

            recentActivity.setIcon(R.drawable.ic_access_time_black_36dp);

        }

        auditlog_repo.close();

        return activity_list;
    }

    public void deleteEverything(){
        auditlog_repo.open();

        auditlog_repo.database_truncate();

        auditlog_repo.close();
    }
}
