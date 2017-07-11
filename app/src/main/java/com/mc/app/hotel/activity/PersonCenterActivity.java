package com.mc.app.hotel.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.PersonBean;
import com.mc.app.hotel.common.App;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.ArrayListUtils;
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
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.ll_police)
    LinearLayout llPolice;
    @BindView(R.id.tv_heotel_name)
    TextView tvHeotelName;
    @BindView(R.id.tv_heotel_address)
    TextView tvHeotelAddress;
    @BindView(R.id.tv_heotel_own)
    TextView tvHeotelOwn;
    @BindView(R.id.tv_heotel_phone)
    TextView tvHeotelPhone;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_aera)
    TextView tvAera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ButterKnife.bind(this);
        buckButton(true);
        setTitle("个人中心");
        getPersonInfo();
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

    private void getPersonInfo() {

        Api.getInstance().mApiService.getPersonInfo(Params.getParams())
                .compose(RxSubscribeThread.<PersonBean>ioAndMain()).
                subscribe(new RxSubscribeProgress<PersonBean>(PersonCenterActivity.this) {
                    @Override
                    protected void onOverNext(PersonBean t) {
                        if (SPerfUtil.getUserInfo().getUserType() == 0) {
                            llNormal.setVisibility(View.VISIBLE);
                            tvHeotelName.setText(t.getStoreName());
                            tvHeotelAddress.setText(t.getAddress());
                            tvHeotelOwn.setText(t.getChargeName());
                            tvHeotelPhone.setText(t.getChargePhone());
                        } else {
                            llPolice.setVisibility(View.VISIBLE);
                            tvName.setText(t.getPoliceMan());
                            tvAera.setText(ArrayListUtils.toStrings(t.getStoresList()));
                        }
                    }

                    @Override
                    protected void onOverError(String message) {
                        ToastUtils.show(PersonCenterActivity.this, message, Toast.LENGTH_SHORT);
                    }
                });
    }
}
