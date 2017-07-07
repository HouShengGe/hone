package com.mc.app.hotel.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */

public class RoomStatusBean {
    int recordNums;
    List<RoomStatusInfo> rooms;

    public int getRecordNums() {
        return recordNums;
    }

    public void setRecordNums(int recordNums) {
        this.recordNums = recordNums;
    }

    public List<RoomStatusInfo> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomStatusInfo> rooms) {
        this.rooms = rooms;
    }
}
