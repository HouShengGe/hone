package com.mc.app.hotel.common.facealignment.model;

import android.graphics.Bitmap;

import com.mc.app.hotel.common.facealignment.util.FaceAlignmentUtil;

import java.nio.ByteBuffer;

/**
 * Created by gaofeng on 2017-02-22.
 */

public class Photo {
    public byte[] photoBytes;
    public FaceAlignmentUtil.PhotoType photoType;
    public String photoName;

    public Photo(byte[] photoBytes, FaceAlignmentUtil.PhotoType photoType) {
        this(photoBytes, photoType, null);
    }

    public Photo(Bitmap bitmap, FaceAlignmentUtil.PhotoType photoType, String photoName) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);
        byte[] photoBytes = buffer.array();
        this.photoBytes = photoBytes;
        this.photoType = photoType;
        this.photoName = photoName;
    }

    public Photo(byte[] photoBytes, FaceAlignmentUtil.PhotoType photoType, String photoName) {
        this.photoBytes = photoBytes;
        this.photoType = photoType;
        this.photoName = photoName;
    }
}
