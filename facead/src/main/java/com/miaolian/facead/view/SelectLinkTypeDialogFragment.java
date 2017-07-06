package com.miaolian.facead.view;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.miaolian.facead.R;
import com.miaolian.facead.adapter.SelectLinkTypeAdapter;
import com.miaolian.facead.event.EventSelectLinkType;
import com.miaolian.facead.util.PrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectLinkTypeDialogFragment extends DialogFragment {

    @BindView(R.id.mainRv)
    RecyclerView mainRv;
    SelectLinkTypeAdapter selectLinkTypeAdapter;

    public SelectLinkTypeDialogFragment() {
        // Required empty public constructor
    }


    public static SelectLinkTypeDialogFragment newInstance() {
        SelectLinkTypeDialogFragment fragment = new SelectLinkTypeDialogFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_select_link_type, container, false);
        ButterKnife.bind(this, view);
        selectLinkTypeAdapter = new SelectLinkTypeAdapter(getContext());
        mainRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRv.setAdapter(selectLinkTypeAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectLinkType(EventSelectLinkType event) {
        PrefUtil.setLinkType(event.getLinkType());
        dismiss();
    }

    @OnClick(R.id.cancelBtn)
    public void onClick() {
        dismiss();
    }
}
