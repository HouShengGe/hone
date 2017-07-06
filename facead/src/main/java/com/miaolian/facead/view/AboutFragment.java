package com.miaolian.facead.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caihua.cloud.common.util.DeviceIdUtil;
import com.miaolian.facead.R;
import com.miaolian.facead.service.CheckUpdateService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    @BindView(R.id.deviceIDTv)
    TextView deviceIDTv;
    Unbinder unbinder;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, view);
        deviceIDTv.setText(getString(R.string.DEVICE_ID) + DeviceIdUtil.generateDeviceId(getContext()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.checkUpdateBtn)
    public void onViewClicked() {
        CheckUpdateService.checkUpdateImmediately(getContext());
    }
}
