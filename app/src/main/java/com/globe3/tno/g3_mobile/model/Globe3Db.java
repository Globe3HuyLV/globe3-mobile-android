package com.globe3.tno.g3_mobile.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.globe3.tno.g3_mobile.app_objects.LogItem;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_DB;

public class Globe3Db extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "globe3.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERACCESS = "useraccess";
    public static final String TABLE_ENTITY = "entity";
    public static final String TABLE_PROJECT = "entproject";
    public static final String TABLE_WDATA = "staffdata";
    public static final String TABLE_AUDITLOG = "auditlog";
    public static final String TABLE_SCANIMAGE = "scanimage";
    public static final String TABLE_DAILYTIME = "dailytime";
    public static final String TABLE_TABLEDATA = "tabledata";
    public static final String TABLE_PROJECTPHOTO = "projectphoto";
    public static final String TABLE_TEAM = "team";
    public static final String TABLE_SALESORDER = "salesorder";

    public static final String COLUMN_IDCODE = "idcode";
    public static final String COLUMN_TAG_TABLE_USAGE = "tag_table_usage";
    public static final String COLUMN_SYNC_UNIQUE = "sync_unique";
    public static final String COLUMN_UNIQUENUM_PRI = "uniquenum_pri";
    public static final String COLUMN_UNIQUENUM_SEC = "uniquenum_sec";
    public static final String COLUMN_ACTIVE_YN = "active_yn";
    public static final String COLUMN_DATE_POST = "date_post";
    public static final String COLUMN_DATE_SUBMIT = "date_submit";
    public static final String COLUMN_DATE_LASTUPDATE = "date_lastupdate";
    public static final String COLUMN_DATE_SYNC = "date_sync";
    public static final String COLUMN_CFSQLFILENAME = "cfsqlfilename";
    public static final String COLUMN_MASTERFN = "masterfn";
    public static final String COLUMN_COMPANYFN = "companyfn";
    public static final String COLUMN_COMPANYID = "companyid";
    public static final String COLUMN_USER_SPECIALNUM = "user_specialnum";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_DATE_VALID_TO = "date_valid_to";
    public static final String COLUMN_DATE_VALID_FROM = "date_valid_from";
    public static final String COLUMN_STAFF_CODE = "staff_code";
    public static final String COLUMN_STAFF_DESC = "staff_desc";
    public static final String COLUMN_STAFF_UNIQUE = "staff_unique";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_DEVICE_ID_IN = "device_id_in";
    public static final String COLUMN_DEVICE_ID_OUT = "device_id_out";
    public static final String COLUMN_CO_CODE = "co_code";
    public static final String COLUMN_CO_NAME = "co_name";
    public static final String COLUMN_USERID_CREATOR = "userid_creator";
    public static final String COLUMN_PROJECT_CODE = "project_code";
    public static final String COLUMN_PROJECT_NAME = "project_name";
    public static final String COLUMN_PROJECT_UNIQUE = "project_unique";
    public static final String COLUMN_OWN_WORKER_YN = "own_staff_yn";
    public static final String COLUMN_WORKER_TYPE = "staff_type";
    public static final String COLUMN_WORKER_GROUP = "staff_group";
    public static final String COLUMN_PAY_GROUP = "pay_group";
    public static final String COLUMN_NATIONALITY = "nationality";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_RACE = "race";
    public static final String COLUMN_RELIGION = "religion";
    public static final String COLUMN_JOB_TITLE = "job_title";
    public static final String COLUMN_WORKER_UNIQUE = "staff_unique";
    public static final String COLUMN_WORKER_ID = "staff_id";
    public static final String COLUMN_WORKER_FULLNAME = "staff_fullname";
    public static final String COLUMN_WORKPERMIT_TYPE = "workpermit_type";
    public static final String COLUMN_WORKPERMIT_NUM = "workpermit_num";
    public static final String COLUMN_WORKPERMIT_ISSUEDATE = "workpermit_issuedate";
    public static final String COLUMN_WORKPERMIT_EXPIRYDATE = "workpermit_expirydate";
    public static final String COLUMN_WORK_STARTDATE = "work_startdate";
    public static final String COLUMN_WORK_CEASEDATE = "work_ceasedate";
    public static final String COLUMN_SUPERVISOR_NAME = "supervisor_name";
    public static final String COLUMN_SUPERVISOR_UNIQUE = "supervisor_unique";
    public static final String COLUMN_VENDOR_NAME = "vendor_name";
    public static final String COLUMN_VENDOR_UNIQUE = "vendor_unique";
    public static final String COLUMN_DATE_BIRTH = "date_birth";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_PHOTO1 = "photo1";
    public static final String COLUMN_PHOTO2 = "photo2";
    public static final String COLUMN_FINGERPRINT_IMAGE1 = "fingerprint_image1";
    public static final String COLUMN_FINGERPRINT_IMAGE2 = "fingerprint_image2";
    public static final String COLUMN_FINGERPRINT_IMAGE3 = "fingerprint_image3";
    public static final String COLUMN_FINGERPRINT_IMAGE4 = "fingerprint_image4";
    public static final String COLUMN_FINGERPRINT_IMAGE5 = "fingerprint_image5";
    public static final String COLUMN_GPS_LOCATION = "gps_location";
    public static final String COLUMN_GPS_LOCATION_IN = "gps_location_in";
    public static final String COLUMN_GPS_LOCATION_OUT = "gps_location_out";
    public static final String COLUMN_NVAR25_01 = "nvar25_01";
    public static final String COLUMN_NVAR25_02 = "nvar25_02";
    public static final String COLUMN_NVAR25_03 = "nvar25_03";
    public static final String COLUMN_NVAR100_01 = "nvar100_01";
    public static final String COLUMN_NVAR100_02 = "nvar100_02";
    public static final String COLUMN_NVAR100_03 = "nvar100_03";
    public static final String COLUMN_DATE01 = "date01";
    public static final String COLUMN_DATE02 = "date02";
    public static final String COLUMN_NUM01 = "num01";
    public static final String COLUMN_NUM02 = "num02";
    public static final String COLUMN_DATE_REGISTER = "date_register";
    public static final String COLUMN_TAG_VOID_YN = "tag_void_yn";
    public static final String COLUMN_TAG_DELETED_YN = "tag_deleted_yn";
    public static final String COLUMN_TYPE_IN_OUT = "type_in_out";
    public static final String COLUMN_DATE_TIME_SCAN = "date_time_scan";
    public static final String COLUMN_DATE_TIME_IN = "date_time_in";
    public static final String COLUMN_DATE_TIME_OUT = "date_time_out";
    public static final String COLUMN_JOB_CODE = "job_code";
    public static final String COLUMN_JOB_UNIQUE = "job_unique";
    public static final String COLUMN_TASK_CODE = "task_code";
    public static final String COLUMN_TASK_UNIQUE = "task_unique";
    public static final String COLUMN_NVAR25_04 = "nvar25_04";
    public static final String COLUMN_NVAR25_05 = "nvar25_05";
    public static final String COLUMN_NVAR100_04 = "nvar100_04";
    public static final String COLUMN_NVAR100_05 = "nvar100_05";
    public static final String COLUMN_ROW_ITEM_NUM = "row_item_num";
    public static final String COLUMN_REFERENCE_NUM = "reference_num";
    public static final String COLUMN_REMARKS = "remarks";
    public static final String COLUMN_TEAM_CODE = "team_code";
    public static final String COLUMN_TEAM_NAME = "team_name";
    public static final String COLUMN_TEAM_UNIQUE = "team_unique";
    public static final String COLUMN_SALES_ORDER_CODE = "sales_order_code";
    public static final String COLUMN_SALES_ORDER_NAME = "sales_order_name";
    public static final String COLUMN_SALES_ORDER_UNIQUE = "sales_order_unique";

    private static final String TABLE_USERACCESS_CREATE =  "CREATE TABLE IF NOT EXISTS `useraccess` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`cfsqlfilename`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`companyid`TEXT," +
            "`user_specialnum`TEXT," +
            "`userid`TEXT," +
            "`password`TEXT," +
            "`date_valid_to`TEXT," +
            "`date_valid_from`TEXT," +
            "`staff_code`TEXT," +
            "`staff_desc`TEXT," +
            "`staff_unique`TEXT," +
            "`device_id`TEXT" +
            ");";

    private static final String TABLE_ENTITY_CREATE =  "CREATE TABLE IF NOT EXISTS `entity` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`co_code`TEXT," +
            "`co_name`TEXT" +
            ");";

    private static final String TABLE_PROJECT_CREATE =  "CREATE TABLE IF NOT EXISTS `entproject` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`project_code`TEXT," +
            "`project_name`TEXT," +
            "`project_unique`TEXT" +
            ");";

    private static final String TABLE_WDATA_CREATE =  "CREATE TABLE IF NOT EXISTS `staffdata` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`co_code`TEXT," +
            "`co_name`TEXT," +
            "`own_staff_yn`TEXT," +
            "`staff_group`TEXT," +
            "`pay_group`TEXT," +
            "`nationality`TEXT," +
            "`gender`TEXT," +
            "`race`TEXT," +
            "`religion`TEXT," +
            "`job_title`TEXT," +
            "`staff_unique`TEXT," +
            "`staff_id`TEXT," +
            "`staff_fullname`TEXT," +
            "`staff_type`TEXT," +
            "`workpermit_type`TEXT," +
            "`workpermit_num`TEXT," +
            "`workpermit_issuedate`TEXT," +
            "`workpermit_expirydate`TEXT," +
            "`work_startdate`TEXT," +
            "`work_ceasedate`TEXT," +
            "`supervisor_name`TEXT," +
            "`supervisor_unique`TEXT," +
            "`vendor_name`TEXT," +
            "`vendor_unique`TEXT," +
            "`date_birth`TEXT," +
            "`photo1`BLOB," +
            "`photo2`BLOB," +
            "`fingerprint_image1`BLOB," +
            "`fingerprint_image2`BLOB," +
            "`fingerprint_image3`BLOB," +
            "`fingerprint_image4`BLOB," +
            "`fingerprint_image5`BLOB," +
            "`project_code`TEXT," +
            "`project_name`TEXT," +
            "`project_unique`TEXT" +
            ");";

    private static final String TABLE_AUDITLOG_CREATE =  "CREATE TABLE IF NOT EXISTS `auditlog` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`userid`TEXT," +
            "`user_specialnum`TEXT," +
            "`gps_location`TEXT," +
            "`device_id`TEXT," +
            "`nvar25_01`TEXT," +
            "`nvar25_02`TEXT," +
            "`nvar25_03`TEXT," +
            "`nvar100_01`TEXT," +
            "`nvar100_02`TEXT," +
            "`nvar100_03`TEXT," +
            "`date01`TEXT," +
            "`date02`TEXT," +
            "`num01`NUMERIC," +
            "`num02`NUMERIC" +
            ");";

    private static final String TABLE_SCANIMAGE_CREATE =  "CREATE TABLE IF NOT EXISTS `scanimage` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`staff_unique`TEXT," +
            "`staff_id`TEXT," +
            "`staff_fullname`TEXT," +
            "`photo1`BLOB," +
            "`photo2`BLOB," +
            "`fingerprint_image1`BLOB," +
            "`fingerprint_image2`BLOB," +
            "`fingerprint_image3`BLOB," +
            "`fingerprint_image4`BLOB," +
            "`fingerprint_image5`BLOB," +
            "`date_register`TEXT," +
            "`gps_location`TEXT," +
            "`device_id`TEXT," +
            "`nvar25_01`TEXT," +
            "`nvar25_02`TEXT," +
            "`nvar25_03`TEXT," +
            "`nvar100_01`TEXT," +
            "`nvar100_02`TEXT," +
            "`nvar100_03`TEXT," +
            "`date01`TEXT," +
            "`date02`TEXT," +
            "`num01`NUMERIC," +
            "`num02`NUMERIC" +
            ");";

    private static final String TABLE_DAILYTIME_CREATE =  "CREATE TABLE IF NOT EXISTS `dailytime` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`tag_void_yn`TEXT," +
            "`tag_deleted_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_sync`TEXT," +
            "`date_lastupdate`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`staff_unique`TEXT," +
            "`staff_id`TEXT," +
            "`staff_fullname`TEXT," +
            "`type_in_out`TEXT," +
            "`date_time_in`TEXT," +
            "`date_time_out`TEXT," +
            "`gps_location_in`TEXT," +
            "`gps_location_out`TEXT," +
            "`device_id_in`TEXT," +
            "`device_id_out`TEXT," +
            "`project_code`TEXT," +
            "`project_name`TEXT," +
            "`project_unique`TEXT," +
            "`job_code`TEXT," +
            "`job_unique`TEXT," +
            "`task_code`TEXT," +
            "`task_unique`TEXT," +
            "`nvar25_01`TEXT," +
            "`nvar25_02`TEXT," +
            "`nvar25_03`TEXT," +
            "`nvar100_01`TEXT," +
            "`nvar100_02`TEXT," +
            "`nvar100_03`TEXT," +
            "`nvar100_04`TEXT," +
            "`date01`TEXT," +
            "`date02`TEXT," +
            "`num01`NUMERIC," +
            "`num02`NUMERIC" +
            ");";

    private static final String TABLE_TABLEDATA_CREATE =  "CREATE TABLE IF NOT EXISTS `tabledata` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`tag_void_yn`TEXT," +
            "`tag_deleted_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_sync`TEXT," +
            "`date_lastupdate`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`nvar25_01`TEXT," +
            "`nvar25_02`TEXT," +
            "`nvar25_03`TEXT," +
            "`nvar25_04`TEXT," +
            "`nvar25_05`TEXT," +
            "`nvar100_01`TEXT," +
            "`nvar100_02`TEXT," +
            "`nvar100_03`TEXT," +
            "`nvar100_04`TEXT," +
            "`nvar100_05`TEXT," +
            "`date01`TEXT," +
            "`date02`TEXT," +
            "`num01`NUMERIC," +
            "`num02`NUMERIC" +
            ");";

    private static final String TABLE_PROJECT_PHOTO_CREATE =  "CREATE TABLE IF NOT EXISTS `projectphoto` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`project_code`TEXT," +
            "`project_name`TEXT," +
            "`project_unique`TEXT," +
            "`row_item_num`NUMERIC," +
            "`reference_num`TEXT," +
            "`remarks`TEXT," +
            "`photo`BLOB" +
            ");";

    private static final String TABLE_TEAM_CREATE =  "CREATE TABLE IF NOT EXISTS `team` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`team_code`TEXT," +
            "`team_name`TEXT," +
            "`team_unique`TEXT" +
            ");";

    private static final String TABLE_SALES_ORDER_CREATE =  "CREATE TABLE IF NOT EXISTS `salesorder` (" +
            "`idcode`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`tag_table_usage`TEXT," +
            "`sync_unique`TEXT," +
            "`uniquenum_pri`TEXT," +
            "`uniquenum_sec`TEXT," +
            "`active_yn`TEXT," +
            "`date_post`TEXT," +
            "`date_submit`TEXT," +
            "`date_lastupdate`TEXT," +
            "`date_sync`TEXT," +
            "`userid_creator`TEXT," +
            "`masterfn`TEXT," +
            "`companyfn`TEXT," +
            "`team_code`TEXT," +
            "`team_name`TEXT," +
            "`team_unique`TEXT," +
            "`sales_order_code`TEXT," +
            "`sales_order_name`TEXT," +
            "`sales_order_unique`TEXT" +
            ");";

    public Globe3Db(Context context) {
        super(context, GLOBE3_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_USERACCESS_CREATE);
        database.execSQL(TABLE_ENTITY_CREATE);
        database.execSQL(TABLE_PROJECT_CREATE);
        database.execSQL(TABLE_WDATA_CREATE);
        database.execSQL(TABLE_AUDITLOG_CREATE);
        database.execSQL(TABLE_SCANIMAGE_CREATE);
        database.execSQL(TABLE_DAILYTIME_CREATE);
        database.execSQL(TABLE_TABLEDATA_CREATE);
        database.execSQL(TABLE_PROJECT_PHOTO_CREATE);
        database.execSQL(TABLE_TEAM_CREATE);
        database.execSQL(TABLE_SALES_ORDER_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERACCESS_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WDATA_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITLOG_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANIMAGE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILYTIME_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLEDATA_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_PHOTO_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_ORDER_CREATE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase database) {
        database.execSQL(TABLE_USERACCESS_CREATE);
        database.execSQL(TABLE_ENTITY_CREATE);
        database.execSQL(TABLE_PROJECT_CREATE);
        database.execSQL(TABLE_WDATA_CREATE);
        database.execSQL(TABLE_AUDITLOG_CREATE);
        database.execSQL(TABLE_SCANIMAGE_CREATE);
        database.execSQL(TABLE_DAILYTIME_CREATE);
        database.execSQL(TABLE_TABLEDATA_CREATE);
        database.execSQL(TABLE_PROJECT_PHOTO_CREATE);
        database.execSQL(TABLE_TEAM_CREATE);
        database.execSQL(TABLE_SALES_ORDER_CREATE);
    }
}
