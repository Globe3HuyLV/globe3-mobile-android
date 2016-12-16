package com.globe3.tno.g3_mobile.constants;

import android.os.Environment;

import com.globe3.tno.g3_mobile.R;

public class App {
    public static final String APP_NAME = "globe3-mobile";

    public static String GLOBE3_SERVER_EXT = "";
    public static String GLOBE3_SERVER_INT = "";

    public static String GLOBE3_WEBSERVICE_PATH = "rest/mobile/";
    public static String GLOBE3_WEBSERVICE_ADDR = GLOBE3_WEBSERVICE_PATH;
    public static final String GLOBE3_DIR_NAME = "globe3";
    public static final String GLOBE3_DIR = Environment.getExternalStorageDirectory().toString()+"/"+GLOBE3_DIR_NAME+"/";
    public static final String GLOBE3_TIMELOG_DIR = GLOBE3_DIR + "timelog/";
    public static final String GLOBE3_DATA_DIR = GLOBE3_DIR + "data/";
    public static final String GLOBE3_IMAGE_DIR = GLOBE3_DIR + "images/";
    public static final String GLOBE3_DB = GLOBE3_DIR + "db/globe3.db";

    public static int HTTP_READ_TIMEOUT = 20000;
    public static int HTTP_CONNECT_TIMEOUT = 30000;

    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_GPS = 2;
    public static final int REQUEST_CAMERA = 3;

    public static final int ACTIVITY_RESULT_SELECT_PHOTOS = 3;

    public static int[] FINGER_COUNTER = {R.drawable.ic_finger_one, R.drawable.ic_finger_one, R.drawable.ic_finger_two,  R.drawable.ic_finger_three,  R.drawable.ic_finger_four,  R.drawable.ic_finger_five};

    public static String WEB_SERVICE_PREFIX = "ws_";
}
