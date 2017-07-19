package com.mc.app.hotel.common.util;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mc.app.hotel.common.App;
import com.mc.app.hotel.common.facealignment.util.DensityUtil;

import java.lang.reflect.Method;

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

    public static void hideKeyBoard(Activity a, EditText mEditText){
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEditText.setInputType(InputType.TYPE_NULL);
        } else {
            a.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(mEditText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
