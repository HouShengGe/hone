package com.mc.app.hotel.bean;

/**
 * Created by admin on 2017/7/8.
 */

public class CustomerParamsInfo {
    int pageIndex;
    int pageSize = 5;
    String roomNo;
    String storeId;
    String custName;
    String idCard;
    String mobile;
    int qtype;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }

    public void setCustomerParamsInfo(CustomerParamsInfo info) {
            setRoomNo(info.getRoomNo());
            setCustName(info.getCustName());
            setIdCard(info.getIdCard());
            setMobile(info.getMobile());
    }
}
