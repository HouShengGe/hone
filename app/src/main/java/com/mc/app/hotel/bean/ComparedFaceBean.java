package com.mc.app.hotel.bean;

/**
 * Created by admin on 2017/7/8.
 */

public class ComparedFaceBean {

    String idCardPhoto;
    String scanPhoto;
    String customer;
    String faceResult;

    public String getIdCardPhoto() {
        return idCardPhoto;
    }

    public void setIdCardPhoto(String idCardPhoto) {
        this.idCardPhoto = idCardPhoto;
    }

    public String getScanPhoto() {
        return scanPhoto;
    }

    public void setScanPhoto(String scanPhoto) {
        this.scanPhoto = scanPhoto;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getFaceResult() {
        return faceResult;
    }

    public void setFaceResult(String faceResult) {
        this.faceResult = faceResult;
    }
}
