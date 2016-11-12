package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;

public class TabledataRepo {

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
            Globe3Db.COLUMN_NVAR25_01,
            Globe3Db.COLUMN_NVAR25_02,
            Globe3Db.COLUMN_NVAR25_03,
            Globe3Db.COLUMN_NVAR25_04,
            Globe3Db.COLUMN_NVAR25_05,
            Globe3Db.COLUMN_NVAR100_01,
            Globe3Db.COLUMN_NVAR100_02,
            Globe3Db.COLUMN_NVAR100_03,
            Globe3Db.COLUMN_NVAR100_04,
            Globe3Db.COLUMN_NVAR100_05,
            Globe3Db.COLUMN_DATE01,
            Globe3Db.COLUMN_DATE02,
            Globe3Db.COLUMN_NUM01,
            Globe3Db.COLUMN_NUM02
    };



    public TabledataRepo(Context context) {
        dbHelper = new Globe3Db(context);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }

    public tabledata create_tabledata(tabledata tabledata) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, tabledata.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, tabledata.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, tabledata.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, tabledata.uniquenum_sec);
        values.put(Globe3Db.COLUMN_TAG_VOID_YN, tabledata.tag_void_yn);
        values.put(Globe3Db.COLUMN_TAG_DELETED_YN, tabledata.tag_deleted_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(tabledata.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(tabledata.date_submit));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(tabledata.date_sync));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(tabledata.date_lastupdate));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, tabledata.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, tabledata.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, tabledata.companyfn);
        values.put(Globe3Db.COLUMN_NVAR25_01, tabledata.nvar25_01);
        values.put(Globe3Db.COLUMN_NVAR25_02, tabledata.nvar25_02);
        values.put(Globe3Db.COLUMN_NVAR25_03, tabledata.nvar25_03);
        values.put(Globe3Db.COLUMN_NVAR25_04, tabledata.nvar25_04);
        values.put(Globe3Db.COLUMN_NVAR25_05, tabledata.nvar25_05);
        values.put(Globe3Db.COLUMN_NVAR100_01, tabledata.nvar100_01);
        values.put(Globe3Db.COLUMN_NVAR100_02, tabledata.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, tabledata.nvar100_03);
        values.put(Globe3Db.COLUMN_NVAR100_04, tabledata.nvar100_04);
        values.put(Globe3Db.COLUMN_NVAR100_05, tabledata.nvar100_05);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(tabledata.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(tabledata.date02));
        values.put(Globe3Db.COLUMN_NUM01, tabledata.num01);
        values.put(Globe3Db.COLUMN_NUM02, tabledata.num02);
        long insertId = database.insert(Globe3Db.TABLE_BIOM_TABLE1, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_TABLE1, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        tabledata new_tabledata = cursorToObject(cursor);
        cursor.close();
        return new_tabledata;
    }


    public void delete_tabledata(tabledata tabledata) {
        long id = tabledata.idcode;
        database.delete(Globe3Db.TABLE_BIOM_TABLE1, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_tabledata_all() {
        database.delete(Globe3Db.TABLE_BIOM_TABLE1, null, null);
    }

    public void update_tabledata(tabledata tabledata) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, tabledata.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, tabledata.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, tabledata.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, tabledata.uniquenum_sec);
        values.put(Globe3Db.COLUMN_TAG_VOID_YN, tabledata.tag_void_yn);
        values.put(Globe3Db.COLUMN_TAG_DELETED_YN, tabledata.tag_deleted_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(tabledata.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(tabledata.date_submit));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(tabledata.date_sync));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(tabledata.date_lastupdate));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, tabledata.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, tabledata.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, tabledata.companyfn);
        values.put(Globe3Db.COLUMN_NVAR25_01, tabledata.nvar25_01);
        values.put(Globe3Db.COLUMN_NVAR25_02, tabledata.nvar25_02);
        values.put(Globe3Db.COLUMN_NVAR25_03, tabledata.nvar25_03);
        values.put(Globe3Db.COLUMN_NVAR25_04, tabledata.nvar25_04);
        values.put(Globe3Db.COLUMN_NVAR25_05, tabledata.nvar25_05);
        values.put(Globe3Db.COLUMN_NVAR100_01, tabledata.nvar100_01);
        values.put(Globe3Db.COLUMN_NVAR100_02, tabledata.nvar100_02);
        values.put(Globe3Db.COLUMN_NVAR100_03, tabledata.nvar100_03);
        values.put(Globe3Db.COLUMN_NVAR100_04, tabledata.nvar100_04);
        values.put(Globe3Db.COLUMN_NVAR100_05, tabledata.nvar100_05);
        values.put(Globe3Db.COLUMN_DATE01, DateUtility.getDateString(tabledata.date01));
        values.put(Globe3Db.COLUMN_DATE02, DateUtility.getDateString(tabledata.date02));
        values.put(Globe3Db.COLUMN_NUM01, tabledata.num01);
        values.put(Globe3Db.COLUMN_NUM02, tabledata.num02);


        database.update(Globe3Db.TABLE_BIOM_TABLE1, values, Globe3Db.COLUMN_IDCODE + " = " + tabledata.idcode, null);
    }


    public ArrayList<tabledata> get_active_tabledatas() {
        ArrayList<tabledata> tabledatas = new ArrayList<tabledata>();


        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_TABLE1, allColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tabledata tabledata = cursorToObject(cursor);
            if(tabledata.tag_deleted_yn.equals("n") && tabledata.tag_void_yn.equals("n")){
                tabledatas.add(tabledata);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return tabledatas;
    }

    public ArrayList<tabledata> get_tabledatas(String param) {
        ArrayList<tabledata> tabledatas = new ArrayList<tabledata>();

        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_TABLE1, allColumns, param, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tabledata tabledata = cursorToObject(cursor);
            tabledatas.add(tabledata);
            cursor.moveToNext();
        }

        cursor.close();
        return tabledatas;
    }


    public tabledata get_tabledata(String pUniquenum){


        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_TABLE1, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        tabledata tabledata = cursorToObject(cursor);

        cursor.close();

        return tabledata;
    }

    public tabledata get_tabledata_user_co_list(String pUniquenum){


        Cursor cursor = database.query(Globe3Db.TABLE_BIOM_TABLE1, allColumns, "uniquenum_sec = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();

        tabledata tabledata = cursorToObject(cursor);

        cursor.close();

        return tabledata;
    }

    private tabledata cursorToObject(Cursor cursor) {
        if(cursor.getCount()>0){
            tabledata tabledata = new tabledata();
            tabledata.idcode = cursor.getLong(0);
            tabledata.tag_table_usage = cursor.getString(1);
            tabledata.sync_unique = cursor.getString(2);
            tabledata.uniquenum_pri = cursor.getString(3);
            tabledata.uniquenum_sec = cursor.getString(4);
            tabledata.tag_void_yn = cursor.getString(5);
            tabledata.tag_deleted_yn = cursor.getString(6);
            tabledata.date_post = DateUtility.getStringDate(cursor.getString(7));
            tabledata.date_submit = DateUtility.getStringDate(cursor.getString(8));
            tabledata.date_sync = DateUtility.getStringDate(cursor.getString(9));
            tabledata.date_lastupdate = DateUtility.getStringDate(cursor.getString(10));
            tabledata.userid_creator = cursor.getString(11);
            tabledata.masterfn = cursor.getString(12);
            tabledata.companyfn = cursor.getString(13);
            tabledata.nvar25_01 = cursor.getString(14);
            tabledata.nvar25_02 = cursor.getString(15);
            tabledata.nvar25_03 = cursor.getString(16);
            tabledata.nvar25_04 = cursor.getString(17);
            tabledata.nvar25_05 = cursor.getString(18);
            tabledata.nvar100_01 = cursor.getString(19);
            tabledata.nvar100_02 = cursor.getString(20);
            tabledata.nvar100_03 = cursor.getString(21);
            tabledata.nvar100_04 = cursor.getString(22);
            tabledata.nvar100_05 = cursor.getString(23);
            tabledata.date01 = DateUtility.getStringDate(cursor.getString(24));
            tabledata.date02 = DateUtility.getStringDate(cursor.getString(25));
            tabledata.num01 = cursor.getFloat(26);
            tabledata.num02 = cursor.getFloat(27);

            return tabledata;
        }else{
            return null;
        }
    }
}

