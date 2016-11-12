package com.globe3.tno.g3_mobile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getDateString(Date date){
        if(date!=null){
            return df.format(date);
        }else{
            return null;
        }
    }

    public static String getDateString(Date date, String format){
        if(date!=null){
            SimpleDateFormat newDf = new SimpleDateFormat(format);
            return newDf.format(date);
        }else{
            return null;
        }
    }

    public static Date getStringDate(String date_string){
        if(date_string!=null){
            try {
                return df.parse(date_string);
            } catch (ParseException e) {
                //e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

    public static Date getStringDate(String date_string, String format){
        if(date_string!=null){
            try {
                SimpleDateFormat newDf = new SimpleDateFormat(format);
                return newDf.parse(date_string);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }
}
