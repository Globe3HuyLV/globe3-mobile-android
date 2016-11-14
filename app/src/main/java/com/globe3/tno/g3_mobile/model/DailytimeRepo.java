package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.model.entities.dailytime;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;

public class DailytimeRepo {

    private SQLiteDatabase database;
    private Globe3Db dbHelper;
    private String[] allColumns = { Globe3Db.COLUMN_IDCODE,
            Globe3Db.COLUMN_TAG_TABLE_USAGE,
            Globe3Db.COLUMN_SYNC_UNIQUE,
            Globe3Db.COLUMN_UNIQUENUM_PRI,
            Globe3Db.COLUMN_UNIQUENUM_SEC,
            Globe3Db.COLUMN_TAG_VOID_YN,
            Globe3Db.COLUMN_TAG_DELETED_YN,
            Globe3Db.COLUMN_DATE_POST,
            Globe3Db.COLUMN_DATE_SUBMIT,
            Globe3Db.COLUMN_DATE_SYNC,
            Globe3Db.COLUMN_DATE_LASTUPDATE,
            Globe3Db.COLUMN_USERID_CREATOR,
            Globe3Db.COLUMN_MASTERFN,
            Globe3Db.COLUMN_COMPANYFN,
            Globe3Db.COLUMN_WORKER_UNIQUE,
            Globe3Db.COLUMN_WORKER_ID,
            Globe3Db.COLUMN_WORKER_FULLNAME,
            Globe3Db.COLUMN_TYPE_IN_OUT,
            Globe3Db.COLUMN_DATE_TIME_IN,
            Globe3Db.COLUMN_DATE_TIME_OUT,
            Globe3Db.COLUMN_GPS_LOCATION_IN,
            Globe3Db.COLUMN_GPS_LOCATION_OUT,
            Globe3Db.COLUMN_DEVICE_ID_IN,
            Globe3Db.COLUMN_DEVICE_ID_OUT,
            Globe3Db.COLUMN_PROJECT_CODE,
            Globe3Db.COLUMN_PROJECT_NAME,
            Globe3Db.COLUMN_PROJECT_UNIQUE,
            Globe3Db.COLUMN_JOB_CODE,
            Globe3Db.COLUMN_JOB_UNIQUE,
            Globe3Db.COLUMN_TASK_CODE,
            Globe3Db.COLUMN_TASK_UNIQUE,
            Globe3Db.COLUMN_NVAR25_01,
            Globe3Db.COLUMN_NVAR25_02,
            Globe3Db.COLUMN_NVAR25_03,
            Globe3Db.COLUMN_NVAR100_01,
            Globe3Db.COLUMN_NVAR100_02,
            Globe3Db.COLUMN_NVAR100_03,
            Globe3Db.COLUMN_NVAR100_04,
            Globe3Db.COLUMN_DATE01,
            Globe3Db.COLUMN_DATE02,
            Globe3Db.COLUMN_NUM01,
            Globe3Db.COLUMN_NUM02
    };

