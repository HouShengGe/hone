package com.mc.app.hotel.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.common.App;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.SPerfUtil;
import com.mc.app.hotel.common.util.ToastUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by admin on 2017/7/10.
 */

public class PersonCenterActivity extends BaseActivity {

    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ButterKnife.bind(this);
        buckButton(true);
        setTitle("个人中心");
        exit();
    }

    private void exit() {

        RxView.clicks(btnExit)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Map<String, String> map = Params.getParams();
                        Api.getInstance().mApiService.getExit(map)
                                .compose(RxSubscribeThread.<String>ioAndMain()).
                                subscribe(new RxSubscribeProgress<String>(PersonCenterActivity.this) {
                                    @Override
                                    protected void onOverNext(String t) {
                                        SPerfUtil.reset();
                                        toNextActivity(LoginActivity.class);
                                        for (Activity a : App.store) {
                                            a.finish();
                                        }
                                    }

                                    @Override
                                    protected void onOverError(String message) {
                                        ToastUtils.show(PersonCenterActivity.this, message, Toast.LENGTH_SHORT);
                                    }
                                });
                    }
                });
    }
}
