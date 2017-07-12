package com.mc.app.hotel.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/8.
 */

public class Constants {
    public static final String LIVING = "living";
    public static final String HISTORY = "history";
    public static final String ALL = "all";
    public static final String CUSTOMER_STATUS = "customer_status";
    public static final String MASTER_ID = "masterId";
    public static final String READ_ID_CARD = "readIDCard";
    public static final String FROM = "from";
    public static final String STORE_ID = "storeid";
    public static final String LONG_OUT = "longOut";
    public static final int LONG_OUT_FLAG = 3;

    public static List<String> getSexLset() {
        List<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        return list;
    }

}
