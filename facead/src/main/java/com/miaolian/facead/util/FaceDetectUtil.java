package com.miaolian.facead.util;

import android.graphics.Bitmap;
import android.media.FaceDetector;

/**
 * Created by gaofeng on 2017-02-17.
 */

public class FaceDetectUtil {
    private static int maxFaceNum = -1;
    private static int width = -1;
    private static int height = -1;
    private static FaceDetector.Face[] faces = null;
    private static FaceDetector faceDetector = null;

    private static void init(int width, int height, int maxFaceNum) {
        if ((faces == null || faceDetector == null) || (width != FaceDetectUtil.width || height != FaceDetectUtil.height || maxFaceNum != FaceDetectUtil.maxFaceNum)) {
            faces = new FaceDetector.Face[maxFaceNum];
            faceDetector = new FaceDetector(width, height, maxFaceNum);
            FaceDetectUtil.maxFaceNum = maxFaceNum;
            FaceDetectUtil.width = width;
            FaceDetectUtil.height = height;
        }
    }

    public static FaceDetector.Face[] detectFace(Bitmap bitmap, int maxFaceNum) {
        init(bitmap.getWidth(), bitmap.getHeight(), maxFaceNum);
        int foundFaceCount = faceDetector.findFaces(bitmap, faces);
        FaceDetector.Face[] foundFaces = new FaceDetector.Face[foundFaceCount];
        for (int i = 0; i < foundFaceCount; ++i) {
            foundFaces[i] = faces[i];
        }
        return foundFaces;
    }
}
