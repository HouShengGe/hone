package com.mc.app.hotel.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/31.
 */
public class HttpResBaseBean<T> implements Serializable {

    int flag;
    String msg;
    String total;
    T data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
