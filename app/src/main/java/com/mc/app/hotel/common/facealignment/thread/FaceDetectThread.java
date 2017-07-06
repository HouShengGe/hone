package com.mc.app.hotel.common.facealignment.thread;


import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.Log;
import android.view.TextureView;

import com.mc.app.hotel.common.facealignment.collection.DataPipe;
import com.mc.app.hotel.common.facealignment.util.BitmapUtil;
import com.mc.app.hotel.common.facealignment.util.FaceDetectUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import timber.log.Timber;

/**
 * Created by gaofeng on 2017-02-20.
 */

public class FaceDetectThread extends Thread {
    private static final String TAG = FaceDetectThread.class.getSimpleName();
    private static final int DATA_PIPE_SIZE = 10;
    private static final int MAX_FACE_NUM = 1;
    private static final long MAX_FACE_DETECT_TIME = 5000;
    DataPipe dataPipe;
    Lock mutex;
    volatile boolean paused = true;
    volatile boolean destroyed = false;
    TextureView textureView;
    int previewWidth;
    int previewHeight;
    FaceDetectThreadListener listener;
    PointF midPoint;
    int faceX;
    int faceY;
    int faceW;
    int faceH;
    double eyeDistance;
    long faceDetectStartTimestamp = 0;
    byte[] rgb565Buffer;
    Bitmap bitmap;
    Bitmap rotatedBitmap;

    public FaceDetectThread(TextureView textureView, int previewWidth, int previewHeight) {
        dataPipe = new DataPipe(DATA_PIPE_SIZE);
        mutex = new ReentrantLock();
        paused = false;
        this.textureView = textureView;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        midPoint = new PointF();
        rgb565Buffer = new byte[previewWidth * previewHeight * 2];
        bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.RGB_565);
    }

    public void setListener(FaceDetectThreadListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while (true) {
            mutex.lock();
            Object data = dataPipe.get(mutex);
            if (data == null) return;
            if (data instanceof DataPipe.DestroySignal) return;
            byte[] imgBytes = (byte[]) data;
            ///face detect from here
            try {
                BitmapUtil.convertNV21toRGB565(imgBytes, previewWidth, previewHeight, rgb565Buffer);
                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(rgb565Buffer));
                rotatedBitmap = BitmapUtil.rotate(bitmap, PrefUtil.getCameraRotateDegree());
                FaceDetector.Face[] foundFaces = FaceDetectUtil.detectFace(rotatedBitmap, MAX_FACE_NUM);
                if (foundFaces.length != 0) {
                    paused = true;
                    dataPipe.clear();
                    foundFaces[0].getMidPoint(midPoint);
                    eyeDistance = foundFaces[0].eyesDistance();
                    faceX = (int) Math.floor(midPoint.x - 2.5 * eyeDistance);
                    faceY = (int) Math.floor(midPoint.y - 2.5 * eyeDistance);
                    faceW = (int) Math.ceil(4.5 * eyeDistance);
                    faceH = (int) Math.ceil(4.5 * eyeDistance);
                    faceX = faceX < 0 ? 0 : faceX;
                    faceY = faceY < 0 ? 0 : faceY;
                    faceW = faceX + faceW > rotatedBitmap.getWidth() ? rotatedBitmap.getWidth() - faceX : faceW;
                    faceH = faceY + faceH > rotatedBitmap.getHeight() ? rotatedBitmap.getHeight() - faceY : faceH;
                    Bitmap faceBmp = Bitmap.createBitmap(rotatedBitmap, faceX, faceY, faceW, faceH);
                    if (listener != null) {
                        listener.onFoundFace(faceBmp);
                    }
                } else if (System.currentTimeMillis() - faceDetectStartTimestamp > MAX_FACE_DETECT_TIME) {
                    paused = true;
                    dataPipe.clear();
                    if (listener != null) {
                        listener.onFoundFace(null);
                    }
                }
            } catch (Exception e) {
                Timber.e("FaceDetect error:" + Log.getStackTraceString(e));
            }
            mutex.unlock();
        }
    }


    public void submit(Object data) {
        if (mutex.tryLock()) {
            if (paused == false) {
                dataPipe.submit(data);
            }
            mutex.unlock();
        }
    }

    public void pauseThread() {
        mutex.lock();
        paused = true;
        dataPipe.clear();
        mutex.unlock();
    }

    public void resumeThread() {
        mutex.lock();
        faceDetectStartTimestamp = System.currentTimeMillis();
        paused = false;
        mutex.unlock();
    }

    public void destroyThread() {
        if (destroyed) return;
        destroyed = true;
        mutex.lock();
        paused = true;
        dataPipe.clear();
        dataPipe.submit(new DataPipe.DestroySignal());
        mutex.unlock();
        try {
            join();
        } catch (Exception e) {
            Log.e(TAG, "destroyThread: " + Log.getStackTraceString(e));
        }
    }

    public interface FaceDetectThreadListener {
        void onFoundFace(Bitmap faceBmp);
    }
}
