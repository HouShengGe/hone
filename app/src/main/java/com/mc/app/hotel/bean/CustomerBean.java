package com.mc.app.hotel.bean;

import java.util.List;

/**
 * Created by admin on 2017/7/8.
 */

public class CustomerBean {
    int recordNums;
    List<CustomerInfo> custs;

    public int getRecordNums() {
        return recordNums;
    }

    public void setRecordNums(int recordNums) {
        this.recordNums = recordNums;
    }

    public List<CustomerInfo> getCusts() {
        return custs;
    }

    public void setCusts(List<CustomerInfo> custs) {
        this.custs = custs;
    }
}
