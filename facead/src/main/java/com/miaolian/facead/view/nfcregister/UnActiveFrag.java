package com.miaolian.facead.view.nfcregister;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaolian.facead.R;
import com.miaolian.facead.event.EventActiveFormal;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class UnActiveFrag extends Fragment {
    public UnActiveFrag() {
        // Required empty public constructor
    }

    public static UnActiveFrag newInstance() {
        UnActiveFrag fragment = new UnActiveFrag();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_un_active, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.activeFormalBtn)
    public void onClick() {
        EventBus.getDefault().post(new EventActiveFormal());
    }
}
