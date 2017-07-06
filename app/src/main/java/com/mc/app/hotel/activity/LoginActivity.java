package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.LoginResBean;
import com.mc.app.hotel.bean.UrlBean;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.SPerfUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by admin on 2017/7/4.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.btn_login)
    Button btnUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login})
    public void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void getURL() {
        RxView.clicks(btnUrl)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Api.getInstance().mApiService.getURL()
                                .compose(RxSubscribeThread.<List<UrlBean>>ioAndMain()).
                                subscribe(new RxSubscribeProgress<List<UrlBean>>(LoginActivity.this) {
                                    @Override
                                    protected void onOverNext(List<UrlBean> t) {
                                        Toast.makeText(LoginActivity.this, "成功" + t.get(0).getServiceName(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    protected void onOverError(String message) {
                                        Toast.makeText(LoginActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void login() {
        final String moblie = "15869196076";
        final String vCode = "1234";
        final Map<String, String> map = Params.getLoginParams(moblie, vCode);
        RxView.clicks(btnUrl)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Api.getInstance().mApiService.userLogin(map)
                                .compose(RxSubscribeThread.<LoginResBean>ioAndMain()).
                                subscribe(new RxSubscribeProgress<LoginResBean>(LoginActivity.this) {
                                    @Override
                                    protected void onOverNext(LoginResBean t) {
                                        SPerfUtil.setReqBaseInfo(t.getToken(), t.getKey(), t.getUserInfo().getUserid());
                                        SPerfUtil.setUserInfo(t.getUserInfo());
                                        toNextActivity(MainActivity.class);
                                        Toast.makeText(LoginActivity.this, "成功" + t.getUserInfo().getUserid(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    protected void onOverError(String message) {
                                        Toast.makeText(LoginActivity.this, "失败 :" + message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }
}
