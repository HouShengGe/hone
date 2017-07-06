package com.mc.app.hotel.common.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mc.app.hotel.BuildConfig;


/**
 * Toast 工具类
 *
 * @author KEZHUANG
 */
public class ToastUtils {
    private static final String TAG = "ToastUtils";

    /**
     * Toast 开关<br>
     * true为开启<br>
     * false为关闭
     */
    public static boolean SHOW_DEBUG = BuildConfig.DEBUG;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        Log.d(TAG, "showShort() called with: " + "context = [" + context + "], message = [" + message + "]");
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, duration).show();
    }


    }