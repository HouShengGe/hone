package com.mc.app.hotel.common.util;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/7/10.
 */

public class WidgetUtils {
    public static void setHeigthEQWidget(View view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = params.width;
        view.setLayoutParams(params);
    }
}
