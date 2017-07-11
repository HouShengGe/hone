package com.mc.app.hotel.bean;

import java.util.List;

/**
 * Created by admin on 2017/7/11.
 */

public class HotelBean {
    int recordNums;
    List<HotelInfo> stores;

    public int getRecordNums() {
        return recordNums;
    }

    public void setRecordNums(int recordNums) {
        this.recordNums = recordNums;
    }

    public List<HotelInfo> getStores() {
        return stores;
    }

    public void setStores(List<HotelInfo> stores) {
        this.stores = stores;
    }
}
