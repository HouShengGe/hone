package com.mc.app.hotel.common.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Interceptor for download
 * Created by JokAr on 16/5/11.
 */
public class DownloadInterceptor implements Interceptor {

    private DownloadListener listener;

    public DownloadInterceptor(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
