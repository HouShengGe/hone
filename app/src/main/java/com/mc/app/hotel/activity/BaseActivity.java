package com.mc.app.hotel.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.inter.TitleInterface;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2017/7/1.
 */

public class BaseActivity extends AppCompatActivity implements TitleInterface, View.OnClickListener {
    boolean registerEventBus = false;
    private View headerTitleView;
    protected TitleViewHolder titleViewHolder;

    protected void onCreate(Bundle savedInstanceState, boolean register) {
        super.onCreate(savedInstanceState);
        registerEventBus(register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState, false);
    }

    public void registerEventBus(boolean registerEventBus) {
        this.registerEventBus = registerEventBus;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (headerTitleView == null) {
            headerTitleView = findViewById(R.id.view_header_title);
            initHeaderTitleView();
        }
    }

    private void initHeaderTitleView() {

        if (headerTitleView == null) {
            return;
        }
        titleViewHolder = new TitleViewHolder(headerTitleView);
        titleViewHolder.leftBtn.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        progress();
        if (registerEventBus) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        if (registerEventBus) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    private Dialog progressDialog;

    private void progress() {
        progressDialog = new Dialog(this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.load_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
    }

    public void showLoading() {
        progressDialog.show();
    }

    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                hideLoading();
                this.finish();
                break;
            case R.id.img_right_icon:
                showToast("right click");
                break;
        }
    }

    @Override
    public void buckButton(boolean isShow) {
        if (isShow) {
            titleViewHolder.rlLeft.setOnClickListener(this);
            titleViewHolder.imgBack.setVisibility(View.VISIBLE);
            titleViewHolder.leftBtn.setText("返回");
        } else {
            titleViewHolder.rlLeft.setOnClickListener(null);
        }
    }

    @Override
    public void leftTitle(String title) {
        if (titleViewHolder == null) {
            return;
        }
        titleViewHolder.rlLeft.setEnabled(false);
        titleViewHolder.imgBack.setVisibility(View.GONE);
        titleViewHolder.leftBtn.setText(title);
    }

    @Override
    public void setTitle(String title) {
        if (titleViewHolder == null) {
            return;
        }
        titleViewHolder.titleTv.setText(title);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void rightTitle(int drawable) {
        if (titleViewHolder == null) {
            return;
        }
        titleViewHolder.rightBtn.setVisibility(View.VISIBLE);
        titleViewHolder.rightBtn.setImageResource(drawable);
        titleViewHolder.rightBtn.setOnClickListener(this);
    }

    protected class TitleViewHolder {

        public TextView titleTv;
        public TextView leftBtn;
        public ImageView rightBtn;
        ImageView imgBack;
        LinearLayout rlLeft;

        public TitleViewHolder(View view) {
            if (view != null) {
                titleTv = (TextView) view.findViewById(R.id.tv_header_title);
                leftBtn = (TextView) view.findViewById(R.id.tv_left_title);
                rightBtn = (ImageView) view.findViewById(R.id.img_right_icon);
                imgBack = (ImageView) view.findViewById(R.id.img_back);
                rlLeft = (LinearLayout) view.findViewById(R.id.rl_left);
            }
        }
    }

    public void toNextActivity(Class<?> targetActivity) {
        toNextActivity(targetActivity, null);
    }

    public void toNextActivity(Class<?> targetActivity, Bundle data) {
        Intent it = new Intent(this, targetActivity);
        if (data != null) {
            it.putExtras(data);
        }
        startActivity(it);
    }

    public void toNextActivity(Class<?> targetActivity, Bundle data, int reqCode) {
        Intent it = new Intent(this, targetActivity);
        if (data != null) {
            it.putExtras(data);
        }
        startActivityForResult(it, reqCode);
    }

    public void toNextActivity(Class<?> targetActivity, int reqCode) {
        toNextActivity(targetActivity, null, reqCode);
    }

}
