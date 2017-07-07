package com.mc.app.hotel.common.facealignment.util;

import com.mc.app.hotel.common.App;

/**
 * Created by Administrator on 2017年7月7日.
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = App.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = App.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
