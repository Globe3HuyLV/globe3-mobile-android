package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.globe3.tno.g3_mobile.model.entities.staffdata;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;
import java.util.Date;

import static com.globe3.tno.g3_mobile.globals.Globals.COMPANYFN;

public class StaffdataRepo {

    private SQLiteDatabase database;
    private Globe3Db dbHelper;
    private String[] allColumns = { Globe3Db.COLUMN_IDCODE,
            Globe3Db.COLUMN_TAG_TABLE_USAGE,
            Globe3Db.COLUMN_SYNC_UNIQUE,
            Globe3Db.COLUMN_UNIQUENUM_PRI,
            Globe3Db.COLUMN_UNIQUENUM_SEC,
            Globe3Db.COLUMN_ACTIVE_YN,
            Globe3Db.COLUMN_DATE_POST,
            Globe3Db.COLUMN_DATE_SUBMIT,
            Globe3Db.COLUMN_DATE_LASTUPDATE,
            Globe3Db.COLUMN_DATE_SYNC,
            Globe3Db.COLUMN_USERID_CREATOR,
            Globe3Db.COLUMN_MASTERFN,
            Globe3Db.COLUMN_COMPANYFN,
            Globe3Db.COLUMN_CO_CODE,
            Globe3Db.COLUMN_CO_NAME,
            Globe3Db.COLUMN_OWN_WORKER_YN,
            Globe3Db.COLUMN_WORKER_TYPE,
            Globe3Db.COLUMN_WORKER_GROUP,
            Globe3Db.COLUMN_PAY_GROUP,
            Globe3Db.COLUMN_NATIONALITY,
            Globe3Db.COLUMN_GENDER,
            Globe3Db.COLUMN_RACE,
            Globe3Db.COLUMN_RELIGION,
            Globe3Db.COLUMN_JOB_TITLE,
            Globe3Db.COLUMN_WORKER_UNIQUE,
            Globe3Db.COLUMN_WORKER_ID,
            Globe3Db.COLUMN_WORKER_FULLNAME,
            Globe3Db.COLUMN_WORKER_TYPE,
            Globe3Db.COLUMN_WORKPERMIT_TYPE,
            Globe3Db.COLUMN_WORKPERMIT_NUM,
            Globe3Db.COLUMN_WORKPERMIT_ISSUEDATE,
            Globe3Db.COLUMN_WORKPERMIT_EXPIRYDATE,
            Globe3Db.COLUMN_WORK_STARTDATE,
            Globe3Db.COLUMN_WORK_CEASEDATE,
            Globe3Db.COLUMN_SUPERVISOR_NAME,
            Globe3Db.COLUMN_SUPERVISOR_UNIQUE,
            Globe3Db.COLUMN_VENDOR_NAME,
            Globe3Db.COLUMN_VENDOR_UNIQUE,
            Globe3Db.COLUMN_DATE_BIRTH,
            Globe3Db.COLUMN_PHOTO1,
            Globe3Db.COLUMN_PHOTO2,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE1,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE2,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE3,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE4,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE5,
            Globe3Db.COLUMN_PROJECT_CODE,
            Globe3Db.COLUMN_PROJECT_NAME,
            Globe3Db.COLUMN_PROJECT_UNIQUE
    };

