package com.mc.app.hotel.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mc.app.hotel.bean.NationBean;
import com.mc.app.hotel.bean.ReqBaseBean;
import com.mc.app.hotel.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

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

    public static final String PREF_KEY_USER_NO = "PREF_KEY_USER_NO";
    public static final String PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME";
    public static final String PREF_KEY_USER_MOBILE = "PREF_KEY_USER_MOBILE";
    public static final String PREF_KEY_USER_STORE_ID = "PREF_KEY_USER_STORE_ID";
    public static final String PREF_KEY_USER_TYPE = "PREF_KEY_USER_TYPE";
    public static final String PREF_KEY_NATIONS = "PREF_KEY_NATIONS";
    public static final String TOAST_X = "TOAST_X";
    public static final String TOAST_Y = "TOAST_Y";


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
        prefs.edit().putInt(PREF_KEY_USER_STORE_ID, userInfo.getStoreId()).commit();
        prefs.edit().putInt(PREF_KEY_USER_TYPE, userInfo.getUserType()).commit();
    }

    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setStoreId(prefs.getInt(PREF_KEY_USER_STORE_ID, DEFAULT_INT));
        userInfo.setStrMobile(prefs.getString(PREF_KEY_USER_MOBILE, DEFAULT_STRING));
        userInfo.setUserid(prefs.getInt(PREF_KEY_USER_ID, DEFAULT_INT));
        userInfo.setUsername(prefs.getString(PREF_KEY_USER_NAME, DEFAULT_STRING));
        userInfo.setUserNo(prefs.getString(PREF_KEY_USER_NO, DEFAULT_STRING));
        userInfo.setUserType(prefs.getInt(PREF_KEY_USER_TYPE, DEFAULT_INT));
        return userInfo;
    }

    public static boolean isLogin() {
        UserInfo user = getUserInfo();
        ReqBaseBean bean = getReqBaseInfo();
        return (user.getUserid() != -1 && !bean.getToken().equals(""));
    }

    public static void saveNation(List<NationBean> list) {
        Gson gson = new Gson();
        String nations = gson.toJson(list);
        prefs.edit().putString(PREF_KEY_NATIONS, nations).commit();
    }

    public static List<String> readNation() {
        Gson gson = new Gson();
        List<NationBean> list = gson.fromJson(prefs.getString(PREF_KEY_NATIONS, DEFAULT_STRING), new TypeToken<List<NationBean>>() {
        }.getType());
        List<String> list1 = new ArrayList<>();
        for (NationBean bean : list) {
            list1.add(bean.getNation());
        }
        return list1;
    }

    public static void saveToastX(int x) {
        prefs.edit().putInt(TOAST_X, x).commit();
    }

    public static void saveToastY(int y) {
        prefs.edit().putInt(TOAST_Y, y).commit();
    }

    public static int readToastX() {
        return prefs.getInt(TOAST_X, DEFAULT_INT);
    }

    public static int readToastY() {
        return prefs.getInt(TOAST_Y, DEFAULT_INT);
    }
}
