package com.mc.app.hotel.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class SDCardUtils {

    /**
     * Log 输出标签
     */
    public static String TAG = "SDCardUtils";// InnerContacts.DEFAULT_TAG;
    /**
     * 根目录
     **/
    @SuppressLint("SdCardPath")
    public static final String MS_FILE = SDCardUtils.getEffectivePath() + "ms" + File.separator;
    /**
     * 缓存目录
     **/
    public static final String MS_CACHE = MS_FILE + "cache" + File.separator;
    /**
     * 临时存储目录
     **/
    public static final String MS_TEMP = MS_FILE + "temp" + File.separator;
    /**
     * 图片临时存储目录
     **/
    public static final String MS_TEMP_PIC = MS_TEMP + "IMG" + File.separator;

    public static final String MS_ERROR = MS_TEMP + "ERR" + File.separator;

    public static boolean isSDCardEnable() {
        boolean result = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return result;

    }

    public static String getSDCardPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        return path;
    }

    public static String getEffectivePath() {
        if (isSDCardEnable()) {
            return getSDCardPath();
        } else {
            return getRootDirectoryPath();
        }
    }

    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            @SuppressWarnings("deprecation")
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            @SuppressWarnings("deprecation")
            long freeBlocks = stat.getAvailableBlocks();
            long result = freeBlocks * availableBlocks;
            return result;
        }
        return 0;
    }

    public static String getRootDirectoryPath() {
        String path = Environment.getRootDirectory().getAbsolutePath();
        return path;
    }

    public static String getDatebaseDir(Context context) {
        return context.getDatabasePath("oma_db").getParent() + File.separator;
    }

    public static String getDatebasePath(Context context, String dbName) {
        return context.getDatabasePath(dbName).getAbsolutePath();
    }


    public static String getExternalFileDir(Context context) {
        return context.getExternalFilesDir("oma").getParent() + File.separator;
    }

    public static String getExternalCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

}
