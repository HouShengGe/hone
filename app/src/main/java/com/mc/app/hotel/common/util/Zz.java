package com.mc.app.hotel.common.util;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/27.
 */
public class Zz {
    public static final String plateZZ = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
    public static final String emailZZ = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
    public static final String identityZZ = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    public static final String taiwanIDZZ = "^[A-Z][0-9]{9}$";
    public static final String xianggangIDZZ = "^[A-Z][0-9]{6}\\([0-9A]\\)$";
    public static final String aomenIDZZ = "^[157][0-9]{6}\\([0-9]\\)$";
    public static final String vinZZ = "^[a-zA-Z0-9]{17}$";
    public static final String mobileZZ = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";

    public static boolean zzPlateNumber(String plateNumber) {
        Pattern p = Pattern
                .compile(plateZZ);
        Matcher m = p.matcher(plateNumber);
        return m.matches();
    }

    public static boolean zzEmail(String email) {
        Pattern p = Pattern
                .compile(emailZZ);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean zzIdentity(String idNum) {
        Pattern p = Pattern
                .compile(identityZZ);
        Matcher m = p.matcher(idNum);
        return m.matches();
    }

    public static boolean zzMobile(@NonNull String mobile) {
        Pattern p = Pattern
                .compile(mobileZZ);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static boolean zzGATidentity(String idNum) {
        Pattern p1 = Pattern
                .compile(taiwanIDZZ);
        Pattern p2 = Pattern
                .compile(xianggangIDZZ);
        Pattern p3 = Pattern
                .compile(aomenIDZZ);
        Matcher m1 = p1.matcher(idNum);
        Matcher m2 = p2.matcher(idNum);
        Matcher m3 = p3.matcher(idNum);
        return m1.matches() || m2.matches() || m3.matches();
    }

    public static boolean zzVin(@NonNull String vin) {

        Pattern p = Pattern
                .compile(vinZZ);
        Matcher m = p.matcher(vin);
        return m.matches();

    }
}
