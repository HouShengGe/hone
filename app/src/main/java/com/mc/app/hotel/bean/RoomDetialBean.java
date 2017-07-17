package com.mc.app.hotel.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

public class RoomDetialBean {
    String roomNo;
    int recordNums;
    String roomType;
    String roomPrice;
    List<RoomDetialInfo> custs;

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getRecordNums() {
        return recordNums;
    }

    public void setRecordNums(int recordNums) {
        this.recordNums = recordNums;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public List<RoomDetialInfo> getCusts() {
        return custs;
    }

    public void setCusts(List<RoomDetialInfo> custs) {
        this.custs = custs;
    }

}
