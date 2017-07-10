package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.mc.app.hotel.R;
import com.mc.app.hotel.common.Constants;
import com.mc.app.hotel.common.facealignment.FaceAilgmentActivity;

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
        buckButton(false);
        leftTitle("民宿APP");
        rightTitle(R.drawable.user);
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
                    }
                });
    }

    private static final String TAG = "MainActivity";
}
