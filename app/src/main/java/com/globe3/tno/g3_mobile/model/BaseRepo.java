package com.globe3.tno.g3_mobile.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseRepo {
    public SQLiteDatabase database;
    public Globe3Db db_helper;

    public BaseRepo(Context context) {
        db_helper = new Globe3Db(context);
    }

    public BaseRepo(SQLiteDatabase database, Globe3Db db_helper){
        this.database = database;
        this.db_helper = db_helper;
    }

    public void open() throws SQLException {
        if(database==null || !database.isOpen()){
            database = db_helper.getWritableDatabase();
        }
    }

    public void close() {
        if(database!=null && database.isOpen()){
            db_helper.close();
        }
    }
}
