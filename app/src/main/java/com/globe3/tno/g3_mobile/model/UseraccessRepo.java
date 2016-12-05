package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.Encryption;
import com.globe3.tno.g3_mobile.model.entities.useraccess;

import java.util.ArrayList;

public class UseraccessRepo {

    private SQLiteDatabase database;
    private Globe3Db db_helper;
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
            Globe3Db.COLUMN_CFSQLFILENAME,
            Globe3Db.COLUMN_MASTERFN,
            Globe3Db.COLUMN_COMPANYFN,
            Globe3Db.COLUMN_COMPANYID,
            Globe3Db.COLUMN_USER_SPECIALNUM,
            Globe3Db.COLUMN_USERID,
            Globe3Db.COLUMN_PASSWORD,
            Globe3Db.COLUMN_DATE_VALID_TO,
            Globe3Db.COLUMN_DATE_VALID_FROM,
            Globe3Db.COLUMN_STAFF_CODE,
            Globe3Db.COLUMN_STAFF_DESC,
            Globe3Db.COLUMN_STAFF_UNIQUE,
            Globe3Db.COLUMN_DEVICE_ID
    };

    public UseraccessRepo(Context context) {
        db_helper = new Globe3Db(context);
    }


    public void open() throws SQLException {
        database = db_helper.getWritableDatabase();
    }


    public void close() {
        db_helper.close();
    }


    public useraccess create_useraccess(useraccess useraccess) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, useraccess.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, useraccess.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, useraccess.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, useraccess.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, useraccess.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(useraccess.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(useraccess.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(useraccess.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(useraccess.date_sync));
        values.put(Globe3Db.COLUMN_CFSQLFILENAME, useraccess.cfsqlfilename);
        values.put(Globe3Db.COLUMN_MASTERFN, useraccess.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, useraccess.companyfn);
        values.put(Globe3Db.COLUMN_COMPANYID, useraccess.companyid);
        values.put(Globe3Db.COLUMN_USER_SPECIALNUM, useraccess.user_specialnum);
        values.put(Globe3Db.COLUMN_USERID, useraccess.userid);
        values.put(Globe3Db.COLUMN_PASSWORD, Encryption.md5(useraccess.password));
        values.put(Globe3Db.COLUMN_DATE_VALID_TO, DateUtility.getDateString(useraccess.date_valid_to));
        values.put(Globe3Db.COLUMN_DATE_VALID_FROM, DateUtility.getDateString(useraccess.date_valid_from));
        values.put(Globe3Db.COLUMN_STAFF_CODE, useraccess.staff_code);
        values.put(Globe3Db.COLUMN_STAFF_DESC, useraccess.staff_desc);
        values.put(Globe3Db.COLUMN_STAFF_UNIQUE, useraccess.staff_unique);
        values.put(Globe3Db.COLUMN_DEVICE_ID, useraccess.device_id);
        long insertId = database.insert(Globe3Db.TABLE_USERACCESS, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_USERACCESS, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        useraccess new_useraccess = cursorToObject(cursor);
        cursor.close();
        return new_useraccess;
    }


    public void delete_useraccess(useraccess useraccess) {
        long id = useraccess.idcode;
        database.delete(Globe3Db.TABLE_USERACCESS, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_useraccess_all() {
        database.delete(Globe3Db.TABLE_USERACCESS, null, null);
    }

    public void update_useraccess(useraccess useraccess) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, useraccess.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, useraccess.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, useraccess.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, useraccess.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, useraccess.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(useraccess.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(useraccess.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(useraccess.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(useraccess.date_sync));
        values.put(Globe3Db.COLUMN_CFSQLFILENAME, useraccess.cfsqlfilename);
        values.put(Globe3Db.COLUMN_MASTERFN, useraccess.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, useraccess.companyfn);
        values.put(Globe3Db.COLUMN_COMPANYID, useraccess.companyid);
        values.put(Globe3Db.COLUMN_USER_SPECIALNUM, useraccess.user_specialnum);
        values.put(Globe3Db.COLUMN_USERID, useraccess.userid);
        values.put(Globe3Db.COLUMN_PASSWORD, useraccess.password);
        values.put(Globe3Db.COLUMN_DATE_VALID_TO, DateUtility.getDateString(useraccess.date_valid_to));
        values.put(Globe3Db.COLUMN_DATE_VALID_FROM, DateUtility.getDateString(useraccess.date_valid_from));
        values.put(Globe3Db.COLUMN_STAFF_CODE, useraccess.staff_code);
        values.put(Globe3Db.COLUMN_STAFF_DESC, useraccess.staff_desc);
        values.put(Globe3Db.COLUMN_STAFF_UNIQUE, useraccess.staff_unique);
        values.put(Globe3Db.COLUMN_DEVICE_ID, useraccess.device_id);


        database.update(Globe3Db.TABLE_USERACCESS, values, Globe3Db.COLUMN_IDCODE + " = " + useraccess.idcode, null);
    }


    public ArrayList<useraccess> get_active_useraccess() {
        ArrayList<useraccess> useraccesss = new ArrayList<useraccess>();

        Cursor cursor = database.query(Globe3Db.TABLE_USERACCESS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            useraccess useraccess = cursorToObject(cursor);
            if(useraccess.active_yn.equals("y")){
                useraccesss.add(useraccess);
            }
            cursor.moveToNext();
        }


        cursor.close();
        return useraccesss;
    }


    public useraccess get_useraccess(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_USERACCESS, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return null;
        }
    }

    public useraccess get_useraccess_by_name(String pUserLoginId){

        Cursor cursor = database.query(Globe3Db.TABLE_USERACCESS, allColumns, "userid = '"+pUserLoginId+"'", null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return null;
        }
    }

    public boolean useraccess_authenticate(String pCompany, String pUserId, String pPassword){
        Cursor cursor = database.query(Globe3Db.TABLE_USERACCESS, allColumns, "companyid='"+ pCompany +"' AND userid='"+ pUserId +"' AND password='"+ pPassword +"' AND (DATE() BETWEEN date_valid_from AND date_valid_to OR userid='m8')", null, null, null, null);

        return cursor.getCount() > 0;
    }

    public useraccess useraccess_get_by_login(String pCompany, String pUserId, String pPassword){
        Cursor cursor = database.query(Globe3Db.TABLE_USERACCESS, allColumns, "companyid='"+ pCompany +"' AND userid='"+ pUserId +"' AND password='"+ pPassword +"' AND (DATE() BETWEEN date_valid_from AND date_valid_to OR userid='m8')", null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return null;
        }
    }

    private useraccess cursorToObject(Cursor cursor) {
        useraccess useraccess = new useraccess();
        useraccess.idcode = cursor.getLong(0);
        useraccess.tag_table_usage = cursor.getString(1);
        useraccess.sync_unique = cursor.getString(2);
        useraccess.uniquenum_pri = cursor.getString(3);
        useraccess.uniquenum_sec = cursor.getString(4);
        useraccess.active_yn = cursor.getString(5);
        useraccess.date_post = DateUtility.getStringDate(cursor.getString(6));
        useraccess.date_submit = DateUtility.getStringDate(cursor.getString(7));
        useraccess.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        useraccess.date_sync = DateUtility.getStringDate(cursor.getString(9));
        useraccess.cfsqlfilename = cursor.getString(10);
        useraccess.masterfn = cursor.getString(11);
        useraccess.companyfn = cursor.getString(12);
        useraccess.companyid = cursor.getString(13);
        useraccess.user_specialnum = cursor.getString(14);
        useraccess.userid = cursor.getString(15);
        useraccess.password = cursor.getString(16);
        useraccess.date_valid_to = DateUtility.getStringDate(cursor.getString(17));
        useraccess.date_valid_from = DateUtility.getStringDate(cursor.getString(18));
        useraccess.staff_code = cursor.getString(19);
        useraccess.staff_desc = cursor.getString(20);
        useraccess.staff_unique = cursor.getString(21);
        useraccess.device_id = cursor.getString(22);

        return useraccess;
    }
}

