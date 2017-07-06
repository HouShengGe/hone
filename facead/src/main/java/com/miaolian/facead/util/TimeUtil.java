package com.miaolian.facead.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gaofeng on 2017-04-25.
 */

public class TimeUtil {
    public static final String DATE_FROMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getDateStringFrom(long millisecond) {
        Date date = new Date(millisecond);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FROMAT);
        return dateFormat.format(date);
    }
}
