package com.miaolian.facead.event;

import com.miaolian.facead.util.CameraUtil;

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
