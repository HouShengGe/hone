package com.mc.app.hotel.common.http;


import com.mc.app.hotel.bean.AppUpdateBean;
import com.mc.app.hotel.bean.BaseBean;
import com.mc.app.hotel.bean.DataDemo;
import com.mc.app.hotel.bean.PictureBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/31.
 */
public interface ApiService {

    /*获取json数据（一个不需要参数的接口）*/
    @POST(HttpConstant.GRT_DATA)
    Observable<BaseBean<DataDemo>> getDataDemo();

    /*上传图片（一张或多张）URL暂时不能用*/
    @Multipart
    @POST(HttpConstant.UPLOAD_PIC)
    Observable<BaseBean<PictureBean>> uploadPicture(@Part List<MultipartBody.Part> partList);

    /*检查app 的版本号*/
    @GET(HttpConstant.GET_APK_VERSION)
    Observable<AppUpdateBean> getAppVersion();

    /*下载app*/
    @Streaming
    @GET
    Observable<ResponseBody> downloadApp(@Url String loadUrl);

    /*获取json数据（一个不需要参数的接口）*/
    @GET(HttpConstant.HOME_PAGE)
    Observable<BaseBean<DataDemo>> getHomePage();
}
