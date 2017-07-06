package com.miaolian.facead.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miaolian.facead.R;
import com.miaolian.facead.event.EventChooseCamera;
import com.miaolian.facead.event.EventSelectCameraRotate;
import com.miaolian.facead.util.PrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CameraSettingFragment extends Fragment {


    @BindView(R.id.chooseCameraBtn)
    Button chooseCameraBtn;
    @BindView(R.id.currentCameraPreviewSizeTv)
    TextView currentCameraPreviewSizeTv;
    ChooseCameraDialogFragment chooseCameraDialogFragment;
    CameraRotateDialogFragment cameraRotateDialogFragment;
    @BindView(R.id.chooseRotateDegreeBtn)
    Button chooseRotateDegreeBtn;
    @BindView(R.id.setCameraSleepBtn)
    Button setCameraSleepBtn;
    @BindView(R.id.cameraSleepTimeOutEt)
    EditText cameraSleepTimeOutEt;
    @BindView(R.id.setCameraSleepPanel)
    MyExpandableView setCameraSleepPanel;
    @BindView(R.id.chooseCameraPreviewSizeBtn)
    Button chooseCameraPreviewSizeBtn;


    public CameraSettingFragment() {
    }


    public static CameraSettingFragment newInstance() {
        CameraSettingFragment fragment = new CameraSettingFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        cameraSleepTimeOutEt.setText(String.valueOf(PrefUtil.getCameraSleepTimeoutMS()));
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseCameraDialogFragment = ChooseCameraDialogFragment.newInstance();
        cameraRotateDialogFragment = CameraRotateDialogFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_setting, container, false);
        ButterKnife.bind(this, view);
        if (PrefUtil.getUseCameraSleep()) {
            setCameraSleepPanel.expand(false);
            setCameraSleepBtn.setText(R.string.ON);
        } else {
            setCameraSleepBtn.setText(R.string.OFF);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        chooseRotateDegreeBtn.setText(PrefUtil.getCameraRotateDegree() + "°");
        cameraSleepTimeOutEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    long timeoutMS = Long.parseLong(s.toString());
                    PrefUtil.setCameraSleepTimeoutMS(timeoutMS);
                    Timber.e("onTextChanged:timeoutMS=" + timeoutMS);
                } catch (Exception e) {
                    Timber.e("onTextChanged:" + Log.getStackTraceString(e));
                    PrefUtil.setCameraSleepTimeoutMS(PrefUtil.DEFAULT_CAMERA_SLEEP_TIMEOUT_MS);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.setCameraSleepBtn, R.id.chooseRotateDegreeBtn, R.id.chooseCameraBtn, R.id.chooseCameraPreviewSizeBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chooseCameraBtn:
                chooseCameraDialogFragment.show(getChildFragmentManager(), "");
                break;
            case R.id.chooseRotateDegreeBtn:
                cameraRotateDialogFragment.show(getChildFragmentManager(), "");
                break;

            case R.id.chooseCameraPreviewSizeBtn:

                break;

            case R.id.setCameraSleepBtn:
                if (PrefUtil.getUseCameraSleep()) {
                    setCameraSleepBtn.setText(R.string.OFF);
                    PrefUtil.setUseCameraSleep(false);
                    setCameraSleepPanel.collapse(true);
                } else {
                    setCameraSleepBtn.setText(R.string.ON);
                    PrefUtil.setUseCameraSleep(true);
                    setCameraSleepPanel.expand(true);
                }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectCameraRotate(EventSelectCameraRotate event) {
        chooseRotateDegreeBtn.setText(event.getDegree() + "°");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
