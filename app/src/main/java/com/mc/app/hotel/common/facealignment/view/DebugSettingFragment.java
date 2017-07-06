package com.mc.app.hotel.common.facealignment.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.UMengUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebugSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebugSettingFragment extends Fragment {


    @BindView(R.id.setDebugModeBtn)
    Button setDebugModeBtn;
    @BindView(R.id.debugIDEt)
    EditText debugIDEt;
    @BindView(R.id.setDebugModePanel)
    MyExpandableView setDebugModePanel;

    public DebugSettingFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static DebugSettingFragment newInstance() {
        DebugSettingFragment fragment = new DebugSettingFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debug_setting, container, false);
        ButterKnife.bind(this, view);
        debugIDEt.setText(UMengUtil.generateDebugID(getContext()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PrefUtil.getDebugMode()) {
            setDebugModePanel.expand(false);
            setDebugModeBtn.setText(R.string.ON);
        }
    }

    @OnClick({R.id.setDebugModeBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setDebugModeBtn: {
                if (setDebugModeBtn.getText().equals(getString(R.string.OFF))) {
                    setDebugModeBtn.setText(R.string.ON);
                    PrefUtil.setDebugMode(true);
                    setDebugModePanel.expand(true);
                } else {
                    setDebugModeBtn.setText(R.string.OFF);
                    PrefUtil.setDebugMode(false);
                    setDebugModePanel.collapse(true);
                }
                break;
            }
            default: {
                break;
            }
        }

    }
}
