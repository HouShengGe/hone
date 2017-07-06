package com.miaolian.facead.event;

import android.graphics.Bitmap;

import com.miaolian.facead.view.CameraFaceAlignmentFragment;

/**
 * Created by gaofeng on 2017-05-11.
 */

public class EventTakePhotoResponse {
    Bitmap bitmap;
    CameraFaceAlignmentFragment.PhotoType type;

    public EventTakePhotoResponse(Bitmap bitmap, CameraFaceAlignmentFragment.PhotoType type) {
        this.bitmap = bitmap;
        this.type = type;
    }

    public CameraFaceAlignmentFragment.PhotoType getType() {
        return type;
    }

    public void setType(CameraFaceAlignmentFragment.PhotoType type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
