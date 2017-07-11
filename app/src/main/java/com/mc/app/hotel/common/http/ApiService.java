package com.mc.app.hotel.common.http;


import com.mc.app.hotel.bean.ComparedFaceBean;
import com.mc.app.hotel.bean.CustomerBean;
import com.mc.app.hotel.bean.HotelBean;
import com.mc.app.hotel.bean.HttpResBaseBean;
import com.mc.app.hotel.bean.LoginResBean;
import com.mc.app.hotel.bean.NationBean;
import com.mc.app.hotel.bean.PersonBean;
import com.mc.app.hotel.bean.RoomDetialBean;
import com.mc.app.hotel.bean.RoomStatusBean;
import com.mc.app.hotel.bean.UrlBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/31.
 */
public interface ApiService {

    @GET(HttpConstant.GET_URL)
    Observable<HttpResBaseBean<List<UrlBean>>> getURL();

    @FormUrlEncoded
    @POST(HttpConstant.USER_LOGIN)
    Observable<HttpResBaseBean<LoginResBean>> userLogin(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.GET_CODE)
    Observable<HttpResBaseBean<String>> getVCode(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.GET_ROOM_PIC)
    Observable<HttpResBaseBean<RoomStatusBean>> getRoomStatus(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.CHECK_IN)
    Observable<HttpResBaseBean<String>> commitCheckInMsg(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.GET_CUST_LIST)
    Observable<HttpResBaseBean<CustomerBean>> getCustomerList(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.GET_FACE)
    Observable<HttpResBaseBean<ComparedFaceBean>> getGetFace(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.CHEC_KOUT)
    Observable<HttpResBaseBean<String>> checkOut(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.GET_ROOM_INFO)
    Observable<HttpResBaseBean<RoomDetialBean>> getRoomDetial(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.USER_LOG_OFF)
    Observable<HttpResBaseBean<String>> getExit(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.GET_STORE_LIST)
    Observable<HttpResBaseBean<HotelBean>> getHotelList(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.GET_NATION_LIST)
    Observable<HttpResBaseBean<List<NationBean>>> getNationList(@FieldMap Map<String, String> a);

    @FormUrlEncoded
    @POST(HttpConstant.USE_CENTER)
    Observable<HttpResBaseBean<PersonBean>> getPersonInfo(@FieldMap Map<String, String> a);


}
