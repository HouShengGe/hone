package com.mc.app.hotel.common.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mc.app.hotel.common.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/10/31.
 */
public class Api {

    private static final String CACHE_PATH = "cache";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static Retrofit retrofit;
    public static ApiService mApiService;

    //在访问HttpMethods时创建单例
    private static class ApiHolder {
        private static final Api INSTANCE = new Api();
    }

    //获取单例
    public static Api getInstance() {
        return ApiHolder.INSTANCE;
    }

    //构造方法私有
    private Api() {

        BaseParamsInterceptor baseInterceptor = new BaseParamsInterceptor.Builder()
                .addParam("SHIT_ID", "")
                .interceptor;

        /*设置缓存*/
        File cacheFile = new File(App.getAppContext().getCacheDir(), CACHE_PATH);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb

        /*初始化OkHttp*/
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)                                        //开启缓存
//                .sslSocketFactory(HttpSLL.getSLLContext().getSocketFactory())         //添加https证书
//                .addInterceptor(HttpInterceptor.mHeaderInterceptor)                  //添加请求header
//                .addInterceptor(baseInterceptor)                                    //固定参数
                .addInterceptor(HttpInterceptor.mLogInterceptor)                     //添加log
                .addInterceptor(HttpInterceptor.mDownloadInterceptor)                //添加下载监听的拦截器
//                .addNetworkInterceptor(new HttpNetInterceptor.TokenInterceptor())     //检测Token
                .addNetworkInterceptor(new HttpNetInterceptor.HttpCacheInterceptor())
                .cache(cache)
                .build();

        /*添加json格式*/
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(HttpConstant.BASE_URL)
                .build();
        mApiService = retrofit.create(ApiService.class);
    }
}
