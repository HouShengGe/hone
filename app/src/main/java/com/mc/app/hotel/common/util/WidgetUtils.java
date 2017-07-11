package com.mc.app.hotel.common.util;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mc.app.hotel.common.App;
import com.mc.app.hotel.common.facealignment.util.DensityUtil;

/**
 * Created by Administrator on 2017/7/10.
 */

public class WidgetUtils {
    public static void setHeigthEQWidget(View view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = params.width;
        view.setLayoutParams(params);
    }
    public static void setWidth(View view) {
        WindowManager wm = (WindowManager) App.getAppContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int dwidth = wm.getDefaultDisplay().getWidth();
        int width = (DensityUtil.px2dip(dwidth)-85)/2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = DensityUtil.dip2px(width);
        view.setLayoutParams(params);
    }
}
