package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.model.entities.entproject;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;

public class ProjectRepo {

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
            Globe3Db.COLUMN_PROJECT_CODE,
            Globe3Db.COLUMN_PROJECT_NAME,
            Globe3Db.COLUMN_PROJECT_UNIQUE
    };



    public ProjectRepo(Context context) {
        dbHelper = new Globe3Db(context);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }


    public entproject create_project(entproject entproject) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, entproject.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, entproject.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, entproject.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, entproject.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, entproject.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(entproject.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(entproject.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(entproject.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(entproject.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, entproject.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, entproject.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, entproject.companyfn);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, entproject.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, entproject.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, entproject.project_unique);
        long insertId = database.insert(Globe3Db.TABLE_PROJECT, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_PROJECT, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        entproject new_entproject = cursorToObject(cursor);
        cursor.close();
        return new_entproject;
    }


    public void delete_project(entproject entproject) {
        long id = entproject.idcode;
        database.delete(Globe3Db.TABLE_PROJECT, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_project_all() {
        database.delete(Globe3Db.TABLE_PROJECT, null, null);
    }

    public void update_project(entproject entproject) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, entproject.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, entproject.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, entproject.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, entproject.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, entproject.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(entproject.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(entproject.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(entproject.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(entproject.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, entproject.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, entproject.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, entproject.companyfn);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, entproject.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, entproject.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, entproject.project_unique);


        database.update(Globe3Db.TABLE_PROJECT, values, Globe3Db.COLUMN_IDCODE + " = " + entproject.idcode, null);
    }


    public ArrayList<entproject> get_active_projects() {
        ArrayList<entproject> entprojects = new ArrayList<entproject>();


        Cursor cursor = database.query(Globe3Db.TABLE_PROJECT, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y'", null, null, null, "project_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entproject entproject = cursorToObject(cursor);
            if(entproject.active_yn.equals("y")){
                entprojects.add(entproject);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return entprojects;
    }

    public ArrayList<entproject> search_project(String pSearch) {
        ArrayList<entproject> entprojects = new ArrayList<entproject>();


        Cursor cursor = database.query(Globe3Db.TABLE_PROJECT, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y' AND project_code LIKE '%" + pSearch.trim() + "%'", null, null, null, "project_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entproject entproject = cursorToObject(cursor);
            if(entproject.active_yn.equals("y")){
                entprojects.add(entproject);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return entprojects;
    }


    public entproject get_project(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_PROJECT, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    private entproject cursorToObject(Cursor cursor) {
        entproject entproject = new entproject();
        entproject.idcode = cursor.getLong(0);
        entproject.tag_table_usage = cursor.getString(1);
        entproject.sync_unique = cursor.getString(2);
        entproject.uniquenum_pri = cursor.getString(3);
        entproject.uniquenum_sec = cursor.getString(4);
        entproject.active_yn = cursor.getString(5);
        entproject.date_post = DateUtility.getStringDate(cursor.getString(6));
        entproject.date_submit = DateUtility.getStringDate(cursor.getString(7));
        entproject.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        entproject.date_sync = DateUtility.getStringDate(cursor.getString(9));
        entproject.userid_creator = cursor.getString(10);
        entproject.masterfn = cursor.getString(11);
        entproject.companyfn = cursor.getString(12);
        entproject.project_code = cursor.getString(13);
        entproject.project_name = cursor.getString(14);
        entproject.project_unique = cursor.getString(15);

        return entproject;
    }


}

