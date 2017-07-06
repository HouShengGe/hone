package com.mc.app.hotel.common.facealignment.event;


import com.mc.app.hotel.common.facealignment.util.CameraUtil;

/**
 * Created by gaofeng on 2017-03-10.
 */

public class EventChooseCamera {
    public CameraUtil.CameraType cameraType;

    public EventChooseCamera(CameraUtil.CameraType cameraType) {
        this.cameraType = cameraType;
    }

    public CameraUtil.CameraType getCameraType() {
        return cameraType;
    }

    public void setCameraType(CameraUtil.CameraType cameraType) {
        this.cameraType = cameraType;
    }
}
