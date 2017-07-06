package com.mc.app.hotel.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mc.app.hotel.bean.ReqBaseBean;
import com.mc.app.hotel.bean.UserInfo;

/**
 * Created by admin on 2017/7/4.
 */

public class SPerfUtil {
    private static SharedPreferences prefs = null;
    private static Context context;


    public static final String DEFAULT_STRING = "";
    public static final int DEFAULT_INT = -1;
    public static final String PREF_KEY_TOKEN = "PREF_KEY_TOKEN";
    public static final String PREF_KEY_KEY = "PREF_KEY_KEY";
    public static final String PREF_KEY_USER_ID = "PREF_KEY_USER_ID";

    public static final String PREF_KEY_USER_NO = "PREF_KEY_USER_ID";
    public static final String PREF_KEY_USER_NAME = "PREF_KEY_USER_ID";
    public static final String PREF_KEY_USER_MOBILE = "PREF_KEY_USER_ID";
    public static final String PREF_KEY_USER_STORE_ID = "PREF_KEY_USER_ID";
    public static final String PREF_KEY_USER_TYPE = "PREF_KEY_USER_ID";


    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SPerfUtil.context = context;
    }

    public static void reset() {
        setReqBaseInfo(DEFAULT_STRING, DEFAULT_STRING, DEFAULT_INT);
        setUserInfo(new UserInfo());
    }

    public static void setReqBaseInfo(String token, String key, int userID) {
        prefs.edit().putString(PREF_KEY_TOKEN, token).commit();
        prefs.edit().putString(PREF_KEY_KEY, key).commit();
        prefs.edit().putInt(PREF_KEY_USER_ID, userID).commit();
    }

    public static ReqBaseBean getReqBaseInfo() {
        ReqBaseBean baseBean = new ReqBaseBean();
        baseBean.setToken(prefs.getString(PREF_KEY_TOKEN, DEFAULT_STRING));
        baseBean.setKey(prefs.getString(PREF_KEY_KEY, DEFAULT_STRING));
        baseBean.setUserId(prefs.getInt(PREF_KEY_USER_ID, DEFAULT_INT));
        baseBean.setAppId(DeviceUtil.getEquDeviceId());
        return baseBean;
    }

    public static void setUserInfo(UserInfo userInfo) {
        prefs.edit().putInt(PREF_KEY_USER_ID, userInfo.getUserid()).commit();
        prefs.edit().putString(PREF_KEY_USER_NO, userInfo.getUserNo()).commit();
        prefs.edit().putString(PREF_KEY_USER_NAME, userInfo.getUsername()).commit();
        prefs.edit().putString(PREF_KEY_USER_MOBILE, userInfo.getStrMobile()).commit();
        prefs.edit().putString(PREF_KEY_USER_STORE_ID, userInfo.getStoreId()).commit();
        prefs.edit().putInt(PREF_KEY_USER_TYPE, userInfo.getUserType()).commit();
    }

    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setStoreId(prefs.getString(PREF_KEY_USER_STORE_ID, DEFAULT_STRING));
        userInfo.setStrMobile(prefs.getString(PREF_KEY_USER_MOBILE, DEFAULT_STRING));
        userInfo.setUserid(prefs.getInt(PREF_KEY_USER_ID, DEFAULT_INT));
        userInfo.setUsername(prefs.getString(PREF_KEY_USER_NAME, DEFAULT_STRING));
        userInfo.setUserNo(prefs.getString(PREF_KEY_USER_NO, DEFAULT_STRING));
        userInfo.setUserType(prefs.getInt(PREF_KEY_USER_TYPE, DEFAULT_INT));
        return userInfo;
    }

    public static boolean isLogin() {
        UserInfo user = getUserInfo();
        return (user.getUserid() != -1);
    }


}
