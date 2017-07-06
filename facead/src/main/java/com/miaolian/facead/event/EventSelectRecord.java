package com.miaolian.facead.event;

import com.miaolian.facead.model.FaceRecord;

/**
 * Created by gaofeng on 2017-04-25.
 */

public class EventSelectRecord {
    FaceRecord faceRecord;

    public EventSelectRecord(FaceRecord faceRecord) {
        this.faceRecord = faceRecord;
    }

    public FaceRecord getFaceRecord() {
        return faceRecord;
    }

    public void setFaceRecord(FaceRecord faceRecord) {
        this.faceRecord = faceRecord;
    }
}
