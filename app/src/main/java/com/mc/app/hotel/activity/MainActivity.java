package com.mc.app.hotel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.FaceAilgmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.faceailgnment)
    Button btnFaceailgnment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setTitle("民宿");
        buckButton(true);
        rightTitle(R.drawable.friend_info_icon);
        btn.setOnClickListener(this);
        btnFaceailgnment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn:
                toNextActivity(RoomStatusActivity.class);
                break;
            case R.id.faceailgnment:
                toNextActivity(FaceAilgmentActivity.class);
                break;

        }
    }
}
