package com.mc.app.hotel.common.facealignment.util;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by gaofeng on 2017-04-26.
 */

public class FileUtil {
    public static void clearDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                clearDirectory(file);
            } else {
                file.delete();
            }
        }
    }

    public static void saveBytesOnDisk(byte[] bytes, File file) throws Exception {
        Exception exception = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
        } catch (Exception e) {
            exception = e;
        } finally {
            try {
                fos.close();
            } catch (Exception ex) {

            }
        }
        if (exception != null)
            throw exception;
    }

}
