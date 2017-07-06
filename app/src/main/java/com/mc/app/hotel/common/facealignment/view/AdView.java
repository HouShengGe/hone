package com.mc.app.hotel.common.facealignment.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by gaofeng on 2017-02-20.
 */

public class AdView extends WebView {
    private String adUrl = "https://www.jd.com";

    public AdView(Context context) {
        super(context);
        init();
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    private void init() {
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        loadUrl(adUrl);
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
        loadUrl(adUrl);
    }
}
