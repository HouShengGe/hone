package com.mc.app.hotel.common.http;

/**
 * Created by Administrator on 2016/10/31.
 */
public class HttpConstant {

    public static String BASE_URL = "http://103.27.6.36:8501/rest/";          //上传图片接口使用

    public static void setBaseUrl(String url) {
        BASE_URL = url;
    }

    public static final String GET_URL = "GetServiceList";//获取URL
    public static final String USER_LOGIN = "UserLogin";//登陆
    public static final String GET_CODE = "GetCode";//获取验证码
}
