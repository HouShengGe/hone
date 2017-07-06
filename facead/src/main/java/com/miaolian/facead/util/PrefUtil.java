package com.miaolian.facead.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by gaofeng on 2017-02-27.
 */

public class PrefUtil {
    private static SharedPreferences prefs = null;
    private static Context context;
    public static final String PREF_KEY_MIN_CONFIDENCE = "PREF_KEY_MIN_CONFIDENCE";
    public static final String PREF_KEY_USE_SPECIFIC_SERVER = "PREF_KEY_USE_SPECIFIC_SERVER";
    public static final String PREF_KEY_SPECIFIC_SERVER_ADDRESS = "PREF_KEY_SPECIFIC_SERVER_ADDRESS";
    public static final String PREF_KEY_SPECIFIC_SERVER_PORT = "PREF_KEY_SPECIFIC_SERVER_PORT";
    public static final String PREF_KEY_LINK_TYPE = "PREF_KEY_LINK_TYPE";
    public static final String PREF_KEY_CAMERA_TYPE = "PREF_KEY_CAMERA_TYPE";
    public static final String PREF_KEY_SERIALPORT_DEV_PATH = "PREF_KEY_SERIALPORT_DEV_PATH_KEY";
    public static final String PREF_KEY_SERIALPORT_BAUDRATE = "PREF_KEY_SERIALPORT_BAUDRATE";
    public static final String PREF_KEY_SERIALPORT_FLAG = "PREF_KEY_SERIALPORT_FLAG";
    public static final String PREF_KEY_CAMERA_ROTATE_DEGREE = "PREF_KEY_CAMERA_ROTATE_DEGREE";
    public static final String PREF_KEY_DEBUG_MODE = "PREF_KEY_DEBUG_MODE";
    public static final String PREF_KEY_USE_CAMERA_SLEEP = "PREF_KEY_USE_CAMERA_SLEEP";
    public static final String PREF_KEY_CAMERA_SLEEP_TIMEOUT_MS = "PREF_KEY_CAMERA_SLEEP_TIMEOUT_MS";
    public static final float DEFAULT_MIN_CONFIDENCE = 60;
    public static final long DEFAULT_CAMERA_SLEEP_TIMEOUT_MS = 60000;
    public static final boolean DEFAULT_USE_CAMERA_SLEEP = false;
    public static final boolean DEFAULT_USE_SPECIFIC_SERVER = false;
    public static final String DEFAULT_SERIALPORT_DEV_PATH = "/dev/ttyS2";
    public static final int DEFAULT_SERIALPORT_BAUDRATE = 115200;
    public static final int DEFAULT_SERIALPORT_FLAG = 0x400;
    public static final int DEFAULT_CAMERA_ROTATE_DEGREE = 0;
    public static final boolean DEFAULT_DEBUG_MODE = false;
    public static int cameraRotateDegreeCache = -1;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        PrefUtil.context = context;
        cameraRotateDegreeCache = prefs.getInt(PREF_KEY_CAMERA_ROTATE_DEGREE, DEFAULT_CAMERA_ROTATE_DEGREE);
    }

    public static void reset() {
        setMinConfidence(DEFAULT_MIN_CONFIDENCE);
        setUseSpecificServer(DEFAULT_USE_SPECIFIC_SERVER);
        setLinkType(ServiceUtil.OTG);
        setCameraType(CameraUtil.CameraType.FRONT_CAMERA);
        setSerialPortBaudRate(DEFAULT_SERIALPORT_BAUDRATE);
        setSerialPortDevPath(DEFAULT_SERIALPORT_DEV_PATH);
        setSerialPortFlag(DEFAULT_SERIALPORT_FLAG);
        setCameraRotateDegree(DEFAULT_CAMERA_ROTATE_DEGREE);
        setDebugMode(DEFAULT_DEBUG_MODE);
        setUseCameraSleep(DEFAULT_USE_CAMERA_SLEEP);
        setCameraSleepTimeoutMS(DEFAULT_CAMERA_SLEEP_TIMEOUT_MS);
    }


    public static void setDebugMode(boolean value) {
        prefs.edit().putBoolean(PREF_KEY_DEBUG_MODE, value).commit();
    }

    public static boolean getDebugMode() {
        return prefs.getBoolean(PREF_KEY_DEBUG_MODE, DEFAULT_DEBUG_MODE);
    }

    public static void setCameraRotateDegree(int degree) {
        cameraRotateDegreeCache = degree;
        prefs.edit().putInt(PREF_KEY_CAMERA_ROTATE_DEGREE, degree).commit();
    }

    public static int getCameraRotateDegree() {
        if (cameraRotateDegreeCache >= 0) {
            return cameraRotateDegreeCache;
        } else {
            cameraRotateDegreeCache = prefs.getInt(PREF_KEY_CAMERA_ROTATE_DEGREE, DEFAULT_CAMERA_ROTATE_DEGREE);
            return cameraRotateDegreeCache;
        }
    }

    public static void setUseCameraSleep(boolean value) {
        prefs.edit().putBoolean(PREF_KEY_USE_CAMERA_SLEEP, value).commit();
    }

    public static boolean getUseCameraSleep() {
        return prefs.getBoolean(PREF_KEY_USE_CAMERA_SLEEP, DEFAULT_USE_CAMERA_SLEEP);
    }

    public static void setCameraSleepTimeoutMS(long timeoutMS) {
        prefs.edit().putLong(PREF_KEY_CAMERA_SLEEP_TIMEOUT_MS, timeoutMS).commit();
    }

    public static long getCameraSleepTimeoutMS() {
        return prefs.getLong(PREF_KEY_CAMERA_SLEEP_TIMEOUT_MS, DEFAULT_CAMERA_SLEEP_TIMEOUT_MS);
    }

    public static void setSerialPortDevPath(String devPath) {
        prefs.edit().putString(PREF_KEY_SERIALPORT_DEV_PATH, devPath).commit();
    }

    public static String getSerialPortDevPath() {
        return prefs.getString(PREF_KEY_SERIALPORT_DEV_PATH, DEFAULT_SERIALPORT_DEV_PATH);
    }

    public static void setSerialPortBaudRate(int baudRate) {
        prefs.edit().putInt(PREF_KEY_SERIALPORT_BAUDRATE, baudRate).commit();
    }

    public static int getSerialPortBaudRate() {
        return prefs.getInt(PREF_KEY_SERIALPORT_BAUDRATE, DEFAULT_SERIALPORT_BAUDRATE);
    }

    public static void setSerialPortFlag(int flag) {
        prefs.edit().putInt(PREF_KEY_SERIALPORT_FLAG, flag).commit();
    }

    public static int getSerialPortFlag() {
        return prefs.getInt(PREF_KEY_SERIALPORT_FLAG, DEFAULT_SERIALPORT_FLAG);
    }

    public static void setCameraType(CameraUtil.CameraType cameraType) {
        prefs.edit().putString(PREF_KEY_CAMERA_TYPE, cameraType.toString()).commit();
    }

    public static CameraUtil.CameraType getCameraType() {
        String str = prefs.getString(PREF_KEY_CAMERA_TYPE, "");
        CameraUtil.CameraType cameraType;
        try {
            cameraType = CameraUtil.CameraType.valueOf(str);
        } catch (Exception e) {
            cameraType = CameraUtil.CameraType.ANYONE;
        }
        return cameraType;
    }

    public static void setMinConfidence(float confidence) {
        prefs.edit().putFloat(PREF_KEY_MIN_CONFIDENCE, confidence).commit();
    }

    public static float getMinConfidence() {
        return prefs.getFloat(PREF_KEY_MIN_CONFIDENCE, DEFAULT_MIN_CONFIDENCE);
    }

    public static void setUseSpecificServer(boolean value) {
        prefs.edit().putBoolean(PREF_KEY_USE_SPECIFIC_SERVER, value).commit();
    }

    public static boolean getUseSpecificServer() {
        return prefs.getBoolean(PREF_KEY_USE_SPECIFIC_SERVER, DEFAULT_USE_SPECIFIC_SERVER);
    }

    public static void setSpecificServerAddress(String address) {
        prefs.edit().putString(PREF_KEY_SPECIFIC_SERVER_ADDRESS, address).commit();
    }

    public static String getSpecificServerAddress() {
        return prefs.getString(PREF_KEY_SPECIFIC_SERVER_ADDRESS, "");
    }

    public static void setSpecificServerPort(int port) {
        prefs.edit().putInt(PREF_KEY_SPECIFIC_SERVER_PORT, port).commit();
    }

    public static int getSpecificServerPort() {
        return prefs.getInt(PREF_KEY_SPECIFIC_SERVER_PORT, -1);
    }

    public static void setLinkType(String type) {
        prefs.edit().putString(PREF_KEY_LINK_TYPE, type).commit();
    }

    public static String getLinkType() {
        return prefs.getString(PREF_KEY_LINK_TYPE, ServiceUtil.OTG);
    }

}