    public DailytimeRepo(Context context) {
        dbHelper = new Globe3Db(context);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public dailytime create_dailytime(dailytime dailytime) {

        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, dailytime.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, dailytime.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, dailytime.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, dailytime.uniquenum_sec);
        values.put(Globe3Db.COLUMN_TAG_VOID_YN, dailytime.tag_void_yn);
        values.put(Globe3Db.COLUMN_TAG_DELETED_YN, dailytime.tag_deleted_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(dailytime.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(dailytime.date_submit));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(dailytime.date_sync));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(dailytime.date_lastupdate));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, dailytime.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, dailytime.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, dailytime.companyfn);
        values.put(Globe3Db.COLUMN_WORKER_UNIQUE, dailytime.staff_unique);
        values.put(Globe3Db.COLUMN_WORKER_ID, dailytime.staff_id);
        values.put(Globe3Db.COLUMN_WORKER_FULLNAME, dailytime.staff_fullname);
        values.put(Globe3Db.COLUMN_TYPE_IN_OUT, dailytime.type_in_out);
        values.put(Globe3Db.COLUMN_DATE_TIME_IN, DateUtility.getDateString(dailytime.date_time_in));
        values.put(Globe3Db.COLUMN_DATE_TIME_OUT, DateUtility.getDateString(dailytime.date_time_out));
        values.put(Globe3Db.COLUMN_GPS_LOCATION_IN, dailytime.gps_location_in);
        values.put(Globe3Db.COLUMN_GPS_LOCATION_OUT, dailytime.gps_location_out);
        values.put(Globe3Db.COLUMN_DEVICE_ID_IN, dailytime.device_id_in);
        values.put(Globe3Db.COLUMN_DEVICE_ID_OUT, dailytime.device_id_out);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, dailytime.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, dailytime.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, dailytime.project_unique);
        values.put(Globe3Db.COLUMN_JOB_CODE, dailytime.job_code);
        values.put(Globe3Db.COLUMN_JOB_UNIQUE, dailytime.job_unique);
        values.put(Globe3Db.COLUMN_TASK_CODE, dailytime.task_code);
        values.put(Globe3Db.COLUMN_TASK_UNIQUE, dailytime.task_unique);
        values.put(Globe3Db.COLUMN_NVAR25_01, dailytime.nvar25_01);
        values.put(Globe3Db.COLUMN_NVAR25_02, dailytime.nvar25_02);
        values.put(Globe3Db.COLUMN_NVAR25_03, dailytime.nvar25_03);
        values.put(Globe3Db.COLUMN_NVAR100_01, dailytime.nvar100_01);
        values.put(Globe3Db.COLUMN_NVAR100_02, dailytime.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, dailytime.nvar100_03);
        values.put(Globe3Db.COLUMN_NVAR100_04, dailytime.nvar100_04);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(dailytime.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(dailytime.date02));
        values.put(Globe3Db.COLUMN_NUM01, dailytime.num01);
        values.put(Globe3Db.COLUMN_NUM02, dailytime.num02);
        long insertId = database.insert(Globe3Db.TABLE_DAILYTIME, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        dailytime new_dailytime = cursorToObject(cursor);
        cursor.close();
        return new_dailytime;
    }


    public void delete_dailytime(dailytime dailytime) {
        long id = dailytime.idcode;
        database.delete(Globe3Db.TABLE_DAILYTIME, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_dailytime_all(dailytime dailytime_all) {
        database.delete(Globe3Db.TABLE_DAILYTIME, null, null);
    }

    public void update_dailytime(dailytime dailytime) {

        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, dailytime.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, dailytime.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, dailytime.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, dailytime.uniquenum_sec);
        values.put(Globe3Db.COLUMN_TAG_VOID_YN, dailytime.tag_void_yn);
        values.put(Globe3Db.COLUMN_TAG_DELETED_YN, dailytime.tag_deleted_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(dailytime.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(dailytime.date_submit));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(dailytime.date_sync));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(dailytime.date_lastupdate));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, dailytime.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, dailytime.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, dailytime.companyfn);
        values.put(Globe3Db.COLUMN_WORKER_UNIQUE, dailytime.staff_unique);
        values.put(Globe3Db.COLUMN_WORKER_ID, dailytime.staff_id);
        values.put(Globe3Db.COLUMN_WORKER_FULLNAME, dailytime.staff_fullname);
        values.put(Globe3Db.COLUMN_TYPE_IN_OUT, dailytime.type_in_out);
        values.put(Globe3Db.COLUMN_DATE_TIME_IN, DateUtility.getDateString(dailytime.date_time_in));
        values.put(Globe3Db.COLUMN_DATE_TIME_OUT, DateUtility.getDateString(dailytime.date_time_out));
        values.put(Globe3Db.COLUMN_GPS_LOCATION_IN, dailytime.gps_location_in);
        values.put(Globe3Db.COLUMN_GPS_LOCATION_OUT, dailytime.gps_location_out);
        values.put(Globe3Db.COLUMN_DEVICE_ID_IN, dailytime.device_id_in);
        values.put(Globe3Db.COLUMN_DEVICE_ID_OUT, dailytime.device_id_out);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, dailytime.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, dailytime.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, dailytime.project_unique);
        values.put(Globe3Db.COLUMN_JOB_CODE, dailytime.job_code);
        values.put(Globe3Db.COLUMN_JOB_UNIQUE, dailytime.job_unique);
        values.put(Globe3Db.COLUMN_TASK_CODE, dailytime.task_code);
        values.put(Globe3Db.COLUMN_TASK_UNIQUE, dailytime.task_unique);
        values.put(Globe3Db.COLUMN_NVAR25_01, dailytime.nvar25_01);
        values.put(Globe3Db.COLUMN_NVAR25_02, dailytime.nvar25_02);
        values.put(Globe3Db.COLUMN_NVAR25_03, dailytime.nvar25_03);
        values.put(Globe3Db.COLUMN_NVAR100_01, dailytime.nvar100_01);
        values.put(Globe3Db.COLUMN_NVAR100_02, dailytime.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, dailytime.nvar100_03);
        values.put(Globe3Db.COLUMN_NVAR100_04, dailytime.nvar100_04);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(dailytime.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(dailytime.date02));
        values.put(Globe3Db.COLUMN_NUM01, dailytime.num01);
        values.put(Globe3Db.COLUMN_NUM02, dailytime.num02);

        database.update(Globe3Db.TABLE_DAILYTIME, values, Globe3Db.COLUMN_UNIQUENUM_PRI + " = '" + dailytime.uniquenum_pri + "'", null);
    }

    public ArrayList<dailytime> get_active_dailytimes() {
        ArrayList<dailytime> dailytimes = new ArrayList<dailytime>();

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '"+ COMPANYFN+ "' AND tag_void_yn='n' AND tag_deleted_yn='n'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dailytime dailytime = cursorToObject(cursor);
            dailytimes.add(dailytime);
            cursor.moveToNext();
        }

        cursor.close();

        return dailytimes;
    }

    public ArrayList<dailytime> get_staff_dailytimes(String pUniquenum, String pType, boolean pGetUnsync) {
        ArrayList<dailytime> dailytimes = new ArrayList<dailytime>();

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '"+ COMPANYFN+ "' AND tag_void_yn='n' AND tag_deleted_yn='n'" + (!pUniquenum.equals("") ? " AND staff_unique ='" + pUniquenum +   "' " : "") + (!pType.equals("") ? " AND type_in_out ='" + pType +   "' " : "") + (pGetUnsync ? " AND typeof(date_sync) = 'null'" : ""), null, null, null, "date_post ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dailytime dailytime = cursorToObject(cursor);
            dailytimes.add(dailytime);
            cursor.moveToNext();
        }

        cursor.close();

        return dailytimes;
    }

    public dailytime get_staff_time_in_dailytimes(String pUniquenum) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date dateBuffer = cal.getTime();

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '" + COMPANYFN + "' AND tag_void_yn='n' AND tag_deleted_yn='n' AND type_in_out='in' AND staff_unique ='" + pUniquenum + "' AND typeof(date_time_in) != 'null' AND date_time_in BETWEEN '" + DateUtility.getDateString(dateBuffer, "yyyy-MM-dd HH:mm:ss") + "' AND '" + DateUtility.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + "'", null, null, null, "date_time_in DESC", "1");

        cursor.moveToFirst();

        if(cursor.getCount() > 0 && cursorToObject(cursor).date_time_out == null){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return  null;
        }

    }

    public ArrayList<dailytime> get_report_dailytimes(String pDatePost, String pUniquenum, String pSortBy) {
        ArrayList<dailytime> dailytimes = new ArrayList<dailytime>();

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '" + COMPANYFN + "' AND tag_void_yn='n' AND tag_deleted_yn='n'" + (!pUniquenum.equals("") ? " AND staff_unique ='" + pUniquenum + "' " : "") + (!pDatePost.equals("") ? " AND date_post <= '" + pDatePost + " 23:59' " : ""), null, null, null, pSortBy + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dailytime dailytime = cursorToObject(cursor);
            dailytimes.add(dailytime);
            cursor.moveToNext();
        }

        cursor.close();

        return dailytimes;
    }

    public dailytime get_previous_time_in(String staffUnique, String logType){
        Cursor cursor = null;
        if(logType.equals(TagTableUsage.TIMELOG_IN)){
            cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '" + COMPANYFN + "' AND tag_void_yn='n' AND tag_deleted_yn='n' AND staff_unique ='" + staffUnique + "' AND typeof(date_time_in) != 'null'", null, null, null, "date_time_in DESC", "2");
        }else if(logType.equals(TagTableUsage.TIMELOG_OUT)){
            cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '" + COMPANYFN + "' AND tag_void_yn='n' AND tag_deleted_yn='n' AND staff_unique ='" + staffUnique + "' AND typeof(date_time_out) != 'null'", null, null, null, "date_time_out DESC", "2");
        }else{
            cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '" + COMPANYFN + "' AND type_in_out='loc_chk' AND tag_void_yn='n' AND tag_deleted_yn='n' AND staff_unique ='" + staffUnique + "' AND typeof(date_time_in) != 'null'", null, null, null, "date_time_in DESC", "2");
        }

        if(cursor != null && cursor.getCount() == 2){
            cursor.moveToPosition(1);
            return cursorToObject(cursor);
        }else{
            return  null;
        }
    }

    public ArrayList<dailytime> get_timesheet_day_dailytimes(Date reportDate) {
        ArrayList<dailytime> dailytimes = new ArrayList<dailytime>();

        String fromDate = DateUtility.getDateString(reportDate, "yyyy-MM-dd") + " 00:00:00";
        String toDate = DateUtility.getDateString(reportDate, "yyyy-MM-dd") + " 23:59:59";

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '" + COMPANYFN + "' AND tag_void_yn='n' AND tag_deleted_yn='n' AND (date_time_in BETWEEN '" + fromDate + "' AND '" + toDate + "' OR date_time_out BETWEEN '" + fromDate + "' AND '" + toDate + "')", null, null, null, "staff_fullname ASC, date_post ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dailytime dailytime = cursorToObject(cursor);
            dailytimes.add(dailytime);
            cursor.moveToNext();
        }

        cursor.close();

        return dailytimes;
    }

    public ArrayList<dailytime> get_timesheet_staff_dailytimes(Date reportDate, String staffUnique) {
        ArrayList<dailytime> dailytimes = new ArrayList<dailytime>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(reportDate);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DATE, lastDay);

        String fromDate = DateUtility.getDateString(reportDate, "yyyy-MM-dd") + " 00:00:00";
        String toDate = DateUtility.getDateString(cal.getTime(), "yyyy-MM-dd") + " 23:59:59";

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '"+ COMPANYFN+ "' AND tag_void_yn='n' AND tag_deleted_yn='n' AND staff_unique='" + staffUnique + "' AND (date_time_in BETWEEN '" + fromDate + "' AND '" + toDate + "' OR date_time_out BETWEEN '" + fromDate + "' AND '" + toDate + "')" , null, null, null, "date_post ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dailytime dailytime = cursorToObject(cursor);
            dailytimes.add(dailytime);
            cursor.moveToNext();
        }

        cursor.close();

        return dailytimes;
    }

    public ArrayList<dailytime> get_staff_location_history(Date reportDate, String staffUnique) {
        ArrayList<dailytime> dailytimes = new ArrayList<dailytime>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(reportDate);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DATE, lastDay);

        String fromDate = DateUtility.getDateString(reportDate, "yyyy-MM-dd") + " 00:00:00";
        String toDate = DateUtility.getDateString(cal.getTime(), "yyyy-MM-dd") + " 23:59:59";

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '"+ COMPANYFN+ "' AND tag_void_yn='n' AND tag_deleted_yn='n' AND tag_table_usage='loc_chk' AND staff_unique='" + staffUnique + "' AND (date_time_in BETWEEN '" + fromDate + "' AND '" + toDate + "')" , null, null, null, "date_post ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dailytime dailytime = cursorToObject(cursor);
            dailytimes.add(dailytime);
            cursor.moveToNext();
        }

        cursor.close();

        return dailytimes;
    }

    public ArrayList<dailytime> get_new_dailytimes() {
        ArrayList<dailytime> dailytimes = new ArrayList<dailytime>();

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '"+ COMPANYFN+ "' AND tag_void_yn='n' AND tag_deleted_yn='n' AND (typeof(date_sync) = 'null' OR (typeof(date_sync) != 'null' AND date_lastupdate > date_sync))" , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dailytime dailytime = cursorToObject(cursor);
            dailytimes.add(dailytime);
            cursor.moveToNext();
        }

        cursor.close();

        return dailytimes;
    }

    public dailytime get_dailytime(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_DAILYTIME, allColumns, "companyfn = '"+ COMPANYFN+ "' AND uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }


    private dailytime cursorToObject(Cursor cursor) {
        dailytime dailytime = new dailytime();
        dailytime.idcode = cursor.getLong(0);
        dailytime.tag_table_usage = cursor.getString(1);
        dailytime.sync_unique = cursor.getString(2);
        dailytime.uniquenum_pri = cursor.getString(3);
        dailytime.uniquenum_sec = cursor.getString(4);
        dailytime.tag_void_yn = cursor.getString(5);
        dailytime.tag_deleted_yn = cursor.getString(6);
        dailytime.date_post = DateUtility.getStringDate(cursor.getString(7));
        dailytime.date_submit = DateUtility.getStringDate(cursor.getString(8));
        dailytime.date_sync = DateUtility.getStringDate(cursor.getString(9));
        dailytime.date_lastupdate = DateUtility.getStringDate(cursor.getString(10));
        dailytime.userid_creator = cursor.getString(11);
        dailytime.masterfn = cursor.getString(12);
        dailytime.companyfn = cursor.getString(13);
        dailytime.staff_unique = cursor.getString(14);
        dailytime.staff_id = cursor.getString(15);
        dailytime.staff_fullname = cursor.getString(16);
        dailytime.type_in_out = cursor.getString(17);

        dailytime.date_time_in = DateUtility.getStringDate(cursor.getString(18));
        dailytime.date_time_out = DateUtility.getStringDate(cursor.getString(19));
        dailytime.gps_location_in = cursor.getString(20);
        dailytime.gps_location_out = cursor.getString(21);
        dailytime.device_id_in = cursor.getString(22);
        dailytime.device_id_out = cursor.getString(23);

        dailytime.project_code = cursor.getString(24);
        dailytime.project_name = cursor.getString(25);
        dailytime.project_unique = cursor.getString(26);
        dailytime.job_code = cursor.getString(27);
        dailytime.job_unique = cursor.getString(28);
        dailytime.task_code = cursor.getString(29);
        dailytime.task_unique = cursor.getString(30);
        dailytime.nvar25_01 = cursor.getString(31);
        dailytime.nvar25_02 = cursor.getString(32);
        dailytime.nvar25_03 = cursor.getString(33);
        dailytime.nvar100_01 = cursor.getString(34);
        dailytime.nvar100_02 = cursor.getString(35);
        dailytime.nvar100_03 = cursor.getString(36);
        dailytime.nvar100_04 = cursor.getString(37);
        dailytime.date01 = DateUtility.getStringDate(cursor.getString(38));
        dailytime.date02 = DateUtility.getStringDate(cursor.getString(39));
        dailytime.num01 = cursor.getFloat(40);
        dailytime.num02 = cursor.getFloat(41);


        return dailytime;
    }
}

