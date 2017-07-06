package com.mc.app.hotel.common.http;


import com.mc.app.hotel.bean.Download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by shouqingcao on 16/12/23.
 */

public class HttpInterceptor {

    public static final String DOWNLOAD_TAG = "download_tag";
    public static final String X_LC_Id = "i7j2k7bm26g7csk7uuegxlvfyw79gkk4p200geei8jmaevmx";
    public static final String X_LC_Key = "n6elpebcs84yjeaj5ht7x0eii9z83iea8bec9szerejj7zy3";

    //添加header信息
    public static Interceptor mHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request().newBuilder()
                    .addHeader("X-LC-Id", X_LC_Id)
                    .addHeader("X-LC-Key", X_LC_Key)
                    .addHeader("Content-Type", "application/json")
                    .build());
            return response;
        }
    };

    /**
     * 设置拦截器，添加Log，打印响应信息
     * 当相应数据做了encoder处理，可以在回调中做处理
     */
    public static HttpLoggingInterceptor mLogInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Timber.e(message);
//                try {
//                    StringReader reader = new StringReader(message);
//                    Properties properties = new Properties();
//                    properties.load(reader);
//                    properties.list(System.out);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);

    /*设置log 拦截器，打印响应数据，没有打印不知道为啥*/
//    public static HttpLoggingInterceptor mLogInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


    /*下载进度拦截器*/
    private static DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            //不频繁发送通知，防止通知栏下拉卡顿
            int progress = (int) ((bytesRead * 100) / contentLength);
            Download download = new Download();
            download.setTotalFileSize(contentLength);
            download.setCurrentFileSize(bytesRead);
            download.setProgress(progress);

            //  TODO 这里可以发送广播到需要的地方，来显示下载的进度
            RxBusManage.getDefault().post(DOWNLOAD_TAG, download);
        }
    };

    public static DownloadInterceptor mDownloadInterceptor =
            new DownloadInterceptor(downloadListener);

}
