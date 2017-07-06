package com.mc.app.hotel.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/31.
 */
public class BaseBean<T> implements Serializable {

    public String code;
    public String message;
    public T result;


    public boolean success(){
        return code.equals("200") || code.equals("0");
    }

}
