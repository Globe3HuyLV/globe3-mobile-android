package com.globe3.tno.g3_mobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.globe3.tno.g3_mobile.globals.Globals;
import com.globe3.tno.g3_mobile.model.entities.team;
import com.globe3.tno.g3_mobile.util.DateUtility;

import java.util.ArrayList;

public class TeamRepo extends BaseRepo{
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
            Globe3Db.COLUMN_TEAM_UNIQUE
    };



    public TeamRepo(Context context) {
        super(context);
    }

    public TeamRepo(BaseRepo baseRepo){
        super(baseRepo.database, baseRepo.db_helper);
    }

    public team create_team(team team) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, team.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, team.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, team.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, team.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, team.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(team.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(team.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(team.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(team.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, team.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, team.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, team.companyfn);
        values.put(Globe3Db.COLUMN_TEAM_CODE, team.team_code);
        values.put(Globe3Db.COLUMN_TEAM_NAME, team.team_name);
        values.put(Globe3Db.COLUMN_TEAM_UNIQUE, team.team_unique);
        long insertId = database.insert(Globe3Db.TABLE_TEAM, null, values);
        Cursor cursor = database.query(Globe3Db.TABLE_TEAM, allColumns, Globe3Db.COLUMN_IDCODE + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        team new_team = cursorToObject(cursor);
        cursor.close();
        return new_team;
    }


    public void delete_team(team team) {
        long id = team.idcode;
        database.delete(Globe3Db.TABLE_TEAM, Globe3Db.COLUMN_IDCODE + " = " + id, null);
    }

    public void delete_team_all() {
        database.delete(Globe3Db.TABLE_TEAM, null, null);
    }

    public void update_team(team team) {
        ContentValues values = new ContentValues();
        values.put(Globe3Db.COLUMN_TAG_TABLE_USAGE, team.tag_table_usage);
        values.put(Globe3Db.COLUMN_SYNC_UNIQUE, team.sync_unique);
        values.put(Globe3Db.COLUMN_UNIQUENUM_PRI, team.uniquenum_pri);
        values.put(Globe3Db.COLUMN_UNIQUENUM_SEC, team.uniquenum_sec);
        values.put(Globe3Db.COLUMN_ACTIVE_YN, team.active_yn);
        values.put(Globe3Db.COLUMN_DATE_POST, DateUtility.getDateString(team.date_post));
        values.put(Globe3Db.COLUMN_DATE_SUBMIT, DateUtility.getDateString(team.date_submit));
        values.put(Globe3Db.COLUMN_DATE_LASTUPDATE, DateUtility.getDateString(team.date_lastupdate));
        values.put(Globe3Db.COLUMN_DATE_SYNC, DateUtility.getDateString(team.date_sync));
        values.put(Globe3Db.COLUMN_USERID_CREATOR, team.userid_creator);
        values.put(Globe3Db.COLUMN_MASTERFN, team.masterfn);
        values.put(Globe3Db.COLUMN_COMPANYFN, team.companyfn);
        values.put(Globe3Db.COLUMN_TEAM_CODE, team.team_code);
        values.put(Globe3Db.COLUMN_TEAM_NAME, team.team_name);
        values.put(Globe3Db.COLUMN_TEAM_UNIQUE, team.team_unique);


        database.update(Globe3Db.TABLE_TEAM, values, Globe3Db.COLUMN_IDCODE + " = " + team.idcode, null);
    }


    public ArrayList<team> get_active_teams() {
        ArrayList<team> teams = new ArrayList<team>();


        Cursor cursor = database.query(Globe3Db.TABLE_TEAM, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y'", null, null, null, "team_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            team team = cursorToObject(cursor);
            if(team.active_yn.equals("y")){
                teams.add(team);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return teams;
    }

    public ArrayList<team> search_team(String pSearch) {
        ArrayList<team> teams = new ArrayList<team>();


        Cursor cursor = database.query(Globe3Db.TABLE_TEAM, allColumns, "companyfn = '" + Globals.COMPANYFN + "' AND active_yn = 'y' AND team_code LIKE '%" + pSearch.trim() + "%'", null, null, null, "team_code DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            team team = cursorToObject(cursor);
            if(team.active_yn.equals("y")){
                teams.add(team);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return teams;
    }


    public team get_team(String pUniquenum){

        Cursor cursor = database.query(Globe3Db.TABLE_TEAM, allColumns, "uniquenum_pri = '"+pUniquenum+"'", null, null, null, null);

        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    private team cursorToObject(Cursor cursor) {
        team team = new team();
        team.idcode = cursor.getLong(0);
        team.tag_table_usage = cursor.getString(1);
        team.sync_unique = cursor.getString(2);
        team.uniquenum_pri = cursor.getString(3);
        team.uniquenum_sec = cursor.getString(4);
        team.active_yn = cursor.getString(5);
        team.date_post = DateUtility.getStringDate(cursor.getString(6));
        team.date_submit = DateUtility.getStringDate(cursor.getString(7));
        team.date_lastupdate = DateUtility.getStringDate(cursor.getString(8));
        team.date_sync = DateUtility.getStringDate(cursor.getString(9));
        team.userid_creator = cursor.getString(10);
        team.masterfn = cursor.getString(11);
        team.companyfn = cursor.getString(12);
        team.team_code = cursor.getString(13);
        team.team_name = cursor.getString(14);
        team.team_unique = cursor.getString(15);

        return team;
    }
}

