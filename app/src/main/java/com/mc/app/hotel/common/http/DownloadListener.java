package com.mc.app.hotel.common.http;

/**
 * 下载进度listener
 * Created by JokAr on 16/5/11.
 */
public interface DownloadListener {
    void update(long bytesRead, long contentLength, boolean done);
}
