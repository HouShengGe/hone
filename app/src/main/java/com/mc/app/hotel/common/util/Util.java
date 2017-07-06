package com.mc.app.hotel.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.mc.app.hotel.common.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/10/31.
 */
public class Util {

//    /**
//     * 初始化相册
//     * @param imagePicker
//     */
//    public static void initOptions(ImagePicker imagePicker) {
//        imagePicker.setMultiMode(false);
//        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
//        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
//        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
//    }

    /**
     * 封装上传图片
     * @param fileList
     * @return
     */
    public static List<MultipartBody.Part> getPicParts(List<File> fileList) {
        if (fileList == null || fileList.size() < 1)
            return null;
        MultipartBody.Builder builder = null;
        for (File file : fileList) {
            builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("img", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }
        return builder.build().parts();
    }

    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString() + File.separator;
    }

    /**
     * 获取缓存路径
     * @param context
     * @return
     */
    public static String getCacheDirPath() {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = App.getAppContext().getExternalCacheDir().getPath();
        } else {
            cachePath = App.getAppContext().getCacheDir().getPath();
        }
        return cachePath;
    }

    public static int getVersionCode() {
        Context context = App.getAppContext();
        PackageManager packageManager = context.getPackageManager();
        int version = 0;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static boolean saveResponseBody(ResponseBody body, String filePath) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static void openApkFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /*下载时，查看文件大小*/
    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }
}
