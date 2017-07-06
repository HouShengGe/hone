package com.miaolian.facead.view;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.miaolian.facead.R;
import com.miaolian.facead.adapter.ChooseCameraAdapter;
import com.miaolian.facead.event.EventChooseCamera;
import com.miaolian.facead.util.PrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseCameraDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseCameraDialogFragment extends DialogFragment {


    @BindView(R.id.mainRv)
    RecyclerView mainRv;

    public ChooseCameraDialogFragment() {
        // Required empty public constructor
    }


    public static ChooseCameraDialogFragment newInstance() {
        ChooseCameraDialogFragment fragment = new ChooseCameraDialogFragment();
        fragment.setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_camera_dialog, container, false);
        ButterKnife.bind(this, view);
        mainRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRv.setAdapter(new ChooseCameraAdapter());
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChooseCamera(EventChooseCamera event) {
        PrefUtil.setCameraType(event.getCameraType());
        dismiss();
    }

    @OnClick(R.id.cancelBtn)
    public void onClick() {
        dismiss();
    }
}
