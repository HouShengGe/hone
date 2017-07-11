package com.mc.app.hotel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.LoginResBean;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.CountDownTimers;
import com.mc.app.hotel.common.util.SPerfUtil;

/**
 * Created by Administrator on 2017/7/6.
 */

public class SplashActivity extends Activity {

    long startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startTime = System.currentTimeMillis();
        login();
    }

    private void login() {
        if (SPerfUtil.isLogin()) {
            String mobile = SPerfUtil.getUserInfo().getStrMobile();
            Api.getInstance().mApiService.userLogin(Params.getLoginParams(mobile, ""))
                    .compose(RxSubscribeThread.<LoginResBean>ioAndMain()).
                    subscribe(new RxSubscribeProgress<LoginResBean>(SplashActivity.this, false) {
                        @Override
                        protected void onOverNext(LoginResBean t) {
                            SPerfUtil.setReqBaseInfo(t.getToken(), t.getKey(), t.getUserInfo().getUserid());
                            SPerfUtil.setUserInfo(t.getUserInfo());
                            if (t.getUserInfo().getUserType() == 0) {
                                toNextActivity(MainActivity.class);
                            } else {
                                toNextActivity(PoliceMainActivity.class);
                            }

                        }

                        @Override
                        protected void onOverError(String message) {
                            toNextActivity(LoginActivity.class);
                        }
                    });

        } else {
            toNextActivity(LoginActivity.class);
        }
    }




    void toNextActivity(Class<?> targetActivity) {
            long distanceTime = System.currentTimeMillis() - startTime;
            if (distanceTime > 2000) {
                startActivity(new Intent(SplashActivity.this, targetActivity));
                finish();
            } else {
                getCountDown(targetActivity, 2000 - distanceTime).start();
            }
    }

    private CountDownTimers sendReceiveCountDown;

    public CountDownTimers getCountDown(final Class<?> targetActivity, long time) {
        if (this.sendReceiveCountDown == null) {
            this.sendReceiveCountDown = new CountDownTimers(time, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(SplashActivity.this, targetActivity));
                    finish();
                }
            };

        }
        return this.sendReceiveCountDown;
    }
}
