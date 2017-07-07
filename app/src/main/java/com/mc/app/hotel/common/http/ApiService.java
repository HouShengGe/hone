package com.mc.app.hotel.common.http;


import com.mc.app.hotel.bean.HttpResBaseBean;
import com.mc.app.hotel.bean.LoginResBean;
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


}
