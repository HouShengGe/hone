package com.mc.app.hotel.common.facealignment.event;


import com.mc.app.hotel.common.facealignment.model.FaceRecord;

/**
 * Created by gaofeng on 2017-05-11.
 */

public class EventDataSaveRequest {
    FaceRecord faceRecord;
    int from;

    public EventDataSaveRequest(FaceRecord faceRecord, int from) {
        this.faceRecord = faceRecord;
        this.from = from;
    }

    public FaceRecord getFaceRecord() {
        return faceRecord;
    }

    public void setFaceRecord(FaceRecord faceRecord) {
        this.faceRecord = faceRecord;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
