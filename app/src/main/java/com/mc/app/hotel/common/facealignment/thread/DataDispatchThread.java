package com.mc.app.hotel.common.facealignment.thread;


import android.util.Log;

import com.mc.app.hotel.common.facealignment.collection.DataPipe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gaofeng on 2017-02-20.
 */

public class DataDispatchThread extends Thread {

    private static final String TAG = DataDispatchThread.class.getSimpleName();
    private static final int DATA_PIPE_SIZE = 10;
    private static final long DATA_DISPATCH_INTERVAL_MS = 100;
    DataPipe dataPipe;
    Lock mutex;
    volatile boolean paused = false;
    volatile boolean destroyed=false;
    DrawPreviewFrameThread drawPreviewFrameThread;
    FaceDetectThread faceDetectThread;
    Lock methodMutex;

    public DataDispatchThread(DrawPreviewFrameThread drawPreviewFrameThread, FaceDetectThread faceDetectThread) {
        dataPipe = new DataPipe(DATA_PIPE_SIZE);
        mutex = new ReentrantLock();
        methodMutex = new ReentrantLock();
        paused = false;
        this.drawPreviewFrameThread = drawPreviewFrameThread;
        this.faceDetectThread = faceDetectThread;
    }

    @Override
    public void run() {
        long dataDispatchStartTime = 0;
        while (true) {
            mutex.lock();
            Object data = dataPipe.get(mutex);
            if (data == null) return;
            if (data instanceof DataPipe.DestroySignal) return;
            if (System.currentTimeMillis() - dataDispatchStartTime > DATA_DISPATCH_INTERVAL_MS) {
                dataDispatchStartTime = System.currentTimeMillis();
                byte[] imgBytes = (byte[]) data;
                byte[] faceDetectImgBytes = new byte[imgBytes.length];
                System.arraycopy(imgBytes, 0, faceDetectImgBytes, 0, imgBytes.length);
                drawPreviewFrameThread.submit(imgBytes);
                faceDetectThread.submit(faceDetectImgBytes);
            }
            mutex.unlock();
        }
    }

    public void submit(Object data) {
        if (methodMutex.tryLock()) {
            if (paused == false) {
                dataPipe.submit(data);
            }
            methodMutex.unlock();
        }
    }

    public void pauseThread() {
        methodMutex.lock();
        mutex.lock();
        paused = true;
        dataPipe.clear();
        mutex.unlock();
        methodMutex.unlock();
    }

    public void resumeThread() {
        methodMutex.lock();
        paused = false;
        methodMutex.unlock();
    }

    public void destroyThread() {
        if (destroyed) return;
        destroyed = true;
        methodMutex.lock();
        dataPipe.clear();
        dataPipe.submit(new DataPipe.DestroySignal());
        try {
            join();
        } catch (Exception e) {
            Log.e(TAG, "destroyThread: " + Log.getStackTraceString(e));
        }
        methodMutex.unlock();
    }
}
