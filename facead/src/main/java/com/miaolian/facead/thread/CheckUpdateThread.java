package com.miaolian.facead.thread;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.miaolian.facead.R;
import com.miaolian.facead.model.CheckUpdateResponse;
import com.miaolian.facead.retrofit.RetrofitService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by gaofeng on 2017-04-19.
 */

public class CheckUpdateThread extends Thread {
    public static final long CHECK_UPDATE_INTERVAL_MS = 2 * 60 * 60 * 1000;
    Retrofit retrofit;
    RetrofitService retrofitService;
    volatile boolean stopWork;
    Lock mutex;
    Condition waitCondition;
    Context context;
    File appDownloadDir;
    Handler mainHandler;
    boolean firtstRun = true;

    public CheckUpdateThread(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://miaolian.mcomm.com.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mainHandler = new Handler(Looper.getMainLooper());
        retrofitService = retrofit.create(RetrofitService.class);
        mutex = new ReentrantLock();
        waitCondition = mutex.newCondition();
        stopWork = false;
        this.context = context;
        appDownloadDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MiaoLian/FaceAD");
    }

    @Override
    public void run() {
        MainLoop:
        while (true) {


            mutex.lock();
            if (stopWork) {
                mutex.unlock();
                return;
            }

            if (firtstRun == false) {
                try {
                    waitCondition.await(CHECK_UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Timber.e(Log.getStackTraceString(e));
                    try {
                        mutex.unlock();
                    } catch (Exception ex) {

                    }
                    stopWork = true;
                    return;
                }
            } else {
                firtstRun = false;
            }


            if (stopWork) {
                mutex.unlock();
                return;
            }
            mutex.unlock();

            try {
                clearAppDownloadDir();
                CheckUpdateResponse checkUpdateResponse = retrofitService.checkUpdate("ANDROID", 2).execute().body();
                long currentVersionCode = Long.valueOf(context.getString(R.string.versionCode));
                if (checkUpdateResponse.getVersionCode() > currentVersionCode) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, R.string.FIND_NEW_VERSION, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Response<ResponseBody> response = retrofitService.downloadNewApp("ANDROID", 2).execute();
                    if (response.isSuccessful() == false) {
                        response.body().close();
                        continue MainLoop;
                    }
                    ResponseBody responseBody = response.body();
                    long downloadLength = responseBody.contentLength();
                    if (checkUpdateResponse.getFileSize() != downloadLength) {
                        responseBody.close();
                        continue MainLoop;
                    } else {
                        File appFile = new File(appDownloadDir.getAbsolutePath() + "/App_" + System.currentTimeMillis() + ".apk");
                        int readSize = 0;
                        byte[] buffer = new byte[1024];
                        InputStream is = null;
                        FileOutputStream fos = null;
                        Exception downloadException = null;
                        long currentDownloadLength = 0;
                        try {
                            appFile.createNewFile();
                            is = responseBody.byteStream();
                            fos = new FileOutputStream(appFile, true);
                            while ((readSize = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, readSize);
                                currentDownloadLength += readSize;
                                Timber.e(context.getString(R.string.HAVE_DOWNLOADED) + currentDownloadLength + "/" + downloadLength);
                            }
                        } catch (Exception e) {
                            Timber.e("download exception:" + Log.getStackTraceString(e));
                            downloadException = e;
                        } finally {
                            try {
                                fos.flush();
                            } catch (Exception e) {
                            }
                            try {
                                fos.close();
                            } catch (Exception e) {
                            }
                            try {
                                is.close();
                            } catch (Exception e) {

                            }
                            try {
                                responseBody.close();
                            } catch (Exception e) {

                            }
                        }

                        if (downloadException == null && appFile.length() == downloadLength) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, R.string.DOWNLOAD_UPDATE_SUCCESS, Toast.LENGTH_SHORT).show();
                                }
                            });
                            installApp(appFile);
                        } else {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, R.string.DOWNLOAD_UPDATE_FAILED, Toast.LENGTH_SHORT).show();
                                }
                            });
                            Timber.e("run:" + Log.getStackTraceString(downloadException));
                            clearAppDownloadDir();
                        }
                    }
                }
            } catch (Exception e) {
                Timber.e("Check bind error:" + Log.getStackTraceString(e));
            }

        }

    }

    public void checkUpdateImmediately() {
        mutex.lock();
        waitCondition.signalAll();
        mutex.unlock();
    }

    public void shutdown() {
        mutex.lock();
        stopWork = true;
        waitCondition.signalAll();
        mutex.unlock();
    }

    private void clearAppDownloadDir() {
        if (appDownloadDir.exists()) {
            for (File file : appDownloadDir.listFiles()) {
                if (file.isDirectory() == false) {
                    file.delete();
                }
            }
        } else {
            appDownloadDir.mkdirs();
        }
    }

    private void installApp(File file) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(installIntent);
    }

}
