package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.model.entities.erpsalesorder;
import com.globe3.tno.g3_mobile.model.entities.tabledata;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;

public class SalesOrderRepo extends BaseRepo{
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
            Globe3Db.COLUMN_TEAM_CODE,
            Globe3Db.COLUMN_TEAM_NAME,
            Globe3Db.COLUMN_TEAM_UNIQUE,
            Globe3Db.COLUMN_SALES_ORDER_CODE,
            Globe3Db.COLUMN_SALES_ORDER_NAME,
            Globe3Db.COLUMN_SALES_ORDER_UNIQUE
    };

    public SalesOrderRepo(Context context) {
        super(context);
    }

    public erpsalesorder create_salesorder(erpsalesorder erpsalesorder) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, erpsalesorder.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, erpsalesorder.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, erpsalesorder.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, erpsalesorder.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, erpsalesorder.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(erpsalesorder.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(erpsalesorder.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(erpsalesorder.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(erpsalesorder.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, erpsalesorder.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, erpsalesorder.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, erpsalesorder.companyfn);
        values.put(Globe3Db.COLUMN_SALES_ORDER_CODE, erpsalesorder.salesorder_code);
        values.put(Globe3Db.COLUMN_SALES_ORDER_NAME, erpsalesorder.salesorder_name);
        values.put(Globe3Db.COLUMN_SALES_ORDER_UNIQUE, erpsalesorder.salesorder_unique);
        long insertId = database.insert(Globe3Db.TABLE_SALESORDER, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        erpsalesorder new_erpsalesorder = cursorToObject(cursor);
        cursor.close();
        return new_erpsalesorder;
    }


    public void delete_salesorder(erpsalesorder erpsalesorder) {
        long id = erpsalesorder.idcode;
        database.delete(Globe3Db.TABLE_SALESORDER, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_salesorder_all() {
        database.delete(Globe3Db.TABLE_SALESORDER, null, null);
    }

    public void update_salesorder(erpsalesorder erpsalesorder) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, erpsalesorder.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, erpsalesorder.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, erpsalesorder.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, erpsalesorder.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, erpsalesorder.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(erpsalesorder.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(erpsalesorder.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(erpsalesorder.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(erpsalesorder.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, erpsalesorder.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, erpsalesorder.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, erpsalesorder.companyfn);
        values.put(Globe3Db.COLUMN_SALES_ORDER_CODE, erpsalesorder.salesorder_code);
        values.put(Globe3Db.COLUMN_SALES_ORDER_NAME, erpsalesorder.salesorder_name);
        values.put(Globe3Db.COLUMN_SALES_ORDER_UNIQUE, erpsalesorder.salesorder_unique);


        database.update(Globe3Db.TABLE_SALESORDER, values, Globe3Db.COLUMN_IDCODE + " = " + erpsalesorder.idcode, null);
    }


    public ArrayList<erpsalesorder> get_active_salesorders() {
        ArrayList<erpsalesorder> erpsalesorders = new ArrayList<erpsalesorder>();


        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y'", null, null, null, "salesorder_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            erpsalesorder erpsalesorder = cursorToObject(cursor);
            if(erpsalesorder.active_yn.equals("y")){
                erpsalesorders.add(erpsalesorder);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return erpsalesorders;
    }

    public ArrayList<erpsalesorder> search_salesorder(String pSearch) {
        ArrayList<erpsalesorder> erpsalesorders = new ArrayList<erpsalesorder>();


        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y' AND salesorder_code LIKE '%" + pSearch.trim() + "%'", null, null, null, "salesorder_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            erpsalesorder erpsalesorder = cursorToObject(cursor);
            if(erpsalesorder.active_yn.equals("y")){
                erpsalesorders.add(erpsalesorder);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return erpsalesorders;
    }


    public erpsalesorder get_salesorder(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    public ArrayList<erpsalesorder> get_erpsalesorders(String param) {
        ArrayList<erpsalesorder> erpsalesorders = new ArrayList<>();

        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, param, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            erpsalesorders.add(cursorToObject(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return erpsalesorders;
    }

    private erpsalesorder cursorToObject(Cursor cursor) {
        erpsalesorder erpsalesorder = new erpsalesorder();
        erpsalesorder.idcode = cursor.getLong(0);
        erpsalesorder.tag_table_usage = cursor.getString(1);
        erpsalesorder.sync_unique = cursor.getString(2);
        erpsalesorder.uniquenum_pri = cursor.getString(3);
        erpsalesorder.uniquenum_sec = cursor.getString(4);
        erpsalesorder.active_yn = cursor.getString(5);
        erpsalesorder.date_post = DateUtility.getStringDate(cursor.getString(6));
        erpsalesorder.date_submit = DateUtility.getStringDate(cursor.getString(7));
        erpsalesorder.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        erpsalesorder.date_sync = DateUtility.getStringDate(cursor.getString(9));
        erpsalesorder.userid_creator = cursor.getString(10);
        erpsalesorder.masterfn = cursor.getString(11);
        erpsalesorder.companyfn = cursor.getString(12);
        erpsalesorder.salesorder_code = cursor.getString(16);
        erpsalesorder.salesorder_name = cursor.getString(17);
        erpsalesorder.salesorder_unique = cursor.getString(18);

        return erpsalesorder;
    }
}

