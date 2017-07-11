package com.mc.app.hotel.common.http;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.mc.app.hotel.activity.LoginActivity;
import com.mc.app.hotel.common.App;
import com.mc.app.hotel.common.util.SPerfUtil;
import com.mc.app.hotel.common.util.ToastUtils;

import static com.mc.app.hotel.common.App.store;

/**
 * Created by Administrator on 2017/7/3.
 */
public class ServerException extends Exception {

    public static final int ERROR_FAIL = 2;
    public static final int ERROR_EXCEPTION = -1;
    public static final int ERROR_TOKEN_FAULT = 103;
    private static String message;

    public ServerException(int flag, String msg) {
        super(getApiExceptionMessage(flag, msg));
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param flag
     * @param msg
     * @return
     */
    private static String getApiExceptionMessage(int flag, String msg) {
        switch (flag) {
            case ERROR_FAIL:
                message = msg;
                break;
            case ERROR_EXCEPTION:
                message = msg;
                break;
            case ERROR_TOKEN_FAULT:
                SPerfUtil.reset();
                store.get(store.size() - 1).startActivity(new Intent(store.get(store.size() - 1), LoginActivity.class));
                for (Activity a : store) {
                    a.finish();
                }
                ToastUtils.show(App.getAppContext(), "请重新登录", Toast.LENGTH_SHORT);
                break;
            default:
                message = "未知错误";
        }
        return message;
    }
}
