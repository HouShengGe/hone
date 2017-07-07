package com.mc.app.hotel.common.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.mc.app.hotel.common.facealignment.util.DensityUtil;

/**
 * Created by Administrator on 2017/7/7.
 */

public class DrawableUtils {

    public static Drawable getDrawable(String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{DensityUtil.dip2px(5), DensityUtil.dip2px(5), DensityUtil.dip2px(5), DensityUtil.dip2px(5)
                , DensityUtil.dip2px(5), DensityUtil.dip2px(5), DensityUtil.dip2px(5), DensityUtil.dip2px(5)});
        drawable.setStroke(DensityUtil.dip2px(1), Color.parseColor("#191e30"));
        drawable.setColor(Color.parseColor(color));
        return drawable;
    }
}
