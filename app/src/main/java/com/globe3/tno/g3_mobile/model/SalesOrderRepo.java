package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.model.entities.salesorder;
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

    public salesorder create_salesorder(salesorder salesorder) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, salesorder.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, salesorder.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, salesorder.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, salesorder.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, salesorder.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(salesorder.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(salesorder.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(salesorder.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(salesorder.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, salesorder.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, salesorder.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, salesorder.companyfn);
        values.put(Globe3Db.COLUMN_TEAM_CODE, salesorder.team_code);
        values.put(Globe3Db.COLUMN_TEAM_NAME, salesorder.team_name);
        values.put(Globe3Db.COLUMN_TEAM_UNIQUE, salesorder.team_unique);
        values.put(Globe3Db.COLUMN_SALES_ORDER_CODE, salesorder.salesorder_code);
        values.put(Globe3Db.COLUMN_SALES_ORDER_NAME, salesorder.salesorder_name);
        values.put(Globe3Db.COLUMN_SALES_ORDER_UNIQUE, salesorder.salesorder_unique);
        long insertId = database.insert(Globe3Db.TABLE_SALESORDER, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        salesorder new_salesorder = cursorToObject(cursor);
        cursor.close();
        return new_salesorder;
    }


    public void delete_salesorder(salesorder salesorder) {
        long id = salesorder.idcode;
        database.delete(Globe3Db.TABLE_SALESORDER, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_salesorder_all() {
        database.delete(Globe3Db.TABLE_SALESORDER, null, null);
    }

    public void update_salesorder(salesorder salesorder) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, salesorder.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, salesorder.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, salesorder.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, salesorder.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, salesorder.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(salesorder.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(salesorder.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(salesorder.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(salesorder.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, salesorder.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, salesorder.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, salesorder.companyfn);
        values.put(Globe3Db.COLUMN_TEAM_CODE, salesorder.team_code);
        values.put(Globe3Db.COLUMN_TEAM_NAME, salesorder.team_name);
        values.put(Globe3Db.COLUMN_TEAM_UNIQUE, salesorder.team_unique);
        values.put(Globe3Db.COLUMN_SALES_ORDER_CODE, salesorder.salesorder_code);
        values.put(Globe3Db.COLUMN_SALES_ORDER_NAME, salesorder.salesorder_name);
        values.put(Globe3Db.COLUMN_SALES_ORDER_UNIQUE, salesorder.salesorder_unique);


        database.update(Globe3Db.TABLE_SALESORDER, values, Globe3Db.COLUMN_IDCODE + " = " + salesorder.idcode, null);
    }


    public ArrayList<salesorder> get_active_salesorders() {
        ArrayList<salesorder> salesorders = new ArrayList<salesorder>();


        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y'", null, null, null, "salesorder_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            salesorder salesorder = cursorToObject(cursor);
            if(salesorder.active_yn.equals("y")){
                salesorders.add(salesorder);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return salesorders;
    }

    public ArrayList<salesorder> search_salesorder(String pSearch) {
        ArrayList<salesorder> salesorders = new ArrayList<salesorder>();


        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y' AND salesorder_code LIKE '%" + pSearch.trim() + "%'", null, null, null, "salesorder_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            salesorder salesorder = cursorToObject(cursor);
            if(salesorder.active_yn.equals("y")){
                salesorders.add(salesorder);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return salesorders;
    }


    public salesorder get_salesorder(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_SALESORDER, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    private salesorder cursorToObject(Cursor cursor) {
        salesorder salesorder = new salesorder();
        salesorder.idcode = cursor.getLong(0);
        salesorder.tag_table_usage = cursor.getString(1);
        salesorder.sync_unique = cursor.getString(2);
        salesorder.uniquenum_pri = cursor.getString(3);
        salesorder.uniquenum_sec = cursor.getString(4);
        salesorder.active_yn = cursor.getString(5);
        salesorder.date_post = DateUtility.getStringDate(cursor.getString(6));
        salesorder.date_submit = DateUtility.getStringDate(cursor.getString(7));
        salesorder.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        salesorder.date_sync = DateUtility.getStringDate(cursor.getString(9));
        salesorder.userid_creator = cursor.getString(10);
        salesorder.masterfn = cursor.getString(11);
        salesorder.companyfn = cursor.getString(12);
        salesorder.team_code = cursor.getString(13);
        salesorder.team_name = cursor.getString(14);
        salesorder.team_unique = cursor.getString(15);
        salesorder.salesorder_code = cursor.getString(16);
        salesorder.salesorder_name = cursor.getString(17);
        salesorder.salesorder_unique = cursor.getString(18);

        return salesorder;
    }
}

