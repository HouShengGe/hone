package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.bean.NationBean;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.facealignment.FaceAilgmentActivity;
import com.mc.app.hotel.common.http.Api;
import com.mc.app.hotel.common.http.Params;
import com.mc.app.hotel.common.http.RxSubscribeProgress;
import com.mc.app.hotel.common.http.RxSubscribeThread;
import com.mc.app.hotel.common.util.SPerfUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {
    @BindView(R.id.ll_room_status)
    LinearLayout llRoomStatus;

    @BindView(R.id.ll_declare_in)
    LinearLayout llDeclareIn;

    @BindView(R.id.ll_customer_history)
    LinearLayout llCustomerHistory;

    @BindView(R.id.ll_living_list)
    LinearLayout llLivingList;

    @BindView(R.id.ll_declare_out)
    LinearLayout llDeclareOut;

    @BindView(R.id.ll_setting)
    LinearLayout llSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        getNation();
        buckButton(false);
        leftTitle("民宿入住申报");
        roomStatus();
        declareIn();
        customerHistory();
        livingList();
        declareOut();
        setting();

    }

    private void roomStatus() {
        RxView.clicks(llRoomStatus)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toNextActivity(RoomStatusActivity.class);
                    }
                });
    }

    private void declareIn() {
        RxView.clicks(llDeclareIn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toNextActivity(FaceAilgmentActivity.class);
                    }
                });
    }

    private void customerHistory() {
        RxView.clicks(llCustomerHistory)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Bundle b = new Bundle();
                        b.putInt(Constants.CUSTOMER_STATUS, 2);
                        toNextActivity(SearchCustomerActivity.class, b);
                    }
                });
    }

    private void livingList() {
        RxView.clicks(llLivingList)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Bundle b = new Bundle();
                        b.putInt(Constants.CUSTOMER_STATUS, 1);
                        toNextActivity(SearchCustomerActivity.class, b);
                    }
                });
    }

    private void declareOut() {
        RxView.clicks(llDeclareOut)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toNextActivity(CheckOutActivity.class);
                    }
                });
    }

    private void setting() {
        RxView.clicks(llSetting)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toNextActivity(PersonCenterActivity.class);
                    }
                });
    }

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            showToast("再按一次退出程序");
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.finish();
            //   System.exit(0);
        }
    }

    private static final String TAG = "MainActivity";

    private void getNation() {
        Api.getInstance().mApiService.getNationList(Params.getParams())
                .compose(RxSubscribeThread.<List<NationBean>>ioAndMain()).
                subscribe(new RxSubscribeProgress<List<NationBean>>(MainActivity.this, false) {
                    @Override
                    protected void onOverNext(List<NationBean> t) {
                        SPerfUtil.saveNation(t);
                    }

                    @Override
                    protected void onOverError(String message) {

                    }
                });

    }

}