    public StaffdataRepo(Context context) {
        dbHelper = new Globe3Db(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public staffdata create_staffdata(staffdata staffdata) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, staffdata.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, staffdata.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, staffdata.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, staffdata.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, staffdata.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(staffdata.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(staffdata.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(staffdata.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(staffdata.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, staffdata.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, staffdata.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, staffdata.companyfn);
        values.put(Globe3Db.COLUMN_CO_CODE, staffdata.co_code);
        values.put(Globe3Db.COLUMN_CO_NAME, staffdata.co_name);
        values.put(Globe3Db.COLUMN_OWN_WORKER_YN, staffdata.own_staff_yn);
        values.put(Globe3Db.COLUMN_WORKER_TYPE, staffdata.staff_type);
        values.put(Globe3Db.COLUMN_WORKER_GROUP, staffdata.staff_group);
        values.put(Globe3Db.COLUMN_PAY_GROUP, staffdata.pay_group);
        values.put(Globe3Db.COLUMN_NATIONALITY, staffdata.nationality);
        values.put(Globe3Db.COLUMN_GENDER, staffdata.gender);
        values.put(Globe3Db.COLUMN_RACE, staffdata.race);
        values.put(Globe3Db.COLUMN_RELIGION, staffdata.religion);
        values.put(Globe3Db.COLUMN_JOB_TITLE, staffdata.job_title);
        values.put(Globe3Db.COLUMN_WORKER_UNIQUE, staffdata.staff_unique);
        values.put(Globe3Db.COLUMN_WORKER_ID, staffdata.staff_id);
        values.put(Globe3Db.COLUMN_WORKER_FULLNAME, staffdata.staff_fullname);
        values.put(Globe3Db.COLUMN_WORKER_TYPE, staffdata.staff_type);
        values.put(Globe3Db.COLUMN_WORKPERMIT_TYPE, staffdata.workpermit_type);
        values.put(Globe3Db.COLUMN_WORKPERMIT_NUM, staffdata.workpermit_num);
        values.put(Globe3Db.COLUMN_WORKPERMIT_ISSUEDATE, DateUtility.getDateString(staffdata.workpermit_issuedate));
        values.put(Globe3Db.COLUMN_WORKPERMIT_EXPIRYDATE, DateUtility.getDateString(staffdata.workpermit_expirydate));
        values.put(Globe3Db.COLUMN_WORK_STARTDATE, DateUtility.getDateString(staffdata.work_startdate));
        values.put(Globe3Db.COLUMN_WORK_CEASEDATE, DateUtility.getDateString(staffdata.work_ceasedate));
        values.put(Globe3Db.COLUMN_SUPERVISOR_NAME, staffdata.supervisor_name);
        values.put(Globe3Db.COLUMN_SUPERVISOR_UNIQUE, staffdata.supervisor_unique);
        values.put(Globe3Db.COLUMN_VENDOR_NAME, staffdata.vendor_name);
        values.put(Globe3Db.COLUMN_VENDOR_UNIQUE, staffdata.vendor_unique);
        values.put(Globe3Db.COLUMN_DATE_BIRTH, DateUtility.getDateString(staffdata.date_birth));
        values.put(Globe3Db.COLUMN_PHOTO1, staffdata.photo1);
        values.put(Globe3Db.COLUMN_PHOTO2, staffdata.photo2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE1, staffdata.fingerprint_image1);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE2, staffdata.fingerprint_image2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE3, staffdata.fingerprint_image3);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE4, staffdata.fingerprint_image4);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE5, staffdata.fingerprint_image5);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, staffdata.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, staffdata.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, staffdata.project_unique);
        long insertId = database.insert(Globe3Db.TABLE_WDATA, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        staffdata new_staffdata = cursorToObject(cursor);
        cursor.close();
        return new_staffdata;
    }

    public void delete_staffdata(staffdata staffdata) {
        long id = staffdata.idcode;
        database.delete(Globe3Db.TABLE_WDATA, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_staffdata_all() {
        database.delete(Globe3Db.TABLE_WDATA, null, null);
    }

    public void update_staffdata(staffdata staffdata) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, staffdata.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, staffdata.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, staffdata.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, staffdata.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, staffdata.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(staffdata.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(staffdata.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(new Date()));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(staffdata.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, staffdata.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, staffdata.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, staffdata.companyfn);
        values.put(Globe3Db.COLUMN_CO_CODE, staffdata.co_code);
        values.put(Globe3Db.COLUMN_CO_NAME, staffdata.co_name);
        values.put(Globe3Db.COLUMN_OWN_WORKER_YN, staffdata.own_staff_yn);
        values.put(Globe3Db.COLUMN_WORKER_TYPE, staffdata.staff_type);
        values.put(Globe3Db.COLUMN_WORKER_GROUP, staffdata.staff_group);
        values.put(Globe3Db.COLUMN_PAY_GROUP, staffdata.pay_group);
        values.put(Globe3Db.COLUMN_NATIONALITY, staffdata.nationality);
        values.put(Globe3Db.COLUMN_GENDER, staffdata.gender);
        values.put(Globe3Db.COLUMN_RACE, staffdata.race);
        values.put(Globe3Db.COLUMN_RELIGION, staffdata.religion);
        values.put(Globe3Db.COLUMN_JOB_TITLE, staffdata.job_title);
        values.put(Globe3Db.COLUMN_WORKER_UNIQUE, staffdata.staff_unique);
        values.put(Globe3Db.COLUMN_WORKER_ID, staffdata.staff_id);
        values.put(Globe3Db.COLUMN_WORKER_FULLNAME, staffdata.staff_fullname);
        values.put(Globe3Db.COLUMN_WORKER_TYPE, staffdata.staff_type);
        values.put(Globe3Db.COLUMN_WORKPERMIT_TYPE, staffdata.workpermit_type);
        values.put(Globe3Db.COLUMN_WORKPERMIT_NUM, staffdata.workpermit_num);
        values.put(Globe3Db.COLUMN_WORKPERMIT_ISSUEDATE, DateUtility.getDateString(staffdata.workpermit_issuedate));
        values.put(Globe3Db.COLUMN_WORKPERMIT_EXPIRYDATE, DateUtility.getDateString(staffdata.workpermit_expirydate));
        values.put(Globe3Db.COLUMN_WORK_STARTDATE, DateUtility.getDateString(staffdata.work_startdate));
        values.put(Globe3Db.COLUMN_WORK_CEASEDATE, DateUtility.getDateString(staffdata.work_ceasedate));
        values.put(Globe3Db.COLUMN_SUPERVISOR_NAME, staffdata.supervisor_name);
        values.put(Globe3Db.COLUMN_SUPERVISOR_UNIQUE, staffdata.supervisor_unique);
        values.put(Globe3Db.COLUMN_VENDOR_NAME, staffdata.vendor_name);
        values.put(Globe3Db.COLUMN_VENDOR_UNIQUE, staffdata.vendor_unique);
        values.put(Globe3Db.COLUMN_DATE_BIRTH, DateUtility.getDateString(staffdata.date_birth));
        values.put(Globe3Db.COLUMN_PHOTO1, staffdata.photo1);
        values.put(Globe3Db.COLUMN_PHOTO2, staffdata.photo2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE1, staffdata.fingerprint_image1);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE2, staffdata.fingerprint_image2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE3, staffdata.fingerprint_image3);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE4, staffdata.fingerprint_image4);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE5, staffdata.fingerprint_image5);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, staffdata.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, staffdata.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, staffdata.project_unique);

