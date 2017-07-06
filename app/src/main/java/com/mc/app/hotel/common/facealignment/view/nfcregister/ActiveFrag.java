package com.mc.app.hotel.common.facealignment.view.nfcregister;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.event.EventDoActive;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActiveFrag extends Fragment {
    @BindView(R.id.activeCodeEt)
    EditText activeCodeEt;

    public ActiveFrag() {
    }

    public static ActiveFrag newInstance() {
        ActiveFrag fragment = new ActiveFrag();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.activeBtn)
    public void onClick() {
        String activeCode = activeCodeEt.getText().toString().trim();
        if (activeCode.equals("")) {
            Toast.makeText(getActivity(), "请输入激活码", Toast.LENGTH_SHORT).show();
        } else {
            EventBus.getDefault().post(new EventDoActive(activeCode));
        }
    }
}
