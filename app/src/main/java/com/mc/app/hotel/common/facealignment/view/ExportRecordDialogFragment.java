package com.mc.app.hotel.common.facealignment.view;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.event.EventCancelExportExcel;
import com.mc.app.hotel.common.facealignment.event.EventConfirmExportExcel;
import com.mc.app.hotel.common.facealignment.event.EventDismissExcelExportDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ExportRecordDialogFragment extends DialogFragment {
    ExportRecordConfirmFragment exportRecordConfirmFragment;
    ExportRecordProcessFragment exportRecordProcessFragment;

    public ExportRecordDialogFragment() {
        exportRecordConfirmFragment = ExportRecordConfirmFragment.newInstance();
        exportRecordProcessFragment = ExportRecordProcessFragment.newInstance();
    }


    public static ExportRecordDialogFragment newInstance() {
        ExportRecordDialogFragment fragment = new ExportRecordDialogFragment();
        fragment.setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_export_record_dialog, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        showFragment(exportRecordConfirmFragment);
    }


    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventConfirmExportExcel(EventConfirmExportExcel event) {
        showFragment(exportRecordProcessFragment);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCancelExportExcel(EventCancelExportExcel event) {
        dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDismissExcelExportDialog(EventDismissExcelExportDialog event) {
        dismiss();
    }


    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.fragContainer, fragment);
        ft.commit();
    }
}
