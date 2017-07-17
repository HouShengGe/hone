package com.mc.app.hotel.common.facealignment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.event.EventChooseCamera;
import com.mc.app.hotel.common.facealignment.event.EventSelectLinkType;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.ServiceUtil;
import com.mc.app.hotel.common.facealignment.view.ChooseCameraDialogFragment;
import com.mc.app.hotel.common.facealignment.view.SelectLinkTypeDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/18.
 */

public class NewSettingActivity extends AppCompatActivity {
    @BindView(R.id.confidenceTv)
    TextView confidenceTv;
    @BindView(R.id.confidenceSeekBar)
    AppCompatSeekBar confidenceSeekBar;

    @BindView(R.id.selectLinkTypeBtn)
    Button selectLinkTypeBtn;
    SelectLinkTypeDialogFragment selectLinkTypeDialogFragment;

    @BindView(R.id.chooseCameraBtn)
    Button chooseCameraBtn;
    ChooseCameraDialogFragment chooseCameraDialogFragment;

    @BindView(R.id.rl_left)
    LinearLayout rl_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_setting);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        initFaceView();
        initReadView();
        initCameraView();
    }

    private void initCameraView() {
        chooseCameraDialogFragment = ChooseCameraDialogFragment.newInstance();
        switch (PrefUtil.getCameraType()) {
            case FRONT_CAMERA:
                chooseCameraBtn.setText(R.string.FRONT_CAMERA);
                break;
            case BACK_CAMERA:
                chooseCameraBtn.setText(R.string.BACK_CAMERA);
                break;
            case ANYONE:
                chooseCameraBtn.setText(R.string.ANY_CAMERA);
                break;
        }

    }

    private void initReadView() {
        selectLinkTypeDialogFragment = SelectLinkTypeDialogFragment.newInstance();
        switch (PrefUtil.getLinkType()) {
            case ServiceUtil.OCR:
                selectLinkTypeBtn.setText(getString(R.string.OCR_READ_CARD));
                break;
            case ServiceUtil.OTG:
                selectLinkTypeBtn.setText(getString(R.string.OTG_READ_CARD));
                break;
            case ServiceUtil.NFC:
                selectLinkTypeBtn.setText(getString(R.string.NFC_READ_CARD));
                break;
            case ServiceUtil.SERIALPORT:
                selectLinkTypeBtn.setText(getString(R.string.SERIALPORT_READ_CARD));
                break;
            case ServiceUtil.CAMERA:
                selectLinkTypeBtn.setText(getString(R.string.CAMERA_READ_CARD));
                break;
            default:
                break;
        }
    }

    private void initFaceView() {
        confidenceTv.setText(String.valueOf((int) PrefUtil.getMinConfidence()));
        confidenceSeekBar.setProgress((int) PrefUtil.getMinConfidence());
        confidenceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                confidenceTv.setText(String.valueOf(progress));
                PrefUtil.setMinConfidence(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectLinkType(EventSelectLinkType event) {
        switch (event.getLinkType()) {
            case ServiceUtil.OCR:
                selectLinkTypeBtn.setText(getString(R.string.OCR_READ_CARD));
                break;
            case ServiceUtil.OTG:
                selectLinkTypeBtn.setText(getString(R.string.OTG_READ_CARD));
                break;
            case ServiceUtil.NFC:
                selectLinkTypeBtn.setText(getString(R.string.NFC_READ_CARD));
                break;
            case ServiceUtil.SERIALPORT:
                selectLinkTypeBtn.setText(getString(R.string.SERIALPORT_READ_CARD));
                break;
            case ServiceUtil.CAMERA:
                selectLinkTypeBtn.setText(getString(R.string.CAMERA_READ_CARD));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChooseCamera(EventChooseCamera event) {
        switch (event.getCameraType()) {
            case FRONT_CAMERA:
                chooseCameraBtn.setText(R.string.FRONT_CAMERA);
                break;
            case BACK_CAMERA:
                chooseCameraBtn.setText(R.string.BACK_CAMERA);
                break;
            case ANYONE:
                chooseCameraBtn.setText(R.string.ANY_CAMERA);
                break;
        }
    }

    @OnClick({R.id.selectLinkTypeBtn, R.id.chooseCameraBtn, R.id.rl_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectLinkTypeBtn:
                selectLinkTypeDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.chooseCameraBtn:
                chooseCameraDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.rl_left:
                finish();
                break;
        }
    }
}
