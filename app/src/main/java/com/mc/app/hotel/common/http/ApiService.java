package com.mc.app.hotel.common.http;


import com.mc.app.hotel.bean.HttpResBaseBean;
import com.mc.app.hotel.bean.LoginResBean;
import com.mc.app.hotel.bean.UrlBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/31.
 */
public interface ApiService {

    @GET(HttpConstant.GET_URL)
    Observable<HttpResBaseBean<List<UrlBean>>> getURL();

    @GET(HttpConstant.USER_LOGIN)
    Observable<HttpResBaseBean<LoginResBean>> userLogin(@QueryMap Map<String,String> a);


}
