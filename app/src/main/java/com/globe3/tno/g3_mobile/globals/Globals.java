package com.globe3.tno.g3_mobile.globals;

import com.globe3.tno.g3_mobile.app_objects.GPSLocation;
import com.globe3.tno.g3_mobile.util.GPSUtility;
import com.neurotec.biometrics.client.NBiometricClient;

public class Globals {
    public static String CFSQLFILENAME = "";
    public static String MASTERFN = "";
    public static String COMPANYFN = "";
    public static String COMPANY_NAME = "";
    public static String USERLOGINUNIQ = "";
    public static String USERLOGINID = "";
    public static String PASSWORD = "";

    public static String MAC = "";

    public static String DEVICE_ID = "";
    public static String DEVICE_MODEL = "";
    public static String DEVICE_NAME = "";
    public static String PHONE_NUMBER = "";

    public static NBiometricClient Globe3BiometricClient;

    public static GPSUtility mGPSUtility;
    public static GPSLocation mGPSLocation;

    public static boolean EXTRACT_LICENSE_OBTAINED = false;
    public static boolean MATCHER_LICENSE_OBTAINED = false;
    public static boolean DEVICES_LICENSE_OBTAINED = false;
}
