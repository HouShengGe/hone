package com.mc.app.hotel.common.facealignment.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.event.EventCancelExportExcel;
import com.mc.app.hotel.common.facealignment.event.EventConfirmExportExcel;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ExportRecordConfirmFragment extends Fragment {

    Unbinder unbinder;

    public ExportRecordConfirmFragment() {

    }


    public static ExportRecordConfirmFragment newInstance() {
        ExportRecordConfirmFragment fragment = new ExportRecordConfirmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_export_record_confirm, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.positiveBtn, R.id.negativeBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.positiveBtn:
                EventBus.getDefault().post(new EventConfirmExportExcel());
                break;
            case R.id.negativeBtn:
                EventBus.getDefault().post(new EventCancelExportExcel());
                break;
        }
    }
}
