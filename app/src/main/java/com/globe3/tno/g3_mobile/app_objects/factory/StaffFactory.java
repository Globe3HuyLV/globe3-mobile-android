package com.globe3.tno.g3_mobile.app_objects.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.app_objects.SalesOrder;
import com.globe3.tno.g3_mobile.app_objects.StaffTeam;
import com.globe3.tno.g3_mobile.model.TeamRepo;
import com.globe3.tno.g3_mobile.model.entities.team;
import com.globe3.tno.g3_mobile.util.BiometricUtility;
import com.globe3.tno.g3_mobile.util.Uniquenum;
import com.globe3.tno.g3_mobile.app_objects.TimeRecord;
import com.globe3.tno.g3_mobile.app_objects.LocationHistory;
import com.globe3.tno.g3_mobile.app_objects.LogItem;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.TimeLog;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.model.DailytimeRepo;
import com.globe3.tno.g3_mobile.model.ScanimageRepo;
import com.globe3.tno.g3_mobile.model.StaffdataRepo;
import com.globe3.tno.g3_mobile.model.TabledataRepo;
import com.globe3.tno.g3_mobile.model.entities.dailytime;
import com.globe3.tno.g3_mobile.model.entities.scanimage;
import com.globe3.tno.g3_mobile.model.entities.staffdata;
import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.FileUtility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_DATA_DIR;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_IMAGE_DIR;
import static com.globe3.tno.g3_mobile.constants.TagTableUsage.SALES_ORDER_TEAM;
import static com.globe3.tno.g3_mobile.constants.TagTableUsage.STAFF_PROJECT;
import static com.globe3.tno.g3_mobile.constants.TagTableUsage.STAFF_TEAM;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_ID;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_MODEL;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.MASTERFN;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.GPS_LOCATION;

public class StaffFactory {
    private Context context;
    ScanimageRepo scanimage_repo;
    DailytimeRepo dailytime_repo;
    StaffdataRepo staffdata_repo;
    TabledataRepo tabledata_repo;
    TeamRepo team_repo;

    public StaffFactory(Context context) {
        this.context = context;
        staffdata_repo = new StaffdataRepo(context);
        scanimage_repo = new ScanimageRepo(context);
        dailytime_repo = new DailytimeRepo(context);
        tabledata_repo = new TabledataRepo(context);
        team_repo = new TeamRepo(context);
    }

    public void openRepo(){
        staffdata_repo.open();
        scanimage_repo = new ScanimageRepo(staffdata_repo);
        dailytime_repo = new DailytimeRepo(staffdata_repo);
        tabledata_repo = new TabledataRepo(staffdata_repo);
        team_repo = new TeamRepo(staffdata_repo);
    }

    public void closeRepo(){
        staffdata_repo.close();
        staffdata_repo.close();
        dailytime_repo.close();
        tabledata_repo.close();
        team_repo.close();
    }

