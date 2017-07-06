package com.mc.app.hotel.common.facealignment.util;


import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import timber.log.Timber;

/**
 * Created by gaofeng on 2017-02-16.
 */

public class CameraUtil {
    private static final String TAG = CameraUtil.class.getSimpleName();

    public enum CameraType {FRONT_CAMERA, BACK_CAMERA, ANYONE}


    public static Camera getAnyOneCamera() {
        Camera camera = null;
        camera = getFrontCamera();
        if (camera != null) {
            return camera;
        }
        camera = getBackCamera();
        if (camera != null) {
            return camera;
        }
        int numberOfCameras = Camera.getNumberOfCameras();
        Log.e(TAG, "numberOfCameras= " + numberOfCameras);

        for (int i = 0; i < numberOfCameras; i++) {
            try {
                camera = Camera.open(i);
                break;
            } catch (Exception e) {
                Log.e(TAG, "getCamera: " + e);
                camera = null;
            }
        }
        return camera;
    }

    public static Camera getFrontCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return Camera.open(i);
            }
        }
        return null;
    }

    public static Camera getBackCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return Camera.open(i);
            }
        }
        return null;
    }

    public static Camera getCamera(CameraType type, int targetWidth, int targetHeight, Camera.PreviewCallback previewCallback) throws Exception {
        Camera camera = null;
        switch (type) {
            case FRONT_CAMERA:
                camera = getFrontCamera();
                break;
            case BACK_CAMERA:
                camera = getBackCamera();
                break;
            case ANYONE:
                camera = getAnyOneCamera();
                break;
        }
        camera.setPreviewCallback(previewCallback);
        Camera.Parameters cameraParameters = camera.getParameters();
        cameraParameters.setPreviewFormat(ImageFormat.NV21);
        Pair<Integer, Integer> previewSize = getClosestAreaPreviewSize(cameraParameters.getSupportedPreviewSizes(), targetWidth, targetHeight);
        Timber.e("previewSize=" + previewSize.first + "," + previewSize.second);
        cameraParameters.setPreviewSize(previewSize.first, previewSize.second);
        camera.setParameters(cameraParameters);
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            camera.setParameters(parameters);
            Timber.e("getCamera:" + "设置对焦模式FOCUS_MODE_CONTINUOUS_VIDEO成功");
        } catch (Exception e) {
            Timber.e("getCamera:" + "设置对焦模式FOCUS_MODE_CONTINUOUS_VIDEO失败，尝试FOCUS_MODE_CONTINUOUS_PICTURE");
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(parameters);
                Timber.e("getCamera:" + "设置对焦模式FOCUS_MODE_CONTINUOUS_PICTURE成功");
            } catch (Exception ex) {
                Timber.e("getCamera:" + "设置对焦模式FOCUS_MODE_CONTINUOUS_PICTURE失败");
            }
        }
        return camera;
    }


    public static Pair<Integer, Integer> getClosetShapePreviewSize(List<Camera.Size> sizes, int width, int height) {
        float targetRatio = width / height;
        float minDiff = Float.MAX_VALUE;
        Camera.Size optimalSize = sizes.get(0);
        for (Camera.Size size : sizes) {
            Log.e(TAG, "getOptimalPreviewSize: " + "width=" + size.width + ",height=" + size.height);
            if (Math.abs((float) size.width / size.height - targetRatio) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs((float) size.width / size.height - targetRatio);
            }
        }
        return new Pair<Integer, Integer>(optimalSize.width, optimalSize.height);
    }

    public static Pair<Integer, Integer> getClosestAreaPreviewSize(List<Camera.Size> sizes, int width, int height) {
        float targetArea = width * height;
        float minDiff = Float.MAX_VALUE;
        float diff = 0;
        Camera.Size optimalSize = sizes.get(0);
        for (Camera.Size size : sizes) {
            diff = Math.abs((float) size.width * size.height - targetArea);
            if (diff < minDiff) {
                optimalSize = size;
                minDiff = diff;
            }
        }
        return new Pair<Integer, Integer>(optimalSize.width, optimalSize.height);
    }

    public static void destroyCamera(Camera camera) {
        try {
            camera.setPreviewCallback(null);
        } catch (Exception e) {
            Timber.e("destroyCamera:" + Log.getStackTraceString(e));
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {
            Timber.e("destroyCamera:" + Log.getStackTraceString(e));
        }
        try {
            camera.release();
        } catch (Exception e) {
            Timber.e("destroyCamera:" + Log.getStackTraceString(e));
        }
    }

}
