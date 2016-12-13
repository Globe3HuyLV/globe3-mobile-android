package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.model.entities.projectphoto;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;

public class ProjectPhotoRepo extends BaseRepo{
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
            Globe3Db.COLUMN_PROJECT_UNIQUE,
            Globe3Db.COLUMN_ROW_ITEM_NUM,
            Globe3Db.COLUMN_REFERENCE_NUM,
            Globe3Db.COLUMN_REMARKS,
            Globe3Db.COLUMN_PHOTO
    };



    public ProjectPhotoRepo(Context context) {
        super(context);
    }

    public ProjectPhotoRepo(BaseRepo baseRepo){
        super(baseRepo.database, baseRepo.db_helper);
    }

    public projectphoto create_projectphoto(projectphoto projectphoto) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, projectphoto.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, projectphoto.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, projectphoto.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, projectphoto.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, projectphoto.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(projectphoto.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(projectphoto.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(projectphoto.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(projectphoto.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, projectphoto.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, projectphoto.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, projectphoto.companyfn);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, projectphoto.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, projectphoto.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, projectphoto.project_unique);
        values.put(Globe3Db.COLUMN_ROW_ITEM_NUM, projectphoto.row_item_num);
        values.put(Globe3Db.COLUMN_REFERENCE_NUM, projectphoto.reference_num);
        values.put(Globe3Db.COLUMN_REMARKS, projectphoto.remarks);
        values.put(Globe3Db.COLUMN_PHOTO, projectphoto.photo);
        long insertId = database.insert(Globe3Db.TABLE_PROJECTPHOTO, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_PROJECTPHOTO, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        projectphoto new_projectphoto = cursorToObject(cursor);
        cursor.close();
        return new_projectphoto;
    }


    public void delete_projectphoto(projectphoto projectphoto) {
        long id = projectphoto.idcode;
        database.delete(Globe3Db.TABLE_PROJECTPHOTO, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_projectphoto_all() {
        database.delete(Globe3Db.TABLE_PROJECT, null, null);
    }

    public void update_projectphoto(projectphoto projectphoto) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, projectphoto.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, projectphoto.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, projectphoto.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, projectphoto.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, projectphoto.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(projectphoto.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(projectphoto.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(projectphoto.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(projectphoto.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, projectphoto.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, projectphoto.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, projectphoto.companyfn);
        values.put(Globe3Db.COLUMN_PROJECT_CODE, projectphoto.project_code);
        values.put(Globe3Db.COLUMN_PROJECT_NAME, projectphoto.project_name);
        values.put(Globe3Db.COLUMN_PROJECT_UNIQUE, projectphoto.project_unique);
        values.put(Globe3Db.COLUMN_ROW_ITEM_NUM, projectphoto.row_item_num);
        values.put(Globe3Db.COLUMN_REFERENCE_NUM, projectphoto.reference_num);
        values.put(Globe3Db.COLUMN_REMARKS, projectphoto.remarks);
        values.put(Globe3Db.COLUMN_PHOTO, projectphoto.photo);

        database.update(Globe3Db.TABLE_PROJECTPHOTO, values, Globe3Db.COLUMN_IDCODE + " = " + projectphoto.idcode, null);
    }


    public ArrayList<projectphoto> get_active_projectphoto() {
        ArrayList<projectphoto> entprojects = new ArrayList<>();


        Cursor cursor = database.query(Globe3Db.TABLE_PROJECTPHOTO, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y'", null, null, null, "row_item_num DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            projectphoto entproject = cursorToObject(cursor);
            if(entproject.active_yn.equals("y")){
                entprojects.add(entproject);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return entprojects;
    }

    public projectphoto get_projectphoto(String pUniquenumsec){

        Cursor cursor = database.query(Globe3Db.TABLE_PROJECTPHOTO, allColumns, "uniquenum_sec = '"+pUniquenumsec+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    public projectphoto get_projectphotos(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_PROJECTPHOTO, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    private projectphoto cursorToObject(Cursor cursor) {
        projectphoto projectphoto = new projectphoto();
        projectphoto.idcode = cursor.getLong(0);
        projectphoto.tag_table_usage = cursor.getString(1);
        projectphoto.sync_unique = cursor.getString(2);
        projectphoto.uniquenum_pri = cursor.getString(3);
        projectphoto.uniquenum_sec = cursor.getString(4);
        projectphoto.active_yn = cursor.getString(5);
        projectphoto.date_post = DateUtility.getStringDate(cursor.getString(6));
        projectphoto.date_submit = DateUtility.getStringDate(cursor.getString(7));
        projectphoto.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        projectphoto.date_sync = DateUtility.getStringDate(cursor.getString(9));
        projectphoto.userid_creator = cursor.getString(10);
        projectphoto.masterfn = cursor.getString(11);
        projectphoto.companyfn = cursor.getString(12);
        projectphoto.project_code = cursor.getString(13);
        projectphoto.project_name = cursor.getString(14);
        projectphoto.project_unique = cursor.getString(15);
        projectphoto.row_item_num = cursor.getInt(16);
        projectphoto.reference_num = cursor.getString(17);
        projectphoto.remarks = cursor.getString(18);
        projectphoto.photo = cursor.getBlob(19);

        return projectphoto;
    }


}

