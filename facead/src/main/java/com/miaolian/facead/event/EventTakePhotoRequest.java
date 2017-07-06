package com.miaolian.facead.event;


import com.miaolian.facead.view.CameraFaceAlignmentFragment;

/**
 * Created by gaofeng on 2017-05-11.
 */

public class EventTakePhotoRequest {
    CameraFaceAlignmentFragment.PhotoType type;

    public EventTakePhotoRequest(CameraFaceAlignmentFragment.PhotoType type) {
        this.type = type;
    }

    public CameraFaceAlignmentFragment.PhotoType getType() {
        return type;
    }

    public void setType(CameraFaceAlignmentFragment.PhotoType type) {
        this.type = type;
    }
}
