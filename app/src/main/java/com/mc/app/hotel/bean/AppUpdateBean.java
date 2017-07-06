package com.mc.app.hotel.bean;

/**
 * Created by shouqingcao on 16/12/21.
 */

public class AppUpdateBean {

    private String latestVersion;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }
}
