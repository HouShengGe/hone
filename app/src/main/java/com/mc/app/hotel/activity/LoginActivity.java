package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.LoginResBean;
import com.mc.app.hotel.bean.UrlBean;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.ApiService;
import com.mc.app.hotel.common.http.ErrorHttpEvent;
import com.mc.app.hotel.common.http.GetUrl;
import com.mc.app.hotel.common.http.HttpConstant;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.CountDownTimers;
import com.mc.app.hotel.common.util.SPerfUtil;
import com.mc.app.hotel.common.util.Zz;
import com.mc.app.hotel.common.view.DialogListView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by admin on 2017/7/4.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.btn_login)
    Button btnUrl;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;

    @BindView(R.id.et_vcode)
    EditText etVCode;

    @BindView(R.id.btn_get_vcode)
    Button btnVCode;

    @BindView(R.id.ll_select_service)
    LinearLayout llSelectService;

    @BindView(R.id.tv_service_name)
    TextView tvServiceName;


    private List<UrlBean> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, true);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            int flag = getIntent().getExtras().getInt(Constants.LONG_OUT, 0);
            if (flag == Constants.LONG_OUT_FLAG) {
                showToast("请重新登录");
            }
        }
        login();
        getVCode();
        buckButton(false);
        setTitle("登录");
    }

    private void initData() {
        getURL();
    }

    private void getURL() {
        GetUrl.getInstance().mApiService.getURL()
                .compose(RxSubscribeThread.<List<UrlBean>>ioAndMain()).
                subscribe(new RxSubscribeProgress<List<UrlBean>>(LoginActivity.this) {
                    @Override
                    protected void onOverNext(List<UrlBean> t) {
                        urlList.addAll(t);
                        selectService();
                    }

                    @Override
                    protected void onOverError(String message) {
                    }
                });
    }

    private void login() {

        RxView.clicks(btnUrl)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String moblie = etPhoneNum.getText().toString().trim();
                        if (!Zz.zzMobile(moblie)) {
                            etPhoneNum.setError("请输入正确手机号");
                            return;
                        }
                        String vCode = etVCode.getText().toString().trim();
                        if (vCode == null || vCode.equals("")) {
                            etPhoneNum.setError("请填写验证码");
                            return;
                        }
                        Map<String, String> map = Params.getLoginParams(moblie, vCode);
                        Api.getInstance().mApiService.userLogin(map)
                                .compose(RxSubscribeThread.<LoginResBean>ioAndMain()).
                                subscribe(new RxSubscribeProgress<LoginResBean>(LoginActivity.this) {
                                    @Override
                                    protected void onOverNext(LoginResBean t) {
                                        SPerfUtil.setReqBaseInfo(t.getToken(), t.getKey(), t.getUserInfo().getUserid());
                                        SPerfUtil.setUserInfo(t.getUserInfo());
                                        if (t.getUserInfo().getUserType() == 0) {
                                            toNextActivity(MainActivity.class);
                                            finish();
                                        } else {
                                            toNextActivity(PoliceMainActivity.class);
                                            finish();
                                        }
                                    }

                                    @Override
                                    protected void onOverError(String message) {
                                        showToast(message);
                                    }
                                });
                    }
                });
    }

    DialogListView dialog;

    private void selectService() {
        RxView.clicks(llSelectService)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dialog = new DialogListView(LoginActivity.this, getServiceName(), new OnItemCilckEvent());
                        dialog.setCanceledOnTouchOutside(true);
                    }
                });
    }

    private List<String> getServiceName() {
        List<String> list = new ArrayList<>();
        for (UrlBean bean : urlList) {
            list.add(bean.getServiceName());
        }
        return list;
    }

    class OnItemCilckEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HttpConstant.setBaseUrl(urlList.get(position).getServiceIP());
            Api.getInstance().init();
            tvServiceName.setText(urlList.get(position).getServiceName());
            if (dialog != null)
                dialog.dismiss();
        }
    }

    private void getVCode() {

        RxView.clicks(btnVCode)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String moblie = etPhoneNum.getText().toString().trim();
                        if (!Zz.zzMobile(moblie)) {
                            etPhoneNum.setError("请输入正确手机号");
                            return;
                        }
                        Map<String, String> map = Params.getVCodeParams(moblie);
                        ApiService mService = Api.getInstance().mApiService;
                        if (mService != null)
                            mService.getVCode(map)
                                    .compose(RxSubscribeThread.<String>ioAndMain()).
                                    subscribe(new RxSubscribeProgress<String>(LoginActivity.this) {
                                        @Override
                                        protected void onOverNext(String t) {
                                            getCountDown().start();
                                        }

                                        @Override
                                        protected void onOverError(String message) {
                                        }
                                    });
                    }
                });
    }

    private CountDownTimers sendReceiveCountDown;

    public CountDownTimers getCountDown() {
        if (this.sendReceiveCountDown == null) {
            this.sendReceiveCountDown = new CountDownTimers(120000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int time = (int) millisUntilFinished / 1000;
                    btnVCode.setText("(" + time + ")");
                    btnVCode.setClickable(false);
                }

                @Override
                public void onFinish() {
                    btnVCode.setText("获取验证码");
                    btnVCode.setClickable(true);
                }
            };

        }
        return this.sendReceiveCountDown;
    }

    @Subscribe
    public void onEvent(ErrorHttpEvent event) {
        showToast("请选择服务器");
    }
}
