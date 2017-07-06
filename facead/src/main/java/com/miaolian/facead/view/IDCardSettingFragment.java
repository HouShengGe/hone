package com.miaolian.facead.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.miaolian.facead.R;
import com.miaolian.facead.event.EventSelectLinkType;
import com.miaolian.facead.util.PrefUtil;
import com.miaolian.facead.util.ServiceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class IDCardSettingFragment extends Fragment {


    @BindView(R.id.selectLinkTypeBtn)
    Button selectLinkTypeBtn;
    SelectLinkTypeDialogFragment selectLinkTypeDialogFragment;
    @BindView(R.id.setDecodeServerBtn)
    Button setDecodeServerBtn;
    @BindView(R.id.addrEt)
    EditText addrEt;
    @BindView(R.id.portEt)
    EditText portEt;
    @BindView(R.id.setDecodeServerPanel)
    MyExpandableView setDecodeServerPanel;

    public IDCardSettingFragment() {

    }


    public static IDCardSettingFragment newInstance() {
        IDCardSettingFragment fragment = new IDCardSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        selectLinkTypeDialogFragment = SelectLinkTypeDialogFragment.newInstance();
        View view = inflater.inflate(R.layout.fragment_idcard_setting, container, false);
        ButterKnife.bind(this, view);
        addrEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.e("onTextChanged:" + s);
                PrefUtil.setSpecificServerAddress(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        portEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int port = Integer.parseInt(s.toString());
                    PrefUtil.setSpecificServerPort(port);
                    Timber.e("onTextChanged:port=" + port);
                } catch (Exception e) {
                    Timber.e("onTextChanged:" + Log.getStackTraceString(e));
                    PrefUtil.setSpecificServerPort(-1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (PrefUtil.getUseSpecificServer()) {
            setDecodeServerPanel.expand(false);
            setDecodeServerBtn.setText(getString(R.string.DECODE_SERVER_MANUAL));
        } else {
            setDecodeServerBtn.setText(getString(R.string.DECODE_SERVER_AUTO));
        }
        switch (PrefUtil.getLinkType()) {
            case ServiceUtil.OCR:
                selectLinkTypeBtn.setText(getString(R.string.OCR_READ_CARD));
                break;
            case ServiceUtil.OTG:
                selectLinkTypeBtn.setText(getString(R.string.OTG_READ_CARD));
                break;
            case ServiceUtil.NFC:
                selectLinkTypeBtn.setText(getString(R.string.NFC_READ_CARD));
                break;
            case ServiceUtil.SERIALPORT:
                selectLinkTypeBtn.setText(getString(R.string.SERIALPORT_READ_CARD));
                break;
            case ServiceUtil.CAMERA:
                selectLinkTypeBtn.setText(getString(R.string.CAMERA_READ_CARD));
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addrEt.setText(PrefUtil.getSpecificServerAddress());
        portEt.setText(PrefUtil.getSpecificServerPort() <= 0 ? "" : String.valueOf(PrefUtil.getSpecificServerPort()));
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectLinkType(EventSelectLinkType event) {
        switch (event.getLinkType()) {
            case ServiceUtil.OCR:
                selectLinkTypeBtn.setText(getString(R.string.OCR_READ_CARD));
                break;
            case ServiceUtil.OTG:
                selectLinkTypeBtn.setText(getString(R.string.OTG_READ_CARD));
                break;
            case ServiceUtil.NFC:
                selectLinkTypeBtn.setText(getString(R.string.NFC_READ_CARD));
                break;
            case ServiceUtil.SERIALPORT:
                selectLinkTypeBtn.setText(getString(R.string.SERIALPORT_READ_CARD));
                break;
            case ServiceUtil.CAMERA:
                selectLinkTypeBtn.setText(getString(R.string.CAMERA_READ_CARD));
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.selectLinkTypeBtn, R.id.setDecodeServerBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectLinkTypeBtn:
                selectLinkTypeDialogFragment.show(getChildFragmentManager(), "");
                break;
            case R.id.setDecodeServerBtn:
                if (setDecodeServerBtn.getText().equals(getString(R.string.DECODE_SERVER_AUTO))) {
                    setDecodeServerBtn.setText(getString(R.string.DECODE_SERVER_MANUAL));
                    PrefUtil.setUseSpecificServer(true);
                    setDecodeServerPanel.expand(true);
                } else {
                    setDecodeServerBtn.setText(getString(R.string.DECODE_SERVER_AUTO));
                    PrefUtil.setUseSpecificServer(false);
                    setDecodeServerPanel.collapse(true);
                }
                break;
        }
    }

}
