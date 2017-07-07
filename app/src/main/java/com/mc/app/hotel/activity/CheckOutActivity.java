package com.mc.app.hotel.activity;

import android.os.Bundle;

import com.mc.app.hotel.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/7.
 */

public class CheckOutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ButterKnife.bind(this);
    }
}
