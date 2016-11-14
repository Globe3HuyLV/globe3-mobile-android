package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.globe3.tno.g3_mobile.model.entities.entity;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;

public class EntityRepo {

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
            Globe3Db.COLUMN_CO_CODE,
            Globe3Db.COLUMN_CO_NAME
    };



    public EntityRepo(Context context) {
        dbHelper = new Globe3Db(context);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }


    public entity create_entity(entity entity) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, entity.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, entity.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, entity.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, entity.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, entity.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(entity.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(entity.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(entity.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(entity.date_sync));
        values.put(Globe3Db.COLUMN_MASTERFN, entity.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, entity.companyfn);
        values.put(Globe3Db.COLUMN_CO_CODE, entity.co_code);
        values.put(Globe3Db.COLUMN_CO_NAME, entity.co_name);
        long insertId = database.insert(Globe3Db.TABLE_ENTITY, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_ENTITY, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        entity new_entity = cursorToObject(cursor);
        cursor.close();
        return new_entity;
    }



    public void delete_entity(entity entity) {
        long id = entity.idcode;
        database.delete(Globe3Db.TABLE_ENTITY, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_entity_all() {
        database.delete(Globe3Db.TABLE_ENTITY, null, null);
    }

    public void update_entity(entity entity) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, entity.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, entity.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, entity.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, entity.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, entity.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(entity.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(entity.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(entity.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(entity.date_sync));
        values.put(Globe3Db.COLUMN_MASTERFN, entity.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, entity.companyfn);
        values.put(Globe3Db.COLUMN_CO_CODE, entity.co_code);
        values.put(Globe3Db.COLUMN_CO_NAME, entity.co_name);


        database.update(Globe3Db.TABLE_ENTITY, values, Globe3Db.COLUMN_IDCODE + " = " + entity.idcode, null);
    }


    public ArrayList<entity> get_active_entitys() {
        ArrayList<entity> entitys = new ArrayList<entity>();

        Cursor cursor = database.query(Globe3Db.TABLE_ENTITY, allColumns, null, null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                entity entity = cursorToObject(cursor);
                if(entity.active_yn!=null && entity.active_yn.equals("y")){
                    entitys.add(entity);
                }
                cursor.moveToNext();
            }

            cursor.close();
        }

        return entitys;
    }

    public ArrayList<entity> get_user_entitys(String userCompanys) {
        ArrayList<entity> entitys = new ArrayList<entity>();

        Cursor cursor = database.query(Globe3Db.TABLE_ENTITY, allColumns, "uniquenum_pri IN ("+userCompanys+")", null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                entity entity = cursorToObject(cursor);
                if(entity.active_yn!=null && entity.active_yn.equals("y")){
                    entitys.add(entity);
                }
                cursor.moveToNext();
            }

            cursor.close();
        }

        return entitys;
    }

    public entity get_entity(String pUniquenum){
        Cursor cursor = database.query(Globe3Db.TABLE_ENTITY, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursorToObject(cursor);
        }else{
            return null;
        }
    }


    private entity cursorToObject(Cursor cursor) {
        entity entity = new entity();
        entity.idcode = cursor.getLong(0);
        entity.tag_table_usage = cursor.getString(1);
        entity.sync_unique = cursor.getString(2);
        entity.uniquenum_pri = cursor.getString(3);
        entity.uniquenum_sec = cursor.getString(4);
        entity.active_yn = cursor.getString(5);
        entity.date_post = DateUtility.getStringDate(cursor.getString(6));
        entity.date_submit = DateUtility.getStringDate(cursor.getString(7));
        entity.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        entity.date_sync = DateUtility.getStringDate(cursor.getString(9));
        entity.masterfn = cursor.getString(10);
        entity.companyfn = cursor.getString(11);
        entity.co_code = cursor.getString(12);
        entity.co_name = cursor.getString(13);


        return entity;
    }
}

