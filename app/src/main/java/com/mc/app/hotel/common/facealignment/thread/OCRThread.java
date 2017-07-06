package com.mc.app.hotel.common.facealignment.thread;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.caihua.cloud.common.entity.PersonInfo;
import com.mc.app.hotel.common.facealignment.collection.DataPipe;
import com.mc.app.hotel.common.facealignment.event.EventOCRFinished;
import com.mc.app.hotel.common.facealignment.util.OCRUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.ServiceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gaofeng on 2017-02-20.
 */

public class OCRThread extends Thread {
    private static final String TAG = OCRThread.class.getSimpleName();
    private static final int DATA_PIPE_SIZE = 10;
    private static final long OCR_RECOGNIZE_INTERVAL = 300;
    DataPipe dataPipe;
    Lock mutex;
    Condition waitCondition;
    volatile boolean shouldPause;
    volatile boolean shouldDestroy;
    volatile boolean paused;
    volatile boolean destroyed;
    int previewWidth;
    int previewHeight;
    Lock notifyMutex;
    Condition notifyWaitCondition;
    Lock methodMutex;
    Context context;
    long ocrRecognizeTimestamp = 0;

    public OCRThread(Context context, int previewWidth, int previewHeight) {
        dataPipe = new DataPipe(DATA_PIPE_SIZE);
        mutex = new ReentrantLock();
        waitCondition = mutex.newCondition();
        notifyMutex = new ReentrantLock();
        notifyWaitCondition = notifyMutex.newCondition();
        methodMutex = new ReentrantLock();
        shouldDestroy = false;
        shouldPause = false;
        paused = false;
        destroyed = false;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        this.context = context;
        EventBus.getDefault().register(this);
    }

    @Override
    public void run() {
        while (shouldDestroy == false) {
            mutex.lock();
            if (shouldPause) {
                try {
                    paused = true;
                    notifyMutex.lock();
                    notifyWaitCondition.signalAll();
                    notifyMutex.unlock();
                    waitCondition.await();
                    notifyMutex.lock();
                    paused = false;
                    notifyWaitCondition.signalAll();
                    notifyMutex.unlock();
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: " + Log.getStackTraceString(e));
                    return;
                }
            }
            mutex.unlock();
            Object data = dataPipe.get();
            if (data == null) return;
            if (data instanceof DataPipe.DummyData)
                continue;
            if (PrefUtil.getLinkType().equals(ServiceUtil.OCR) == false) continue;
            ///ocr read id card from here
            byte[] imgBytes = (byte[]) data;
            if (System.currentTimeMillis() - ocrRecognizeTimestamp > OCR_RECOGNIZE_INTERVAL) {
                OCRUtil.recognize(imgBytes, previewWidth, previewHeight);
                ocrRecognizeTimestamp = System.currentTimeMillis();
            }
        }
    }

    public void submit(Object data) {
        dataPipe.submit(data);
    }

    public void pauseThread() {
        methodMutex.lock();
        mutex.lock();
        if (paused || destroyed) {
            mutex.unlock();
            methodMutex.unlock();
            return;
        }
        mutex.unlock();
        notifyMutex.lock();
        shouldPause = true;
        dataPipe.submit(new DataPipe.DummyData());
        try {
            notifyWaitCondition.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "pauseThread: " + Log.getStackTraceString(e));
        }
        notifyMutex.unlock();
        methodMutex.unlock();
    }

    public void resumeThread() {
        methodMutex.lock();
        mutex.lock();
        if (paused == false || destroyed) {
            mutex.unlock();
            methodMutex.unlock();
            return;
        }
        shouldPause = false;
        waitCondition.signalAll();
        mutex.unlock();
        notifyMutex.lock();
        if (paused) {
            try {
                notifyWaitCondition.await();
            } catch (InterruptedException e) {
                notifyMutex.unlock();
                methodMutex.unlock();
                Log.e(TAG, "resumeThread: " + Log.getStackTraceString(e));
                return;
            }
        }
        notifyMutex.unlock();
        methodMutex.unlock();
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onOCRFinished(EventOCRFinished event) {
        if (event.isSuccess()) {
            PersonInfo personInfo = event.getPersonInfo();
            Intent resultIntent = new Intent(ServiceUtil.FINISH_READ_IDCARD);
            resultIntent.putExtra(ServiceUtil.READ_IDCARD_RESULT, true);
            resultIntent.putExtra(ServiceUtil.NAME, personInfo.getName());
            resultIntent.putExtra(ServiceUtil.SEX, personInfo.getSex());
            resultIntent.putExtra(ServiceUtil.NATION, personInfo.getNation());
            resultIntent.putExtra(ServiceUtil.BIRTHDAY, personInfo.getBirthday());
            resultIntent.putExtra(ServiceUtil.IDNUMBER, personInfo.getIdNumber());
            resultIntent.putExtra(ServiceUtil.ADDRESS, personInfo.getAddress());
            resultIntent.putExtra(ServiceUtil.TERM_BEGIN, personInfo.getTermBegin());
            resultIntent.putExtra(ServiceUtil.TERM_END, personInfo.getTermEnd());
            resultIntent.putExtra(ServiceUtil.ISSUE_AUTHORITY, personInfo.getIssueAuthority());
            resultIntent.putExtra(ServiceUtil.GUID, personInfo.getGuid());
            resultIntent.putExtra(ServiceUtil.PHOTO, personInfo.getPhoto());
            resultIntent.putExtra(ServiceUtil.FINGER_PRINT, personInfo.getFingerPrint());
            context.sendBroadcast(resultIntent);
        } else {
            Intent resultIntent = new Intent(ServiceUtil.FINISH_READ_IDCARD);
            resultIntent.putExtra(ServiceUtil.READ_IDCARD_RESULT, false);
            resultIntent.putExtra(ServiceUtil.ERROR_MESSAGE, event.getErrorMessage());
            context.sendBroadcast(resultIntent);
        }
    }

    public void destroyThread() {
        EventBus.getDefault().unregister(this);
        methodMutex.lock();
        shouldDestroy = true;
        dataPipe.clear();
        dataPipe.submit(new DataPipe.DummyData());
        mutex.lock();
        shouldPause = false;
        waitCondition.signalAll();
        mutex.unlock();
        try {
            join();
        } catch (Exception e) {
            Log.e(TAG, "destroyThread: " + Log.getStackTraceString(e));
        }
        destroyed = true;
        methodMutex.unlock();
    }
}
