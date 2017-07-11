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
    public static final String GET_ROOM_PIC = "GetRoomPic";//获取房态图
    public static final String CHECK_IN = "CheckIn";//申请入驻
    public static final String GET_CUST_LIST = "GetCustList";//申请入驻
    public static final String GET_FACE = "GetFace";//人证对比
    public static final String CHEC_KOUT = "CheckOut";//申报离开
    public static final String GET_ROOM_INFO = "GetRoomInfo";//获取房间信息
    public static final String USER_LOG_OFF = "UserLogOff";//登出
    public static final String GET_STORE_LIST = "GetStoreList";//获取酒店列表
    public static final String GET_NATION_LIST = "GetNationList";//获取酒店列表
    public static final String USE_CENTER = "UseCenter";//个人中心
}
