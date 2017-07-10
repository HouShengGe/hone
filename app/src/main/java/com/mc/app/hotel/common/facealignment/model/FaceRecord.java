package com.mc.app.hotel.common.facealignment.model;

/**
 * Created by gaofeng on 2017-04-24.
 */

public class FaceRecord {
    long recordId = 0;
    long recordTime = 0;
    String name = "";
    String sex = "";
    String nation = "";
    String birthday = "";
    String idNumber = "";
    String address = "";
    String termBegin = "";
    String termEnd = "";
    String issueAuthority = "";
    String guid = "";
    double similarity = 0;
    byte[] idPhoto = null;
    byte[] camPhoto = null;

    public FaceRecord() {
        recordId = 0;
        recordTime = 0;
        name = "";
        sex = "";
        nation = "";
        birthday = "";
        idNumber = "";
        address = "";
        termBegin = "";
        termEnd = "";
        issueAuthority = "";
        guid = "";
        similarity = 0;
        idPhoto = null;
        camPhoto = null;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTermBegin() {
        return termBegin;
    }

    public void setTermBegin(String termBegin) {
        this.termBegin = termBegin;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public String getIssueAuthority() {
        return issueAuthority;
    }

    public void setIssueAuthority(String issueAuthority) {
        this.issueAuthority = issueAuthority;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public byte[] getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(byte[] idPhoto) {
        if (idPhoto == null || idPhoto.length == 0) return;
        byte[] tempBuffer = new byte[idPhoto.length];
        System.arraycopy(idPhoto, 0, tempBuffer, 0, idPhoto.length);
        this.idPhoto = tempBuffer;
    }

    public byte[] getCamPhoto() {
        return camPhoto;
    }

    public void setCamPhoto(byte[] camPhoto) {
        if (camPhoto == null || camPhoto.length == 0) return;
        byte[] tempBuffer = new byte[camPhoto.length];
        System.arraycopy(camPhoto, 0, tempBuffer, 0, camPhoto.length);
        this.camPhoto = tempBuffer;
    }

    public boolean dataIsBlank() {
        return (name.equals("") || sex.equals("") || nation.equals("") || birthday.equals("") || idNumber.equals("") || termBegin.equals(""));
    }

    @Override
    public String toString() {
        return "FaceRecord{" +
                "recordId=" + recordId +
                ", recordTime=" + recordTime +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birthday='" + birthday + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", address='" + address + '\'' +
                ", termBegin='" + termBegin + '\'' +
                ", termEnd='" + termEnd + '\'' +
                ", issueAuthority='" + issueAuthority + '\'' +
                ", guid='" + guid + '\'' +
                ", similarity=" + similarity +
                '}';
    }
}
