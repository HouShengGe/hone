package com.mc.app.hotel.common.facealignment.event;


import com.mc.app.hotel.common.facealignment.model.FaceRecord;

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
