package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;

import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.StaffAction;
import com.globe3.tno.g3_mobile.model.BaseRepo;
import com.globe3.tno.g3_mobile.model.entities.auditlog;
import com.globe3.tno.g3_mobile.util.Uniquenum;
import com.globe3.tno.g3_mobile.app_objects.LogItem;

import java.util.ArrayList;
import java.util.Date;

import com.globe3.tno.g3_mobile.model.AuditLogRepo;

import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;

public class AuditFactory {
    AuditLogRepo auditlog_repo;

    public AuditFactory(Context context) {
        auditlog_repo = new AuditLogRepo(context);
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

    public ArrayList<StaffAction> getRecentActivity(){
        ArrayList<StaffAction> activity_list = new ArrayList<>();

        auditlog_repo.open();

        for(auditlog auditlog : auditlog_repo.get_staff_recent_activity()){
            StaffAction staffAction = new StaffAction();

            staffAction.setStaff(new Staff());
            staffAction.getStaff().setUniquenumPri(auditlog.uniquenum_sec);
            staffAction.setActionDate(auditlog.date_post);
            staffAction.setActionType(auditlog.tag_table_usage);

            activity_list.add(staffAction);
        }

        auditlog_repo.close();

        return activity_list;
    }
}
