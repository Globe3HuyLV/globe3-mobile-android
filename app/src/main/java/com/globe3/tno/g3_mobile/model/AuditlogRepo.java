package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.globe3.tno.g3_mobile.model.entities.auditlog;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;
import java.util.Date;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_ID;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_MODEL;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.PHONE_NUMBER;
import static com.globe3.tno.g3_mobile.globals.Globals.mGPSLocation;


public class AuditlogRepo {

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
            Globe3Db.COLUMN_MASTERFN,
            Globe3Db.COLUMN_COMPANYFN,
            Globe3Db.COLUMN_USERID,
            Globe3Db.COLUMN_USER_SPECIALNUM,
            Globe3Db.COLUMN_GPS_LOCATION,
            Globe3Db.COLUMN_DEVICE_ID,
            Globe3Db.COLUMN_NVAR25_01,
            Globe3Db.COLUMN_NVAR25_02,
            Globe3Db.COLUMN_NVAR25_03,
            Globe3Db.COLUMN_NVAR100_01,
            Globe3Db.COLUMN_NVAR100_02,
            Globe3Db.COLUMN_NVAR100_03,
            Globe3Db.COLUMN_DATE01,
            Globe3Db.COLUMN_DATE02,
            Globe3Db.COLUMN_NUM01,
            Globe3Db.COLUMN_NUM02
    };

    public AuditlogRepo(Context context) {
        dbHelper = new Globe3Db(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public auditlog create_auditlog(auditlog auditlog) {

        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, auditlog.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, auditlog.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, auditlog.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, auditlog.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, auditlog.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(auditlog.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(auditlog.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(auditlog.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(auditlog.date_sync));
        values.put(Globe3Db.COLUMN_MASTERFN, auditlog.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, auditlog.companyfn);
        values.put(Globe3Db.COLUMN_USERID, auditlog.userid);
        values.put(Globe3Db.COLUMN_USER_SPECIALNUM, auditlog.user_specialnum);
        values.put(Globe3Db.COLUMN_GPS_LOCATION, mGPSLocation.getCoordinates());
        values.put(Globe3Db.COLUMN_DEVICE_ID, DEVICE_ID);
        values.put(Globe3Db.COLUMN_NVAR25_01, DEVICE_MODEL);
        values.put(Globe3Db.COLUMN_NVAR25_02, DEVICE_NAME);
        values.put(Globe3Db.COLUMN_NVAR25_03, PHONE_NUMBER);
        values.put(Globe3Db.COLUMN_NVAR100_01, mGPSLocation.getFullAddress());
        values.put(Globe3Db.COLUMN_NVAR100_02, auditlog.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, auditlog.nvar100_03);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(auditlog.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(auditlog.date02));
        values.put(Globe3Db.COLUMN_NUM01, auditlog.num01);
        values.put(Globe3Db.COLUMN_NUM02, auditlog.num02);
        long insertId = database.insert(Globe3Db.TABLE_AUDITLOG, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_AUDITLOG, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        auditlog new_auditlog = cursorToObject(cursor);
        cursor.close();
        return new_auditlog;
    }

    public void delete_auditlog(auditlog auditlog) {
        long id = auditlog.idcode;
        database.delete(Globe3Db.TABLE_AUDITLOG, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_auditlog_all() {
        database.delete(Globe3Db.TABLE_AUDITLOG, null, null);
    }

    public void update_auditlog(auditlog auditlog) {

        auditlog.date_lastupdate = new Date();

        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, auditlog.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, auditlog.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, auditlog.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, auditlog.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, auditlog.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(auditlog.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(auditlog.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(auditlog.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(auditlog.date_sync));
        values.put(Globe3Db.COLUMN_MASTERFN, auditlog.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, auditlog.companyfn);
        values.put(Globe3Db.COLUMN_USERID, auditlog.userid);
        values.put(Globe3Db.COLUMN_USER_SPECIALNUM, auditlog.user_specialnum);
        values.put(Globe3Db.COLUMN_GPS_LOCATION, mGPSLocation.getCoordinates());
        values.put(Globe3Db.COLUMN_DEVICE_ID, DEVICE_ID);
        values.put(Globe3Db.COLUMN_NVAR25_01, DEVICE_MODEL);
        values.put(Globe3Db.COLUMN_NVAR25_02, DEVICE_NAME);
        values.put(Globe3Db.COLUMN_NVAR25_03, PHONE_NUMBER);
        values.put(Globe3Db.COLUMN_NVAR100_01, mGPSLocation.getFullAddress());
        values.put(Globe3Db.COLUMN_NVAR100_02, auditlog.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, auditlog.nvar100_03);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(auditlog.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(auditlog.date02));
        values.put(Globe3Db.COLUMN_NUM01, auditlog.num01);
        values.put(Globe3Db.COLUMN_NUM02, auditlog.num02);

        database.update(Globe3Db.TABLE_AUDITLOG, values, Globe3Db.COLUMN_IDCODE + " = " + auditlog.idcode, null);
    }


    public ArrayList<auditlog> get_active_auditlogs() {
        ArrayList<auditlog> auditlogs = new ArrayList<auditlog>();

        Cursor cursor = database.query(Globe3Db.TABLE_AUDITLOG, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            auditlog auditlog = cursorToObject(cursor);
            if(auditlog.active_yn.equals("y")){
                auditlogs.add(auditlog);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return auditlogs;
    }

    public auditlog get_auditlog_latest_sync(String pUsage){
        //Cursor cursor = database.query(Globe3Db.TABLE_SCANIMAGE, allColumns, "staff_unique=?", new String[]{pUniquenum}, null, null, "date_register DESC", "1");
        Cursor cursor = database.query(Globe3Db.TABLE_AUDITLOG, allColumns, "tag_table_usage=?", new String[]{pUsage}, null, null, "date_post DESC", "1");

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return null;
        }
    }

    public auditlog get_auditlog(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_AUDITLOG, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    public void database_truncate(){
        //database.delete(Globe3Db.TABLE_AUDITLOG, null, null);
        //database.delete(Globe3Db.TABLE_DAILYTIME, null, null);
        database.delete(Globe3Db.TABLE_ENTITY, null, null);
        database.delete(Globe3Db.TABLE_PROJECT, null, null);
        database.delete(Globe3Db.TABLE_SCANIMAGE, null, null);
        database.delete(Globe3Db.TABLE_TABLEDATA, null, null);
        database.delete(Globe3Db.TABLE_USERACCESS, null, null);
        database.delete(Globe3Db.TABLE_WDATA, null, null);
    }

    private auditlog cursorToObject(Cursor cursor) {
        auditlog auditlog = new auditlog();
        auditlog.idcode = cursor.getLong(0);
        auditlog.tag_table_usage = cursor.getString(1);
        auditlog.sync_unique = cursor.getString(2);
        auditlog.uniquenum_pri = cursor.getString(3);
        auditlog.uniquenum_sec = cursor.getString(4);
        auditlog.active_yn = cursor.getString(5);
        auditlog.date_post = DateUtility.getStringDate(cursor.getString(6));
        auditlog.date_submit = DateUtility.getStringDate(cursor.getString(7));
        auditlog.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        auditlog.date_sync = DateUtility.getStringDate(cursor.getString(9));
        auditlog.masterfn = cursor.getString(10);
        auditlog.companyfn = cursor.getString(11);
        auditlog.userid = cursor.getString(12);
        auditlog.user_specialnum = cursor.getString(13);
        auditlog.gps_location = cursor.getString(14);
        auditlog.device_id = cursor.getString(15);
        auditlog.nvar25_01 = cursor.getString(16);
        auditlog.nvar25_02 = cursor.getString(17);
        auditlog.nvar25_03 = cursor.getString(18);
        auditlog.nvar100_01 = cursor.getString(19);
        auditlog.nvar100_02 = cursor.getString(20);
        auditlog.nvar100_03 = cursor.getString(21);
        auditlog.date01 = DateUtility.getStringDate(cursor.getString(22));
        auditlog.date02 = DateUtility.getStringDate(cursor.getString(23));
        auditlog.num01 = cursor.getFloat(24);
        auditlog.num02 = cursor.getFloat(25);

        return auditlog;
    }
}

