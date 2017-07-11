package com.mc.app.hotel.common.util;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ArrayListUtils {

    public static String toStrings(@NonNull List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s + " ");
        }
        return sb.toString();
    }
}
