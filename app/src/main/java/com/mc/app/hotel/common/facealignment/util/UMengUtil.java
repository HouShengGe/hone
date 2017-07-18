package com.mc.app.hotel.common.facealignment.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by gaofeng on 2017-03-23.
 */

public class UMengUtil {
    private static final String EVENT_FACEALIGNMENT_SUCCESS = "EVENT_FACEALIGNMENT_SUCCESS";
    private static final String EVENT_FACEALIGNMENT_FAILED = "EVENT_FACEALIGNMENT_FAILED";
    private static final String EVENT_READIDCARD_SUCCESS = "EVENT_READIDCARD_SUCCESS";
    private static final String EVENT_READIDCARD_FAILED = "EVENT_READIDCARD_FAILED";
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String MESSAGE = "MESSAGE";

    private UMengUtil() {
    }

//    public static void faceAlignmentSuccess(Context context) {
//        Map<String, String> map = new HashMap<>();
//        map.put(DEVICE_ID, DeviceIdUtil.generateDeviceId(context));
//    }
//
//    public static void faceAlignemntFailed(Context context, String message) {
//        Map<String, String> map = new HashMap<>();
//        map.put(DEVICE_ID, DeviceIdUtil.generateDeviceId(context));
//        map.put(MESSAGE, message);
//    }
//
//    public static void readIDCardSuccess(Context context) {
//        Map<String, String> map = new HashMap<>();
//        map.put(DEVICE_ID, DeviceIdUtil.generateDeviceId(context));
//    }
//
//    public static void readIDCardFailed(Context context, String message) {
//        if (message.equals("未检测到身份证")) return;
//        Map<String, String> map = new HashMap<>();
//        map.put(DEVICE_ID, DeviceIdUtil.generateDeviceId(context));
//        map.put(MESSAGE, message);
//    }

    private static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String generateDebugID(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
