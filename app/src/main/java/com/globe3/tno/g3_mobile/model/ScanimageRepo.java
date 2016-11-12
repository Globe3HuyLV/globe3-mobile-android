package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.model.entities.scanimage;

import java.util.ArrayList;

import static com.globe3.tno.g3_mobile.globals.Globals.mGPSLocation;

public class ScanimageRepo {

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
            Globe3Db.COLUMN_WORKER_UNIQUE,
            Globe3Db.COLUMN_WORKER_ID,
            Globe3Db.COLUMN_WORKER_FULLNAME,
            Globe3Db.COLUMN_PHOTO1,
            Globe3Db.COLUMN_PHOTO2,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE1,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE2,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE3,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE4,
            Globe3Db.COLUMN_FINGERPRINT_IMAGE5,
            Globe3Db.COLUMN_DATE_REGISTER,
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

    public ScanimageRepo(Context context) {
        dbHelper = new Globe3Db(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public scanimage create_scanimage(scanimage scanimage) {

        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, scanimage.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, scanimage.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, scanimage.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, scanimage.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, scanimage.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(scanimage.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(scanimage.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(scanimage.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(scanimage.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, scanimage.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, scanimage.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, scanimage.companyfn);
        values.put(Globe3Db.COLUMN_WORKER_UNIQUE, scanimage.staff_unique);
        values.put(Globe3Db.COLUMN_WORKER_ID, scanimage.staff_id);
        values.put(Globe3Db.COLUMN_WORKER_FULLNAME, scanimage.staff_fullname);
        values.put(Globe3Db.COLUMN_PHOTO1, scanimage.photo1);
        values.put(Globe3Db.COLUMN_PHOTO2, scanimage.photo2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE1, scanimage.fingerprint_image1);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE2, scanimage.fingerprint_image2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE3, scanimage.fingerprint_image3);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE4, scanimage.fingerprint_image4);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE5, scanimage.fingerprint_image5);
        values.put(Globe3Db.COLUMN_DATE_REGISTER, DateUtility.getDateString(scanimage.date_register));
        values.put(Globe3Db.COLUMN_GPS_LOCATION, mGPSLocation.getCoordinates());
        values.put(Globe3Db.COLUMN_DEVICE_ID, scanimage.device_id);
        values.put(Globe3Db.COLUMN_NVAR25_01, scanimage.nvar25_01);
        values.put(Globe3Db.COLUMN_NVAR25_02, scanimage.nvar25_02);
        values.put(Globe3Db.COLUMN_NVAR25_03, scanimage.nvar25_03);
        values.put(Globe3Db.COLUMN_NVAR100_01, mGPSLocation.getFullAddress());
        values.put(Globe3Db.COLUMN_NVAR100_02, scanimage.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, scanimage.nvar100_03);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(scanimage.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(scanimage.date02));
        values.put(Globe3Db.COLUMN_NUM01, scanimage.num01);
        values.put(Globe3Db.COLUMN_NUM02, scanimage.num02);
        long insertId = database.insert(Globe3Db.TABLE_BIOM_SCANIMAGE, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_SCANIMAGE, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        scanimage new_scanimage = cursorToObject(cursor);
        cursor.close();
        return new_scanimage;
    }


    public void delete_scanimage(scanimage scanimage) {
        long id = scanimage.idcode;
        database.delete(Globe3Db.TABLE_BIOM_SCANIMAGE, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_scanimage_all() {
        database.delete(Globe3Db.TABLE_BIOM_SCANIMAGE, null, null);
    }

    public void update_scanimage(scanimage scanimage) {

        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, scanimage.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, scanimage.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, scanimage.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, scanimage.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, scanimage.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(scanimage.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(scanimage.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(scanimage.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(scanimage.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, scanimage.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, scanimage.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, scanimage.companyfn);
        values.put(Globe3Db.COLUMN_WORKER_UNIQUE, scanimage.staff_unique);
        values.put(Globe3Db.COLUMN_WORKER_ID, scanimage.staff_id);
        values.put(Globe3Db.COLUMN_WORKER_FULLNAME, scanimage.staff_fullname);
        values.put(Globe3Db.COLUMN_PHOTO1, scanimage.photo1);
        values.put(Globe3Db.COLUMN_PHOTO2, scanimage.photo2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE1, scanimage.fingerprint_image1);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE2, scanimage.fingerprint_image2);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE3, scanimage.fingerprint_image3);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE4, scanimage.fingerprint_image4);
        values.put(Globe3Db.COLUMN_FINGERPRINT_IMAGE5, scanimage.fingerprint_image5);
        values.put(Globe3Db.COLUMN_DATE_REGISTER, DateUtility.getDateString(scanimage.date_register));
        values.put(Globe3Db.COLUMN_GPS_LOCATION, mGPSLocation.getCoordinates());
        values.put(Globe3Db.COLUMN_DEVICE_ID, scanimage.device_id);
        values.put(Globe3Db.COLUMN_NVAR25_01, scanimage.nvar25_01);
        values.put(Globe3Db.COLUMN_NVAR25_02, scanimage.nvar25_02);
        values.put(Globe3Db.COLUMN_NVAR25_03, scanimage.nvar25_03);
        values.put(Globe3Db.COLUMN_NVAR100_01, mGPSLocation.getFullAddress());
        values.put(Globe3Db.COLUMN_NVAR100_02, scanimage.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, scanimage.nvar100_03);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(scanimage.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(scanimage.date02));
        values.put(Globe3Db.COLUMN_NUM01, scanimage.num01);
        values.put(Globe3Db.COLUMN_NUM02, scanimage.num02);

        database.update(Globe3Db.TABLE_BIOM_SCANIMAGE, values, Globe3Db.COLUMN_IDCODE + " = " + scanimage.idcode, null);
    }


    public ArrayList<scanimage> get_active_scanimages() {
        ArrayList<scanimage> scanimages = new ArrayList<scanimage>();

        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_SCANIMAGE, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            scanimage scanimage = cursorToObject(cursor);
            if(scanimage.active_yn.equals("y")){
                scanimages.add(scanimage);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return scanimages;
    }

    public scanimage get_scanimage_staff_reg(String pUniquenum){
        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_SCANIMAGE, allColumns, "staff_unique=?", new String[]{pUniquenum}, null, null, "date_register DESC", "1");

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return null;
        }

    }


    public scanimage get_scanimage(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_SCANIMAGE, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }


    private scanimage cursorToObject(Cursor cursor) {
        scanimage scanimage = new scanimage();
        scanimage.idcode = cursor.getLong(0);
        scanimage.tag_table_usage = cursor.getString(1);
        scanimage.sync_unique = cursor.getString(2);
        scanimage.uniquenum_pri = cursor.getString(3);
        scanimage.uniquenum_sec = cursor.getString(4);
        scanimage.active_yn = cursor.getString(5);
        scanimage.date_post = DateUtility.getStringDate(cursor.getString(6));
        scanimage.date_submit = DateUtility.getStringDate(cursor.getString(7));
        scanimage.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        scanimage.date_sync = DateUtility.getStringDate(cursor.getString(9));
        scanimage.userid_creator = cursor.getString(10);
        scanimage.masterfn = cursor.getString(11);
        scanimage.companyfn = cursor.getString(12);
        scanimage.staff_unique = cursor.getString(13);
        scanimage.staff_id = cursor.getString(14);
        scanimage.staff_fullname = cursor.getString(15);
        scanimage.photo1 = cursor.getBlob(16);
        scanimage.photo2 = cursor.getBlob(17);
        scanimage.fingerprint_image1 = cursor.getBlob(18);
        scanimage.fingerprint_image2 = cursor.getBlob(19);
        scanimage.fingerprint_image3 = cursor.getBlob(20);
        scanimage.fingerprint_image4 = cursor.getBlob(21);
        scanimage.fingerprint_image5 = cursor.getBlob(22);
        scanimage.date_register = DateUtility.getStringDate(cursor.getString(22));
        scanimage.gps_location = cursor.getString(24);
        scanimage.device_id = cursor.getString(25);
        scanimage.nvar25_01 = cursor.getString(26);
        scanimage.nvar25_02 = cursor.getString(27);
        scanimage.nvar25_03 = cursor.getString(28);
        scanimage.nvar100_01 = cursor.getString(29);
        scanimage.nvar100_02 = cursor.getString(30);
        scanimage.nvar100_03 = cursor.getString(31);
        scanimage.date01 = DateUtility.getStringDate(cursor.getString(32));
        scanimage.date02 = DateUtility.getStringDate(cursor.getString(33));
        scanimage.num01 = cursor.getFloat(34);
        scanimage.num02 = cursor.getFloat(35);

        return scanimage;
    }
}

