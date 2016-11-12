package com.globe3.tno.g3_mobile.util;

import java.util.Date;
import java.util.Random;

public class Uniquenum {
    public static String Generate(){
        Random rand = new Random();
        int i1 = rand.nextInt(999999999) + 1;

        //return "p" + DateUtility.getDateString(new Date(), "yymmddHHmmss") + String.valueOf(i1);
        return "p" + DateUtility.getDateString(new Date(), "yymmddHHmmss") + String.format("%09d", i1);
    }
}