        database.update(Globe3Db.TABLE_WDATA, values, Globe3Db.COLUMN_IDCODE + " = " + staffdata.idcode, null);
    }


    public ArrayList<staffdata> get_active_staffdatas() {
        ArrayList<staffdata> staffdatas = new ArrayList<staffdata>();

        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, "companyfn = '"+COMPANYFN+"'", null, null, null, "staff_fullname ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            staffdata staffdata = cursorToObject(cursor);
            if(staffdata.active_yn.equals("y")){
                staffdatas.add(staffdata);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return staffdatas;
    }

    public ArrayList<staffdata> get_updated_staffdatas() {
        ArrayList<staffdata> staffdatas = new ArrayList<staffdata>();

        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, "companyfn = '"+COMPANYFN+"' AND typeof(date_sync) = 'null'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            staffdata staffdata = cursorToObject(cursor);
            if(staffdata.active_yn.equals("y")){
                staffdatas.add(staffdata);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return staffdatas;
    }

    public ArrayList<staffdata> search_staffdatas(String pSearch) {
        ArrayList<staffdata> staffdatas = new ArrayList<staffdata>();

        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, "companyfn = '"+COMPANYFN+"' AND (staff_fullname LIKE '%" + pSearch.trim() + "%' OR staff_id LIKE '%" + pSearch.trim() + "%')", null, null, null, "staff_fullname ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            staffdata staffdata = cursorToObject(cursor);
            if(staffdata.active_yn.equals("y")){
                staffdatas.add(staffdata);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return staffdatas;
    }

    public ArrayList<staffdata> get_active_staffdatas(String pSortBy) {
        ArrayList<staffdata> staffdatas = new ArrayList<staffdata>();

        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, "companyfn = '"+COMPANYFN+"'", null, null, null, pSortBy + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            staffdata staffdata = cursorToObject(cursor);
            if(staffdata.active_yn.equals("y")){
                staffdatas.add(staffdata);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return staffdatas;
    }

    public ArrayList<staffdata> get_report_staffdatas(String pDatePost, String pUniquenum, String pSortBy) {
        ArrayList<staffdata> staffdatas = new ArrayList<staffdata>();

        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, "companyfn = '"+COMPANYFN+"'" + (!pUniquenum.equals("") ? " AND uniquenum_pri ='" + pUniquenum +   "' " : "") + (!pDatePost.equals("") ? " AND date_post <= '" + pDatePost +   "' " : ""), null, null, null, pSortBy + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            staffdata staffdata = cursorToObject(cursor);
            if(staffdata.active_yn.equals("y")){
                staffdatas.add(staffdata);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return staffdatas;
    }

    public ArrayList<staffdata> get_registered_staffdatas() {
        ArrayList<staffdata> staffdatas = new ArrayList<staffdata>();

        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, "companyfn = '"+COMPANYFN+"' AND (LENGTH(fingerprint_image1)>0 OR LENGTH(fingerprint_image2)>0 OR LENGTH(fingerprint_image3)>0 OR LENGTH(fingerprint_image4)>0 OR LENGTH(fingerprint_image2)>5)", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            staffdata staffdata = cursorToObject(cursor);
            if(staffdata.active_yn.equals("y")){
                staffdatas.add(staffdata);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return staffdatas;
    }

    public staffdata get_staffdata(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_WDATA, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return null;
        }
    }

    private staffdata cursorToObject(Cursor cursor) {
        staffdata staffdata = new staffdata();
        staffdata.idcode = cursor.getLong(0);
        staffdata.tag_table_usage = cursor.getString(1);
        staffdata.sync_unique = cursor.getString(2);
        staffdata.uniquenum_pri = cursor.getString(3);
        staffdata.uniquenum_sec = cursor.getString(4);
        staffdata.active_yn = cursor.getString(5);
        staffdata.date_post = DateUtility.getStringDate(cursor.getString(6));
        staffdata.date_submit = DateUtility.getStringDate(cursor.getString(7));
        staffdata.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        staffdata.date_sync = DateUtility.getStringDate(cursor.getString(9));
        staffdata.userid_creator = cursor.getString(10);
        staffdata.masterfn = cursor.getString(11);
        staffdata.companyfn = cursor.getString(12);
        staffdata.co_code = cursor.getString(13);
        staffdata.co_name = cursor.getString(14);
        staffdata.own_staff_yn = cursor.getString(15);
        staffdata.staff_type = cursor.getString(16);
        staffdata.staff_group = cursor.getString(17);
        staffdata.pay_group = cursor.getString(18);
        staffdata.nationality = cursor.getString(19);
        staffdata.gender = cursor.getString(20);
        staffdata.race = cursor.getString(21);
        staffdata.religion = cursor.getString(22);
        staffdata.job_title = cursor.getString(23);
        staffdata.staff_unique = cursor.getString(24);
        staffdata.staff_id = cursor.getString(25);
        staffdata.staff_fullname = cursor.getString(26);
        staffdata.staff_type = cursor.getString(27);
        staffdata.workpermit_type = cursor.getString(28);
        staffdata.workpermit_num = cursor.getString(29);
        staffdata.workpermit_issuedate = DateUtility.getStringDate(cursor.getString(30));
        staffdata.workpermit_expirydate = DateUtility.getStringDate(cursor.getString(31));
        staffdata.work_startdate = DateUtility.getStringDate(cursor.getString(32));
        staffdata.work_ceasedate = DateUtility.getStringDate(cursor.getString(33));
        staffdata.supervisor_name = cursor.getString(34);
        staffdata.supervisor_unique = cursor.getString(35);
        staffdata.vendor_name = cursor.getString(36);
        staffdata.vendor_unique = cursor.getString(37);
        staffdata.date_birth = DateUtility.getStringDate(cursor.getString(38));
        staffdata.photo1 = cursor.getBlob(39);
        staffdata.photo2 = cursor.getBlob(40);
        staffdata.fingerprint_image1 = cursor.getBlob(41);
        staffdata.fingerprint_image2 = cursor.getBlob(42);
        staffdata.fingerprint_image3 = cursor.getBlob(43);
        staffdata.fingerprint_image4 = cursor.getBlob(44);
        staffdata.fingerprint_image5 = cursor.getBlob(45);
        staffdata.project_code = cursor.getString(46);
        staffdata.project_name = cursor.getString(47);
        staffdata.project_unique = cursor.getString(48);

        return staffdata;
    }
}

