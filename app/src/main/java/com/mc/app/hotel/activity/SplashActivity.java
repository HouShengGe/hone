package com.mc.app.hotel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.util.CountDownTimers;
import com.mc.app.hotel.common.util.SPerfUtil;

/**
 * Created by Administrator on 2017/7/6.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getCountDown().start();
    }

    private CountDownTimers sendReceiveCountDown;

    public CountDownTimers getCountDown() {
        if (this.sendReceiveCountDown == null) {
            this.sendReceiveCountDown = new CountDownTimers(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    if (SPerfUtil.isLogin()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                }
            };

        }
        return this.sendReceiveCountDown;
    }
}