    public void downloadStaff(JSONObject staffJson, LogItem logItem) {

        staffdata staff = new staffdata();
        try {
            staff.masterfn = staffJson.getString("masterfn");
            staff.companyfn = staffJson.getString("companyfn");
            staff.uniquenum_pri = staffJson.getString("uniquenum");
            staff.date_post = DateUtility.getStringDate(staffJson.getString("date_post"));
            staff.staff_fullname = staffJson.getString("name");
            staff.staff_id = staffJson.getString("id");
            staff.job_title = staffJson.getString("job_title");
            staff.workpermit_num = staffJson.getString("work_permit_id");
            staff.sync_unique = logItem.getLogUnique();
            staff.date_sync = logItem.getLogDate();
            staff.date_birth = DateUtility.getStringDate(staffJson.getString("date_birth"));
            staff.nationality = staffJson.getString("nationality");
            staff.gender = staffJson.getString("gender");
            staff.race = staffJson.getString("race");
            staff.active_yn = staffJson.getString("active");

            staff.fingerprint_image1 = FileUtility.getImage(staffJson.getString("fingerprint_image1"));
            staff.fingerprint_image2 = FileUtility.getImage(staffJson.getString("fingerprint_image2"));
            staff.fingerprint_image3 = FileUtility.getImage(staffJson.getString("fingerprint_image3"));
            staff.fingerprint_image4 = FileUtility.getImage(staffJson.getString("fingerprint_image4"));
            staff.fingerprint_image5 = FileUtility.getImage(staffJson.getString("fingerprint_image5"));

            if (!staffJson.getString("photo1").equals("")) {
                byte[] photo = FileUtility.getImage(staffJson.getString("photo1"));

                Bitmap staffPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);

                int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

                Matrix matrix = new Matrix();

                if (staffPhoto.getWidth() > staffPhoto.getHeight()) {
                    matrix.postRotate(90);
                }

                Bitmap newBitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize, matrix, true), 512, 512, false);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                staff.photo1 = stream.toByteArray();
            } else {
                staff.photo1 = null;
            }

            //BiometricUtility.updateFinger(staff.fingerprint_image1, staff.uniquenum_pri + "_1");
            //BiometricUtility.updateFinger(staff.fingerprint_image2, staff.uniquenum_pri + "_2");
            //BiometricUtility.updateFinger(staff.fingerprint_image3, staff.uniquenum_pri + "_3");
            //BiometricUtility.updateFinger(staff.fingerprint_image4, staff.uniquenum_pri + "_4");
            //BiometricUtility.updateFinger(staff.fingerprint_image5, staff.uniquenum_pri + "_5");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        staffdata_repo.create_staffdata(staff);
    }

    public void downloadTeam(JSONObject teamJson, LogItem logItem){
        team staff_team = new team();

        try{
            staff_team.masterfn = teamJson.getString("masterfn");
            staff_team.companyfn = teamJson.getString("companyfn");
            staff_team.uniquenum_pri = teamJson.getString("uniquenum");
            staff_team.tag_table_usage = teamJson.getString("tag_table_usage");
            staff_team.uniquenum_sec = teamJson.getString("uniquenum_sec");
            staff_team.active_yn = teamJson.getString("tag_active_yn");
            staff_team.date_post = DateUtility.getStringDate(teamJson.getString("date_post"));
            staff_team.date_submit = DateUtility.getStringDate(teamJson.getString("date_submit"));
            staff_team.date_lastupdate = DateUtility.getStringDate(teamJson.getString("date_lastupdate"));
            staff_team.sync_unique = logItem.getLogUnique();
            staff_team.date_sync = logItem.getLogDate();
            staff_team.team_code = teamJson.getString("team_code");
            staff_team.team_name = teamJson.getString("team_desc");
            staff_team.team_unique = teamJson.getString("team_unique");

        }catch (Exception e){
            e.printStackTrace();
        }

        team_repo.create_team(staff_team);
    }

    public void downloadStaffTeam(JSONObject staffTeamJson, LogItem logItem) {

        tabledata staff_team = new tabledata();
        try {
            staff_team.masterfn = staffTeamJson.getString("masterfn");
            staff_team.companyfn = staffTeamJson.getString("companyfn");
            staff_team.uniquenum_pri = staffTeamJson.getString("uniquenum");

            staff_team.tag_table_usage = STAFF_TEAM;

            staff_team.nvar25_01 = staffTeamJson.getString("staff_code");
            staff_team.nvar25_02 = staffTeamJson.getString("staff_unique");
            staff_team.nvar100_01 = staffTeamJson.getString("staff_name");

            staff_team.nvar25_03 = staffTeamJson.getString("team_code");
            staff_team.nvar25_04 = staffTeamJson.getString("team_unique");
            staff_team.nvar100_03 = staffTeamJson.getString("team_name");

            staff_team.date_post = DateUtility.getStringDate(staffTeamJson.getString("date_post"));

            staff_team.sync_unique = logItem.getLogUnique();
            staff_team.date_sync = logItem.getLogDate();

            staff_team.tag_deleted_yn = "n";

            tabledata_repo.create_tabledata(staff_team);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void downloadStaffProject(JSONObject staffJson, LogItem logItem) {

        tabledata staff_project = new tabledata();
        try {
            staff_project.masterfn = staffJson.getString("masterfn");
            staff_project.companyfn = staffJson.getString("companyfn");
            staff_project.uniquenum_pri = staffJson.getString("uniquenum");

            staff_project.tag_table_usage = STAFF_PROJECT;

            staff_project.nvar25_01 = staffJson.getString("staff_code");
            staff_project.nvar25_02 = staffJson.getString("staff_unique");
            staff_project.nvar100_01 = staffJson.getString("staff_name");

            staff_project.nvar25_03 = staffJson.getString("project_code");
            staff_project.nvar25_04 = staffJson.getString("project_unique");
            staff_project.nvar100_03 = staffJson.getString("project_name");

            staff_project.date_post = DateUtility.getStringDate(staffJson.getString("date_post"));
            staff_project.date01 = DateUtility.getStringDate(staffJson.getString("date_start"));
            staff_project.date02 = DateUtility.getStringDate(staffJson.getString("date_end"));

            staff_project.sync_unique = logItem.getLogUnique();
            staff_project.date_sync = logItem.getLogDate();

            staff_project.tag_deleted_yn = "n";

            tabledata_repo.create_tabledata(staff_project);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteStaff(Staff staff) {
        staffdata_repo.open();
        staffdata_repo.delete_staffdata(convertToEntity(staff));
        staffdata_repo.close();
    }

    public void deleteAllTeam(){
        team_repo.open();
        team_repo.delete_team_all();
        team_repo.close();
    }

    public void deleteAll() {
        staffdata_repo.open();
        staffdata_repo.delete_staffdata_all();
        staffdata_repo.close();
    }
    public void deleteAllStaffProject(){
        tabledata_repo.open();
        tabledata_repo.delete_tabledata_all(STAFF_PROJECT);
        tabledata_repo.close();
    }
    public void deleteAllStaffTeam(){
        tabledata_repo.open();
        tabledata_repo.delete_tabledata_all(STAFF_TEAM);
        tabledata_repo.close();
    }

    public void updateStaff(Staff staff) {
        staffdata_repo.open();
        staffdata_repo.update_staffdata(convertToEntity(staff));
        staffdata_repo.close();
    }

    public ArrayList<Staff> getActiveStaffs() {
        staffdata_repo.open();

        ArrayList<Staff> staffs = new ArrayList<Staff>();

        for(staffdata staffdata : staffdata_repo.get_active_staffdatas()){
            staffs.add(convertEntity(staffdata));
        }

        staffdata_repo.close();
        return staffs;
    }

    public ArrayList<Staff> getPendingtaffs() {
        staffdata_repo.open();

        ArrayList<Staff> staffs = new ArrayList<Staff>();

        for(staffdata staffdata : staffdata_repo.get_updated_staffdatas()){
            staffs.add(convertEntity(staffdata));
        }

        staffdata_repo.close();
        return staffs;
    }

    public ArrayList<Staff> getActiveStaffs(String sortBy) {
        staffdata_repo.open();

        ArrayList<Staff> staffs = new ArrayList<Staff>();

        for(staffdata staffdata : staffdata_repo.get_active_staffdatas(sortBy)){
            staffs.add(convertEntity(staffdata));
        }

        staffdata_repo.close();
        return staffs;
    }

    public ArrayList<StaffTeam> getStaffTeams(String staffUnique){
        ArrayList<StaffTeam> staffTeams = new ArrayList<>();

        team_repo.open();
        tabledata_repo.open();

        for(tabledata tabledata : tabledata_repo.get_tabledatas("tag_table_usage='"+TagTableUsage.STAFF_TEAM+"' AND nvar25_02 = '"+staffUnique+"'")){
            team team = team_repo.get_team(tabledata.nvar25_04);
            StaffTeam staffTeam = new StaffTeam();
            staffTeam.setIdcode(team.idcode);
            staffTeam.setUniquenumPri(team.uniquenum_pri);
            staffTeam.setCode(team.team_code);
            staffTeam.setDesc(team.team_name);
            staffTeam.setActive(team.active_yn.equals("y"));

            staffTeams.add(staffTeam);
        };

        team_repo.close();
        tabledata_repo.close();

        return staffTeams;
    }

    public ArrayList<Staff> searchStaffs(String search) {
        staffdata_repo.open();

        ArrayList<Staff> staffs = new ArrayList<Staff>();

        for(staffdata staffdata : staffdata_repo.search_staffdatas(search)){
            staffs.add(convertEntity(staffdata));
        }

        staffdata_repo.close();
        return staffs;
    }

    public ArrayList<Staff> getReportStaffs(String pPostDate, String pUniquenum, String pSortBy) {
        staffdata_repo.open();

        ArrayList<Staff> staffs = new ArrayList<Staff>();

        for(staffdata staffdata : staffdata_repo.get_report_staffdatas(pPostDate, pUniquenum, pSortBy)){
            staffs.add(convertEntity(staffdata));
        }

        staffdata_repo.close();
        return staffs;
    }

    public ArrayList<Staff> getRegisteredStaffs() {
        staffdata_repo.open();

        ArrayList<Staff> staffs = new ArrayList<Staff>();

        for(staffdata staffdata : staffdata_repo.get_registered_staffdatas()){
            staffs.add(convertEntity(staffdata));
        }

        staffdata_repo.close();
        return staffs;
    }

    public Staff getStaff(String pUniquenum){
        staffdata_repo.open();
        Staff staff = convertEntity(staffdata_repo.get_staffdata(pUniquenum));
        staffdata_repo.close();
        return staff;
    }

    public void registerFingerprint(Staff staff){
        Date now = new Date();
        scanimage_repo.open();

        scanimage scanimage = new scanimage();
        scanimage.date_post = now;
        scanimage.date_submit = now;

        scanimage.uniquenum_pri = Uniquenum.Generate();
        scanimage.tag_table_usage = TagTableUsage.FINGERPRINT_REGISTER;
        scanimage.active_yn = "y";
        scanimage.userid_creator = USERLOGINID;
        scanimage.masterfn = MASTERFN;
        scanimage.companyfn = COMPANYFN;
        scanimage.staff_unique = staff.getUniquenumPri();
        scanimage.staff_id = staff.getStaff_num();
        scanimage.staff_fullname = staff.getStaff_desc();
        scanimage.photo1 = FileUtility.getFileBlob(GLOBE3_IMAGE_DIR + staff.getUniquenumPri() + ".jpeg");
        scanimage.fingerprint_image1 = FileUtility.getFileBlob(GLOBE3_DATA_DIR + staff.getUniquenumPri() + ".jpeg");
        scanimage.date_register = new Date();

        scanimage_repo.create_scanimage(scanimage);

        scanimage_repo.close();
    }

    public TimeLog getPreviousTimeLog(String staffUnique, String logType){
        TimeLog timeLog = new TimeLog();

        dailytime_repo.open();

        dailytime dailytime = dailytime_repo.get_previous_time_in(staffUnique, logType);
        if(dailytime != null){
            timeLog.setIdcode(dailytime.idcode);

            Date timeStamp = null;
            if(logType.equals(TagTableUsage.TIMELOG_IN)){
                timeStamp = dailytime.date_time_in;
            }else if(logType.equals(TagTableUsage.TIMELOG_OUT)){
                timeStamp = dailytime.date_time_out;
            }else if(logType.equals(TagTableUsage.LOCATION_CHECK)){
                timeStamp = dailytime.date_time_in;
            }

            timeLog.setDate(timeStamp);
            timeLog.setUniquenumPri(dailytime.uniquenum_pri);
            timeLog.setType(dailytime.type_in_out);
        }else{
            return null;
        }

        dailytime_repo.close();

        return timeLog;
    }

    public TimeRecord logTime(Staff staff, Project project, SalesOrder salesOrder, String tableUsage){
        TimeRecord timeRecord = new TimeRecord();
        dailytime_repo.open();

        if(tableUsage.equals(TagTableUsage.TIMELOG_IN)){
            dailytime dailytime = new dailytime();

            Date now = new Date();
            dailytime.date_post = now;
            dailytime.date_submit = now;
            dailytime.date_lastupdate = now;

            dailytime.date_time_in = now;
            dailytime.gps_location_in = GPS_LOCATION.getCoordinates();
            dailytime.device_id_in = DEVICE_ID;

            dailytime.nvar25_01 = DEVICE_MODEL;
            dailytime.nvar100_01 = DEVICE_NAME;
            dailytime.nvar100_03 = GPS_LOCATION.getFullAddress();

            dailytime.uniquenum_pri = Uniquenum.Generate();
            dailytime.tag_table_usage = TagTableUsage.TIMELOG;
            dailytime.tag_void_yn = "n";
            dailytime.tag_deleted_yn = "n";
            dailytime.userid_creator = USERLOGINID;
            dailytime.masterfn = MASTERFN;
            dailytime.companyfn = COMPANYFN;
            dailytime.staff_unique = staff.getUniquenumPri();
            dailytime.staff_id = staff.getStaff_num();
            dailytime.staff_fullname = staff.getStaff_desc();
            dailytime.type_in_out = tableUsage;

            dailytime.project_unique = project == null ? "" : project.getUniquenumPri();
            dailytime.project_code = project == null ? "" : project.getCode();
            dailytime.project_name = project == null ? "" : project.getDesc();

            dailytime.sale_order_unique = salesOrder == null ? "" : salesOrder.getUniquenumPri();
            dailytime.sale_order_code = salesOrder == null ? "" : salesOrder.getCode();
            dailytime.sale_order_name = salesOrder == null ? "" : salesOrder.getDesc();

            timeRecord = convertToDailyTime(staff, dailytime_repo.create_dailytime(dailytime));

        }else if(tableUsage.equals(TagTableUsage.TIMELOG_OUT)){

            dailytime dailytime = dailytime_repo.get_staff_time_in_dailytimes(staff.getUniquenumPri());

            if(dailytime == null){
                dailytime = new dailytime();

                Date now = new Date();
                dailytime.date_post = now;
                dailytime.date_submit = now;
                dailytime.date_lastupdate = now;

                dailytime.date_time_out = now;
                dailytime.gps_location_out = GPS_LOCATION.getCoordinates();
                dailytime.device_id_out = DEVICE_ID;

                dailytime.nvar25_02 = DEVICE_MODEL;
                dailytime.nvar100_02 = DEVICE_NAME;
                dailytime.nvar100_04 = GPS_LOCATION.getFullAddress();

                dailytime.uniquenum_pri = Uniquenum.Generate();
                dailytime.tag_table_usage = TagTableUsage.TIMELOG;
                dailytime.tag_void_yn = "n";
                dailytime.tag_deleted_yn = "n";
                dailytime.userid_creator = USERLOGINID;
                dailytime.masterfn = MASTERFN;
                dailytime.companyfn = COMPANYFN;
                dailytime.staff_unique = staff.getUniquenumPri();
                dailytime.staff_id = staff.getStaff_num();
                dailytime.staff_fullname = staff.getStaff_desc();
                dailytime.type_in_out = tableUsage;

                dailytime.project_unique = project == null ? "" : project.getUniquenumPri();
                dailytime.project_code = project == null ? "" : project.getCode();
                dailytime.project_name = project == null ? "" : project.getDesc();

                dailytime.sale_order_unique = salesOrder == null ? "" : salesOrder.getUniquenumPri();
                dailytime.sale_order_code = salesOrder == null ? "" : salesOrder.getCode();
                dailytime.sale_order_name = salesOrder == null ? "" : salesOrder.getDesc();

                timeRecord = convertToDailyTime(staff, dailytime_repo.create_dailytime(dailytime));
            }else{
                Date now = new Date();
                dailytime.date_lastupdate = now;

                dailytime.date_time_out = now;
                dailytime.gps_location_out = GPS_LOCATION.getCoordinates();
                dailytime.device_id_out = DEVICE_ID;

                dailytime.nvar25_02 = DEVICE_MODEL;
                dailytime.nvar100_02 = DEVICE_NAME;
                dailytime.nvar100_04 = GPS_LOCATION.getFullAddress();

                dailytime.type_in_out = TagTableUsage.TIMELOG_IN_OUT;
                dailytime_repo.update_dailytime(dailytime);

                timeRecord = convertToDailyTime(staff, dailytime);
            }
        }else if(tableUsage.equals(TagTableUsage.LOCATION_CHECK)) {
            dailytime dailytime = new dailytime();

            Date now = new Date();
            dailytime.date_post = now;
            dailytime.date_submit = now;
            dailytime.date_lastupdate = now;

            dailytime.date_time_in = now;
            dailytime.gps_location_in = GPS_LOCATION.getCoordinates();
            dailytime.device_id_in = DEVICE_ID;

            dailytime.nvar25_01 = DEVICE_MODEL;
            dailytime.nvar100_01 = DEVICE_NAME;
            dailytime.nvar100_03 = GPS_LOCATION.getFullAddress();

            dailytime.uniquenum_pri = Uniquenum.Generate();
            dailytime.tag_table_usage = TagTableUsage.LOCATION_CHECK;
            dailytime.tag_void_yn = "n";
            dailytime.tag_deleted_yn = "n";
            dailytime.userid_creator = USERLOGINID;
            dailytime.masterfn = MASTERFN;
            dailytime.companyfn = COMPANYFN;
            dailytime.staff_unique = staff.getUniquenumPri();
            dailytime.staff_id = staff.getStaff_num();
            dailytime.staff_fullname = staff.getStaff_desc();
            dailytime.type_in_out = tableUsage;

            dailytime.project_unique = project == null ? "" : project.getUniquenumPri();
            dailytime.project_code = project == null ? "" : project.getCode();
            dailytime.project_name = project == null ? "" : project.getDesc();

            dailytime.sale_order_unique = salesOrder == null ? "" : salesOrder.getUniquenumPri();
            dailytime.sale_order_code = salesOrder == null ? "" : salesOrder.getCode();
            dailytime.sale_order_name = salesOrder == null ? "" : salesOrder.getDesc();

            timeRecord = convertToDailyTime(staff, dailytime_repo.create_dailytime(dailytime));
        }

        dailytime_repo.close();

        return timeRecord;
    }

    public TimeRecord logOut(Staff staff, Project project, Date dateout){
        TimeRecord timeRecord = new TimeRecord();

        dailytime_repo.open();

        dailytime dailytime = new dailytime();

        dailytime.date_post = dateout;
        dailytime.date_submit = dateout;
        dailytime.date_lastupdate = dateout;

        dailytime.date_time_out = dateout;
        dailytime.gps_location_out = GPS_LOCATION.getCoordinates();
        dailytime.device_id_out = DEVICE_ID;

        dailytime.nvar25_02 = DEVICE_MODEL;
        dailytime.nvar100_02 = DEVICE_NAME;
        dailytime.nvar100_04 = GPS_LOCATION.getFullAddress();

        dailytime.uniquenum_pri = Uniquenum.Generate();
        dailytime.tag_table_usage = TagTableUsage.TIMELOG;
        dailytime.tag_void_yn = "n";
        dailytime.tag_deleted_yn = "n";
        dailytime.userid_creator = USERLOGINID;
        dailytime.masterfn = MASTERFN;
        dailytime.companyfn = COMPANYFN;
        dailytime.staff_unique = staff.getUniquenumPri();
        dailytime.staff_id = staff.getStaff_num();
        dailytime.staff_fullname = staff.getStaff_desc();
        dailytime.type_in_out = TagTableUsage.TIMELOG_OUT;
        dailytime.project_unique = project.getUniquenumPri();
        dailytime.project_code = project.getCode();
        dailytime.project_name = project.getDesc();

        timeRecord = convertToDailyTime(staff, dailytime_repo.create_dailytime(dailytime));

        dailytime_repo.close();

        return timeRecord;
    }

    public void setDailyTimeProject(TimeRecord timeRecord){

        dailytime_repo.open();

        dailytime dailytime = dailytime_repo.get_dailytime(timeRecord.getUniquenumPri());
        dailytime.project_unique = timeRecord.getProject().getUniquenumPri();
        dailytime.project_code = timeRecord.getProject().getCode();
        dailytime.project_name = timeRecord.getProject().getDesc();

        dailytime_repo.update_dailytime(dailytime);

        tabledata_repo.close();
    }

    public ArrayList<TimeLog> getStaffTimeLog(String pUniquenum, String pType, boolean pGetUnsync){
        ArrayList<TimeLog> timeLogs = new ArrayList<TimeLog>();
        dailytime_repo.open();
        staffdata_repo.open();

        for(dailytime dailytime : dailytime_repo.get_staff_dailytimes(pUniquenum, pType, pGetUnsync)){
            TimeLog timeLog = new TimeLog();

            timeLog.setIdcode(dailytime.idcode);
            timeLog.setUniquenumPri(dailytime.uniquenum_pri);
            timeLog.setStaff(convertEntity(staffdata_repo.get_staffdata(dailytime.staff_unique)));
            timeLog.setDate(dailytime.date_post);
            timeLog.setType(dailytime.type_in_out);

            timeLog.setProject(new Project());
            timeLog.getProject().setUniquenumPri(dailytime.project_unique);
            timeLog.getProject().setCode(dailytime.project_code);
            timeLog.getProject().setDesc(dailytime.project_name);

            timeLogs.add(timeLog);
        }

        staffdata_repo.close();
        dailytime_repo.close();

        return timeLogs;
    }

    public ArrayList<TimeLog> getTimelog(String pDatePost, String pUniquenum, String pSortBy){
        ArrayList<TimeLog> timeLogs = new ArrayList<TimeLog>();

        dailytime_repo.open();
        staffdata_repo.open();

        for(dailytime dailytime : dailytime_repo.get_report_dailytimes(pDatePost, pUniquenum, pSortBy)){
            TimeLog timeLog = new TimeLog();

            timeLog.setIdcode(dailytime.idcode);
            timeLog.setUniquenumPri(dailytime.uniquenum_pri);
            timeLog.setStaff(convertEntity(staffdata_repo.get_staffdata(dailytime.staff_unique)));
            timeLog.setDate(dailytime.date_post);
            timeLog.setType(dailytime.type_in_out);

            timeLog.setProject(new Project());
            timeLog.getProject().setUniquenumPri(dailytime.project_unique);
            timeLog.getProject().setCode(dailytime.project_code);
            timeLog.getProject().setDesc(dailytime.project_name);

            timeLogs.add(timeLog);
        }

        staffdata_repo.close();
        dailytime_repo.close();

        return timeLogs;
    }

    public ArrayList<TimeRecord> getTimesheetDay(Date reportDate){
        ArrayList<TimeRecord> timeLogs = new ArrayList<TimeRecord>();

        dailytime_repo.open();
        staffdata_repo.open();

        for(dailytime dailytime : dailytime_repo.get_timesheet_day_dailytimes(reportDate)){
            TimeRecord timeRecord = new TimeRecord();

            timeRecord.setIdcode(dailytime.idcode);
            timeRecord.setUniquenumPri(dailytime.uniquenum_pri);
            timeRecord.setStaff(convertEntity(staffdata_repo.get_staffdata(dailytime.staff_unique)));
            timeRecord.setDateTimePost(dailytime.date_post);

            timeRecord.setDateTimeIn(dailytime.date_time_in);
            timeRecord.setDateTimeOut(dailytime.date_time_out);

            timeRecord.setDeviceIdIn(dailytime.device_id_in);
            timeRecord.setDeviceIdOut(dailytime.device_id_out);

            timeRecord.setDeviceModelIn(dailytime.nvar25_01);
            timeRecord.setDeviceModelOut(dailytime.nvar25_02);

            timeRecord.setDeviceNameIn(dailytime.nvar100_01);
            timeRecord.setDeviceNameOut(dailytime.nvar100_02);

            timeRecord.setGPSLocationIn(dailytime.gps_location_in);
            timeRecord.setGPSLocationOut(dailytime.gps_location_out);

            timeRecord.setAddressIn(dailytime.nvar100_03);
            timeRecord.setAddressOut(dailytime.nvar100_04);

            timeRecord.setLogType(dailytime.type_in_out);

            timeRecord.setProject(new Project());
            timeRecord.getProject().setUniquenumPri(dailytime.project_unique);
            timeRecord.getProject().setCode(dailytime.project_code);
            timeRecord.getProject().setDesc(dailytime.project_name);

            timeRecord.setSynced(((dailytime.sync_unique != null && dailytime.sync_unique != "") && (dailytime.date_lastupdate.compareTo(dailytime.date_sync)!=1)));

            timeLogs.add(timeRecord);
        }

        staffdata_repo.close();
        dailytime_repo.close();

        return timeLogs;
    }

    public ArrayList<LocationHistory> getStaffLocationHistory(Date reportDate, String staffUnique){
        ArrayList<LocationHistory> locationHistory = new ArrayList<LocationHistory>();

        dailytime_repo.open();

        for(dailytime dailytime : dailytime_repo.get_staff_location_history(reportDate, staffUnique)){
            LocationHistory locationHistoryItem = new LocationHistory();

            locationHistoryItem.setLocationDesc(dailytime.nvar100_03);
            locationHistoryItem.setDateCheck(dailytime.date_post);

            locationHistory.add(locationHistoryItem);
        }

        dailytime_repo.close();

        return locationHistory;
    }

    public ArrayList<TimeRecord> getTimesheetStaff(Date reportDate, String staffUnique){
        ArrayList<TimeRecord> timeLogs = new ArrayList<TimeRecord>();

        dailytime_repo.open();
        staffdata_repo.open();

        for(dailytime dailytime : dailytime_repo.get_timesheet_staff_dailytimes(reportDate, staffUnique)){
            TimeRecord timeRecord = new TimeRecord();

            timeRecord.setIdcode(dailytime.idcode);
            timeRecord.setUniquenumPri(dailytime.uniquenum_pri);
            timeRecord.setStaff(convertEntity(staffdata_repo.get_staffdata(dailytime.staff_unique)));
            timeRecord.setDateTimePost(dailytime.date_post);

            timeRecord.setDateTimeIn(dailytime.date_time_in);
            timeRecord.setDateTimeOut(dailytime.date_time_out);

            timeRecord.setDeviceIdIn(dailytime.device_id_in);
            timeRecord.setDeviceIdOut(dailytime.device_id_out);

            timeRecord.setDeviceModelIn(dailytime.nvar25_01);
            timeRecord.setDeviceModelOut(dailytime.nvar25_02);

            timeRecord.setDeviceNameIn(dailytime.nvar100_01);
            timeRecord.setDeviceNameOut(dailytime.nvar100_02);

            timeRecord.setGPSLocationIn(dailytime.gps_location_in);
            timeRecord.setGPSLocationOut(dailytime.gps_location_out);

            timeRecord.setAddressIn(dailytime.nvar100_03);
            timeRecord.setAddressOut(dailytime.nvar100_04);

            timeRecord.setLogType(dailytime.type_in_out);

            timeRecord.setProject(new Project());
            timeRecord.getProject().setUniquenumPri(dailytime.project_unique);
            timeRecord.getProject().setCode(dailytime.project_code);
            timeRecord.getProject().setDesc(dailytime.project_name);

            timeLogs.add(timeRecord);
        }

        staffdata_repo.close();
        dailytime_repo.close();

        return timeLogs;
    }

    public void removeDateOut(String pUniquenum){
        dailytime_repo.open();
        dailytime dailytime = dailytime_repo.get_dailytime(pUniquenum);
        dailytime.type_in_out = TagTableUsage.TIMELOG_IN;
        dailytime.date_lastupdate = dailytime.date_sync;
        dailytime.date_time_out = null;
        dailytime.gps_location_out = "";
        dailytime.device_id_out = "";
        dailytime.nvar25_02 = "";
        dailytime.nvar100_02 = "";
        dailytime.nvar100_04 = "";
        dailytime_repo.update_dailytime(dailytime);
        dailytime_repo.close();
    }

    public void setTimeLogSync(String pUniquenum, Date dateSync, String syncUnique){
        dailytime_repo.open();
        dailytime dailytime = dailytime_repo.get_dailytime(pUniquenum);
        dailytime.date_sync = dateSync;
        dailytime.date_lastupdate = dateSync;
        dailytime.sync_unique = syncUnique;
        dailytime_repo.update_dailytime(dailytime);
        dailytime_repo.close();
    }

    public void setStaffSync(String pUniquenum, Date lastUpdate, Date dateSync, String syncUnique){
        staffdata_repo.open();
        staffdata staffdata = staffdata_repo.get_staffdata(pUniquenum);
        staffdata.date_sync = dateSync;
        staffdata.sync_unique = syncUnique;
        staffdata.date_lastupdate = lastUpdate;
        staffdata_repo.update_staffdata(staffdata);
        staffdata_repo.close();
    }

    public ArrayList<TimeRecord> getPendingTimelogs(){
        ArrayList<TimeRecord> timeLogs = new ArrayList<TimeRecord>();

        dailytime_repo.open();
        staffdata_repo.open();

        for(dailytime dailytime : dailytime_repo.get_new_dailytimes()){
            TimeRecord time_record = new TimeRecord();

            time_record.setIdcode(dailytime.idcode);
            time_record.setUniquenumPri(dailytime.uniquenum_pri);
            time_record.setStaff(convertEntity(staffdata_repo.get_staffdata(dailytime.staff_unique)));
            time_record.setDateTimePost(dailytime.date_post);

            time_record.setDateTimeIn(dailytime.date_time_in);
            time_record.setDateTimeOut(dailytime.date_time_out);

            time_record.setDeviceIdIn(dailytime.device_id_in);
            time_record.setDeviceIdOut(dailytime.device_id_out);

            time_record.setDeviceModelIn(dailytime.nvar25_01);
            time_record.setDeviceModelOut(dailytime.nvar25_02);

            time_record.setDeviceNameIn(dailytime.nvar100_01);
            time_record.setDeviceNameOut(dailytime.nvar100_02);

            time_record.setGPSLocationIn(dailytime.gps_location_in);
            time_record.setGPSLocationOut(dailytime.gps_location_out);

            time_record.setAddressIn(dailytime.nvar100_03);
            time_record.setAddressOut(dailytime.nvar100_04);

            time_record.setLogType(dailytime.type_in_out);

            if(!dailytime.project_unique.equals("")){
                Project project = new Project();
                project.setUniquenumPri(dailytime.project_unique);
                project.setCode(dailytime.project_code);
                project.setDesc(dailytime.project_name);

                time_record.setProject(project);
            };


            timeLogs.add(time_record);
        }

        staffdata_repo.close();
        dailytime_repo.close();

        return timeLogs;
    }

    public TimeRecord convertToDailyTime(Staff staff, dailytime dailytime){
        TimeRecord timeRecord = new TimeRecord();

        timeRecord.setIdcode(dailytime.idcode);
        timeRecord.setUniquenumPri(dailytime.uniquenum_pri);
        timeRecord.setStaff(staff);
        timeRecord.setDateTimePost(dailytime.date_post);

        timeRecord.setDateTimeIn(dailytime.date_time_in);
        timeRecord.setDateTimeOut(dailytime.date_time_out);

        timeRecord.setDeviceIdIn(dailytime.device_id_in);
        timeRecord.setDeviceIdOut(dailytime.device_id_out);

        timeRecord.setDeviceModelIn(dailytime.nvar25_01);
        timeRecord.setDeviceModelOut(dailytime.nvar25_02);

        timeRecord.setDeviceNameIn(dailytime.nvar100_01);
        timeRecord.setDeviceNameOut(dailytime.nvar100_02);

        timeRecord.setGPSLocationIn(dailytime.gps_location_in);
        timeRecord.setGPSLocationOut(dailytime.gps_location_out);

        timeRecord.setAddressIn(dailytime.nvar100_03);
        timeRecord.setAddressOut(dailytime.nvar100_04);

        timeRecord.setLogType(dailytime.type_in_out);

        if(!dailytime.project_unique.equals("")){
            timeRecord.setProject(new Project());
            timeRecord.getProject().setUniquenumPri(dailytime.project_unique);
            timeRecord.getProject().setCode(dailytime.project_code);
            timeRecord.getProject().setDesc(dailytime.project_name);
        }

        if(!dailytime.sale_order_unique.equals("")){
            timeRecord.setSalesOrder(new SalesOrder());
            timeRecord.getSalesOrder().setUniquenumPri(dailytime.sale_order_unique);
            timeRecord.getSalesOrder().setCode(dailytime.sale_order_code);
            timeRecord.getSalesOrder().setDesc(dailytime.sale_order_name);
        }


        timeRecord.setSynced(((dailytime.sync_unique != null && dailytime.sync_unique != "") && (dailytime.date_lastupdate.compareTo(dailytime.date_sync)!=1)));

        return timeRecord;
    }

    private Staff convertEntity(staffdata staffdata){
        if(staffdata!=null){
            Staff staff = new Staff();
            staff.setIdcode(staffdata.idcode);
            staff.setUniquenumPri(staffdata.uniquenum_pri);
            staff.setStaff_desc(staffdata.staff_fullname);
            staff.setStaff_num(staffdata.staff_id);
            staff.setJob_title(staffdata.job_title);
            staff.setRegistered((staffdata.fingerprint_image1 != null && staffdata.fingerprint_image1.length > 0) || (staffdata.fingerprint_image2 != null && staffdata.fingerprint_image2.length > 0) || (staffdata.fingerprint_image3 != null && staffdata.fingerprint_image3.length > 0) || (staffdata.fingerprint_image4 != null && staffdata.fingerprint_image4.length > 0) || (staffdata.fingerprint_image5 != null && staffdata.fingerprint_image5.length > 0) ? true : false);

            if(staff.getRegistered()){
                scanimage_repo.open();
                scanimage scanimage = scanimage_repo.get_scanimage_staff_reg(staff.getUniquenumPri());
                if(scanimage != null){
                    staff.setRegisteration(scanimage.date_register);
                }
                scanimage_repo.close();
            }

            staff.setDate_posted(staffdata.date_post);
            staff.setDate_update(staffdata.date_lastupdate);
            staff.setWorkPermitId(staffdata.workpermit_num);
            staff.setWorkPermitIssued(staffdata.workpermit_issuedate);
            staff.setWorkPermitExpiry(staffdata.workpermit_expirydate);

            staff.setDob(staffdata.date_birth);
            staff.setNationality(staffdata.nationality);
            staff.setGender(staffdata.gender);
            staff.setRace(staffdata.race);
            staff.setActive(staffdata.active_yn.equals("y"));
            staff.setFingerprint_image1(staffdata.fingerprint_image1);
            staff.setFingerprint_image2(staffdata.fingerprint_image2);
            staff.setFingerprint_image3(staffdata.fingerprint_image3);
            staff.setFingerprint_image4(staffdata.fingerprint_image4);
            staff.setFingerprint_image5(staffdata.fingerprint_image5);
            staff.setPhoto1(staffdata.photo1);

            return staff;
        }else{
            return null;
        }
    }

    private staffdata convertToEntity(Staff staff) {
        staffdata staffdata = new staffdata();
        staffdata.idcode = staff.getIdcode();
        staffdata.uniquenum_pri = staff.getUniquenumPri();
        staffdata.masterfn = MASTERFN;
        staffdata.companyfn = COMPANYFN;
        staffdata.staff_fullname = staff.getStaff_desc();
        staffdata.staff_id = staff.getStaff_num();
        staffdata.job_title = staff.getJob_title();
        staffdata.date_post = staff.getDate_posted();
        staffdata.photo1 = staff.getPhoto1();
        staffdata.fingerprint_image1 = staff.getFingerprint_image1();
        staffdata.fingerprint_image2 = staff.getFingerprint_image2();
        staffdata.fingerprint_image3 = staff.getFingerprint_image3();
        staffdata.fingerprint_image4 = staff.getFingerprint_image4();
        staffdata.fingerprint_image5 = staff.getFingerprint_image5();
        staffdata.workpermit_num = staff.getWorkPermitId();
        staffdata.workpermit_issuedate = staff.getWorkPermitIssued();
        staffdata.workpermit_expirydate = staff.getWorkPermitExpiry();
        staffdata.date_birth = staff.getDob();
        staffdata.nationality = staff.getNationality();
        staffdata.gender = staff.getGender();
        staffdata.race = staff.getRace();
        staffdata.active_yn = staff.getActive() ? "y" : "n";

        return staffdata;
    }
}
