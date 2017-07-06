package com.miaolian.facead.event;

import com.miaolian.facead.model.FaceRecord;

/**
 * Created by gaofeng on 2017-05-11.
 */

public class EventDataSaveRequest {
    FaceRecord faceRecord;

    public EventDataSaveRequest(FaceRecord faceRecord) {
        this.faceRecord = faceRecord;
    }

    public FaceRecord getFaceRecord() {
        return faceRecord;
    }

    public void setFaceRecord(FaceRecord faceRecord) {
        this.faceRecord = faceRecord;
    }
}
