package com.mc.app.hotel.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 内存卡 工具类<br>
 * 内部已经封装了打印功能,只需要把DEBUG参数改为true即可<br>
 * 如果需要更换tag可以直接更改,默认为KEZHUANG
 *
 * @author KEZHUANG
 */
public class SDCardUtils {

    /**
     * Log 输出标签
     */
    public static String TAG = "SDCardUtils";// InnerContacts.DEFAULT_TAG;
    /**
     * 根目录
     **/
    @SuppressLint("SdCardPath")
    public static final String OMA_FILE = SDCardUtils.getEffectivePath() + "oma" + File.separator;
    /**
     * 缓存目录
     **/
    public static final String OMA_CACHE = OMA_FILE + "cache" + File.separator;
    /**
     * 临时存储目录
     **/
    public static final String OMA_TEMP = OMA_FILE + "temp" + File.separator;
    /**
     * 图片临时存储目录
     **/
    public static final String OMA_TEMP_PIC = OMA_TEMP + "IMG" + File.separator;
    /**
     * 数据库路径
     **/
//    public static final String DB_PATH = "data/data/com.oma.org.cdt/databases/";

    public static final String OMA_ERROR = OMA_TEMP + "ERR" + File.separator;

    /**
     * 判断SDCard是否可用
     */
    public static boolean isSDCardEnable() {
        boolean result = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return result;

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        return path;
    }

    /**
     * 获取有效存储路径
     *
     * @return
     */
    public static String getEffectivePath() {
        if (isSDCardEnable()) {
            return getSDCardPath();
        } else {
            return getRootDirectoryPath();
        }
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     */
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

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        String path = Environment.getRootDirectory().getAbsolutePath();
        return path;
    }

    /**
     * @param
     * @author 宣志定
     * @modify 宣志定
     * @date 创建时间 2017/1/18
     * @date 修改时间 2017/1/18
     * @description 获取默认数据库父路径:"data/data/package/databases/"
     **/
    public static String getDatebaseDir(Context context) {
        return context.getDatabasePath("oma_db").getParent() + File.separator;
    }

    /**
     * @param
     * @author 宣志定
     * @modify 宣志定
     * @date 创建时间 2017/1/18
     * @date 修改时间 2017/1/18
     * @description 获取默认数据库路径:"data/data/package/databases/dbName"
     **/
    public static String getDatebasePath(Context context, String dbName) {
        return context.getDatabasePath(dbName).getAbsolutePath();
    }


    /**
     * @param
     * @author 宣志定
     * @modify 宣志定
     * @date 创建时间 2017/1/18
     * @date 修改时间 2017/1/18
     * @description 获取外部长期存储文件夹:"/storage/emulated/0/Android/data/com.oma.xzd.master_x/files/"
     **/
    public static String getExternalFileDir(Context context) {
        return context.getExternalFilesDir("oma").getParent() + File.separator;
    }

    /**
     * @param
     * @author 宣志定
     * @modify 宣志定
     * @date 创建时间 2017/1/18
     * @date 修改时间 2017/1/18
     * @description 获取外部缓存文件夹:"/storage/emulated/0/Android/data/com.oma.xzd.master_x/cache"
     **/
    public static String getExternalCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

}
