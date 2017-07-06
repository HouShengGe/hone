package com.mc.app.hotel.common.facealignment.view;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.adapter.CameraRotateAdapter;
import com.mc.app.hotel.common.facealignment.event.EventSelectCameraRotate;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CameraRotateDialogFragment extends DialogFragment {

    @BindView(R.id.mainRv)
    RecyclerView mainRv;
    Unbinder unbinder;

    public CameraRotateDialogFragment() {

    }

    public static CameraRotateDialogFragment newInstance() {
        CameraRotateDialogFragment fragment = new CameraRotateDialogFragment();
        fragment.setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_rotate_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        mainRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRv.setAdapter(new CameraRotateAdapter());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float widthRate = Float.parseFloat(getString(R.string.dialog_fragment_width_rate));
        float heightRate = Float.parseFloat(getString(R.string.dialog_fragment_height_rate));
        getDialog().getWindow().setLayout((int) (width * widthRate), (int) (height * heightRate));
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wmLayoutParams = getDialog().getWindow().getAttributes();
        wmLayoutParams.dimAmount = 0f;
        wmLayoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getDialog().getWindow().setAttributes(wmLayoutParams);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectCameraRotate(EventSelectCameraRotate event) {
        PrefUtil.setCameraRotateDegree(event.getDegree());
        dismiss();
    }

    @OnClick(R.id.cancelBtn)
    public void onViewClicked() {
        dismiss();
    }
}
