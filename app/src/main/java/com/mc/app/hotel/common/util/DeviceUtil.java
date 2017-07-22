package com.mc.app.hotel.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.mc.app.hotel.common.App;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by admin on 2017/7/4.
 */

public class DeviceUtil {
    /**
     * Log的开关<br>
     * true为开启<br>
     * false为关闭<br>
     */
    public static boolean DEBUG = true;

    /**
     * Log 输出标签
     */
    public static String TAG = "AppUtils";// InnerContacts.DEFAULT_TAG;

    /**
     * 获得APP的label名字
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            String appName = context.getResources().getString(labelRes);
            return appName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序包名
     */
    public static String getPackageName(Context context) {
        if (context == null) {
            return null;
        }
        String pkgName = context.getPackageName();
        return pkgName;
    }

    @SuppressLint("NewApi")
    public static Drawable getIcon(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int icon = packageInfo.applicationInfo.icon;
            Drawable drawable = context.getResources().getDrawable(icon);
            return drawable;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取string.xml中的资源
     *
     * @param resId
     * @return
     */
    public static String getStringResources(int resId) {
        return App.getAppContext().getResources().getString(resId);
    }

    /**
     * 读取color.xml中的资源
     *
     * @param resId
     * @return
     */
    public static int getColorResources(int resId) {
        return App.getAppContext().getResources().getColor(resId);
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    @SuppressWarnings("static-access")
    public static String getEquInfo() {
        Build bd = new Build();
        String model = bd.MODEL;
        return model;
    }

    /**
     * 获取本机串号imei
     *
     * @return
     */
    public static String getEquDeviceId() {

        TelephonyManager tm = (TelephonyManager) App.getAppContext().getSystemService(App.getAppContext().TELEPHONY_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(App.getAppContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return tm.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }

    /**
     * 获取本机 设备序列号
     *
     * @return
     */
    public static String getEquSM() {
        String m_szAndroidID = Secure.getString(App.getAppContext().getContentResolver(), Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    /**
     * 判断是否为中文
     *
     * @return
     */
    public boolean isZh() {
        Locale locale = App.getAppContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    /**
     * 当前用的是什么语言
     *
     * @return
     */
    public String whatLanguage() {
        Locale locale = App.getAppContext().getResources().getConfiguration().locale;
        return locale.getLanguage();
    }

    public static String getGenderString(int sex) {

        if (sex == 0) return "未设置";
        if (sex == 1) return "男";
        if (sex == 2) return "女";

        return null;
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 获取程序 图标
     */
    public static Drawable getAppIcon(Context context, String packname) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packname, 0);
            return info.loadIcon(context.getPackageManager());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
