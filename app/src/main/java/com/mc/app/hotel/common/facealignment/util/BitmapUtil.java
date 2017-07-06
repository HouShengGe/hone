package com.mc.app.hotel.common.facealignment.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by gaofeng on 2017-03-13.
 */

public class BitmapUtil {

    public static Bitmap rotate(Bitmap bitmap, int angle) {
        if (angle == 0) return bitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap resize(Bitmap originalBmp, int maxWidth, int maxHeight) {
        int originalWidth = originalBmp.getWidth();
        int originalHeight = originalBmp.getHeight();
        float xScaleFactor = (float) maxWidth / originalWidth;
        float yScaleFactor = (float) maxHeight / originalHeight;
        float scaleFactor = xScaleFactor > yScaleFactor ? yScaleFactor : xScaleFactor;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);
        return originalBmp.createBitmap(originalBmp, 0, 0, originalWidth, originalHeight, matrix, false);
    }

    public static byte[] compress(Bitmap oringalBmp, Bitmap.CompressFormat format, int level) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        oringalBmp.compress(format, level, baos);
        return baos.toByteArray();
    }


    public static void convertNV21toRGB565(byte[] yuvs, int width, int height, byte[] rgbs) {
        //the end of the luminance data
        final int lumEnd = width * height;
        //points to the next luminance value pair
        int lumPtr = 0;
        //points to the next chromiance value pair
        int chrPtr = lumEnd;
        //points to the next byte output pair of RGB565 value
        int outPtr = 0;
        //the end of the current luminance scanline
        int lineEnd = width;

        while (true) {

            //skip back to the start of the chromiance values when necessary
            if (lumPtr == lineEnd) {
                if (lumPtr == lumEnd) break; //we've reached the end
                //division here is a bit expensive, but's only done once per scanline
                chrPtr = lumEnd + ((lumPtr >> 1) / width) * width;
                lineEnd += width;
            }

            //read the luminance and chromiance values
            final int Y1 = yuvs[lumPtr++] & 0xff;
            final int Y2 = yuvs[lumPtr++] & 0xff;
            final int Cr = (yuvs[chrPtr++] & 0xff) - 128;
            final int Cb = (yuvs[chrPtr++] & 0xff) - 128;
            int R, G, B;

            //generate first RGB components
            B = Y1 + ((454 * Cb) >> 8);
            if (B < 0) B = 0;
            else if (B > 255) B = 255;
            G = Y1 - ((88 * Cb + 183 * Cr) >> 8);
            if (G < 0) G = 0;
            else if (G > 255) G = 255;
            R = Y1 + ((359 * Cr) >> 8);
            if (R < 0) R = 0;
            else if (R > 255) R = 255;
            //NOTE: this assume little-endian encoding
            rgbs[outPtr++] = (byte) (((G & 0x3c) << 3) | (B >> 3));
            rgbs[outPtr++] = (byte) ((R & 0xf8) | (G >> 5));

            //generate second RGB components
            B = Y2 + ((454 * Cb) >> 8);
            if (B < 0) B = 0;
            else if (B > 255) B = 255;
            G = Y2 - ((88 * Cb + 183 * Cr) >> 8);
            if (G < 0) G = 0;
            else if (G > 255) G = 255;
            R = Y2 + ((359 * Cr) >> 8);
            if (R < 0) R = 0;
            else if (R > 255) R = 255;
            //NOTE: this assume little-endian encoding
            rgbs[outPtr++] = (byte) (((G & 0x3c) << 3) | (B >> 3));
            rgbs[outPtr++] = (byte) ((R & 0xf8) | (G >> 5));
        }
    }

    private static final String TAG = "BitmapUtil";
    /**
     * 图片保存本地
     *
     * @param bitmap
     * @param path
     * @param filename
     * @param type     1：直接保存为MD5名
     * @return
     */
    public static String savePicInLocal(Bitmap bitmap, String path, String filename, int type) {
        String string = "";
        // type=1代表文件直接保存为md5了
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 68, baos);
            byte[] byteArray = baos.toByteArray();
            filename = type == 1 ? FileMD5.getMD5(byteArray) : filename;
            string = filename + ".png";
            File filDir = new File(path);
            if (!filDir.exists()) {
                filDir.mkdirs();
            }
            File file = new File(path, string);
            if (file.exists()) {
                deleteTemp(path + filename);
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
        } catch (Exception e) {
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return string;
    }

    /**
     * 递归删除文件
     *
     * @param path
     */
    public static void deleteTemp(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteTemp(childFiles[i].getAbsolutePath());
            }
            file.delete();
        }
    }
}
