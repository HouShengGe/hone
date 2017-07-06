package com.mc.app.hotel.common.facealignment.thread;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;
import android.view.TextureView;

import com.mc.app.hotel.common.facealignment.adapter.CameraRotateAdapter;
import com.mc.app.hotel.common.facealignment.collection.DataPipe;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gaofeng on 2017-02-20.
 */

public class DrawPreviewFrameThread extends Thread {
    private static final String TAG = DrawPreviewFrameThread.class.getSimpleName();
    private static final int DATA_PIPE_SIZE = 10;
    DataPipe dataPipe;
    TextureView textureView;
    int previewWidth;
    int previewHeight;
    Context context;
    RenderScript rs;
    ScriptIntrinsicYuvToRGB scriptIntrinsicYuvToRGB;
    Bitmap bitmap;
    Allocation in;
    Allocation out;
    volatile boolean paused = false;
    volatile boolean destroyed = false;
    Lock mutex;

    public DrawPreviewFrameThread(Context context, TextureView textureView, int previewWidth, int previewHeight) {
        this.context = context;
        dataPipe = new DataPipe(DATA_PIPE_SIZE);
        mutex = new ReentrantLock();
        this.textureView = textureView;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        rs = RenderScript.create(context);
        scriptIntrinsicYuvToRGB = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(previewWidth * previewHeight * 3 / 2);
        in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);
        Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(previewWidth).setY(previewHeight);
        out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void run() {
        float textureViewWidth = 0;
        float textureViewHeight = 0;
        float bitmapNewWidth;
        float bitmapNewHeight;
        float scale = 1;
        float scaleX;
        float scaleY;
        float dx;
        float dy;
        Matrix matrix = new Matrix();
        while (true) {
            mutex.lock();
            textureViewWidth = textureView.getMeasuredWidth();
            textureViewHeight = textureView.getMeasuredHeight();
            Object data = dataPipe.get(mutex);
            if (data == null) return;
            if (data instanceof DataPipe.DestroySignal) return;
            byte[] imgBytes = (byte[]) data;
            ///draw frame from here
            try {
                in.copyFrom(imgBytes);
                scriptIntrinsicYuvToRGB.setInput(in);
                scriptIntrinsicYuvToRGB.forEach(out);
                out.copyTo(bitmap);
                switch (PrefUtil.getCameraRotateDegree()) {
                    case CameraRotateAdapter.ROTATE_0:
                        scaleX = textureViewWidth / bitmap.getWidth();
                        scaleY = textureViewHeight / bitmap.getHeight();
                        scale = scaleX > scaleY ? scaleX : scaleY;
                        bitmapNewWidth = bitmap.getWidth() * scale;
                        bitmapNewHeight = bitmap.getHeight() * scale;
                        dx = (textureViewWidth - bitmapNewWidth) / 2;
                        dy = (textureViewHeight - bitmapNewHeight) / 2;
                        matrix.reset();
                        matrix.postScale(scale, scale);
                        matrix.postTranslate(dx, dy);
                        break;
                    case CameraRotateAdapter.ROTATE_180:
                        scaleX = textureViewWidth / bitmap.getWidth();
                        scaleY = textureViewHeight / bitmap.getHeight();
                        scale = scaleX > scaleY ? scaleX : scaleY;
                        bitmapNewWidth = bitmap.getWidth() * scale;
                        bitmapNewHeight = bitmap.getHeight() * scale;
                        dx = (textureViewWidth - bitmapNewWidth) / 2 + bitmapNewWidth;
                        dy = (textureViewHeight - bitmapNewHeight) / 2 + bitmapNewHeight;
                        matrix.reset();
                        matrix.postScale(scale, scale);
                        matrix.postRotate(PrefUtil.getCameraRotateDegree(), 0, 0);
                        matrix.postTranslate(dx, dy);
                        break;
                    case CameraRotateAdapter.ROTATE_90:
                        scaleX = textureViewWidth / bitmap.getHeight();
                        scaleY = textureViewHeight / bitmap.getWidth();
                        scale = scaleX > scaleY ? scaleX : scaleY;
                        bitmapNewWidth = bitmap.getHeight() * scale;
                        bitmapNewHeight = bitmap.getWidth() * scale;
                        dx = (textureViewWidth - bitmapNewWidth) / 2 + bitmapNewWidth;
                        dy = (textureViewHeight - bitmapNewHeight) / 2;
                        matrix.reset();
                        matrix.postScale(scale, scale);
                        matrix.postRotate(PrefUtil.getCameraRotateDegree(), 0, 0);
                        matrix.postTranslate(dx, dy);
                        break;
                    case CameraRotateAdapter.ROTATE_270:
                        scaleX = textureViewWidth / bitmap.getHeight();
                        scaleY = textureViewHeight / bitmap.getWidth();
                        scale = scaleX > scaleY ? scaleX : scaleY;
                        bitmapNewWidth = bitmap.getHeight() * scale;
                        bitmapNewHeight = bitmap.getWidth() * scale;
                        dx = (textureViewWidth - bitmapNewWidth) / 2;
                        dy = (textureViewHeight - bitmapNewHeight) / 2 + bitmapNewHeight;
                        matrix.reset();
                        matrix.postScale(scale, scale);
                        matrix.postRotate(PrefUtil.getCameraRotateDegree(), 0, 0);
                        matrix.postTranslate(dx, dy);
                        break;
                }
                Canvas canvas = textureView.lockCanvas();
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap, matrix, null);
                textureView.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                Log.e(TAG, "run: " + Log.getStackTraceString(e));
            }
            mutex.unlock();
        }
    }

    public synchronized void submit(Object data) {
        if (paused) return;
        dataPipe.submit(data);
    }

    public synchronized void pauseThread() {
        mutex.lock();
        paused = true;
        dataPipe.clear();
        mutex.unlock();
    }

    public synchronized void resumeThread() {
        paused = false;
    }

    public synchronized void destroyThread() {
        if (destroyed) return;
        destroyed = true;
        dataPipe.clear();
        dataPipe.submit(new DataPipe.DestroySignal());
        try {
            join();
        } catch (Exception e) {
            Log.e(TAG, "destroyThread: " + Log.getStackTraceString(e));
        }
        in.destroy();
        out.destroy();
        scriptIntrinsicYuvToRGB.destroy();
        rs.destroy();
    }
}
