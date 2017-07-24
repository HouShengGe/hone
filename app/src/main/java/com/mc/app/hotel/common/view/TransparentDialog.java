package com.mc.app.hotel.common.view;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.mc.app.hotel.R;

/**
 * Created by admin on 2017/7/25.
 */

public class TransparentDialog extends AlertDialog {
    protected TransparentDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.MyDialog);
    }
}
