package com.miaolian.facead.view.nfcregister;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.caihua.cloud.common.service.NFCActiveService;
import com.miaolian.facead.R;
import com.miaolian.facead.event.EventActiveFormal;
import com.miaolian.facead.event.EventDoActive;
import com.miaolian.facead.model.ActiveInfo;
import com.miaolian.facead.util.ServiceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NFCActiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NFCActiveFragment extends Fragment {
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    FormalUserFrag formalUserFrag;
    ActiveFrag activeFrag;
    UnActiveFrag unActiveFrag;
    WaitFragment waitFragment;


    public NFCActiveFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static NFCActiveFragment newInstance() {
        NFCActiveFragment fragment = new NFCActiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formalUserFrag = FormalUserFrag.newInstance();
        activeFrag = ActiveFrag.newInstance();
        unActiveFrag = UnActiveFrag.newInstance();
        waitFragment = WaitFragment.newInstance();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceUtil.NFC_SEARCH_FINISH);
        intentFilter.addAction(ServiceUtil.NFC_REGISTER_FINISH);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if (action.equals(ServiceUtil.NFC_SEARCH_FINISH)) {
                    if (intent.getBooleanExtra(ServiceUtil.NFC_SEARCH_RESULT, false)) {
                        final Bundle args = new Bundle();
                        final ActiveInfo activeInfo = new ActiveInfo();
                        activeInfo.setActCode(intent.getStringExtra(ServiceUtil.ACTIVE_CODE));
                        activeInfo.setStartTime(intent.getLongExtra(ServiceUtil.START_TIME, 0));
                        activeInfo.setEndTime(intent.getLongExtra(ServiceUtil.END_TIME, 0));
                        args.putParcelable(ActiveInfo.class.getName(), activeInfo);
                        String userType = intent.getStringExtra(ServiceUtil.USER_TYPE);
                        if (userType.equals(ServiceUtil.TRAIL_USER)) {
                            Toast.makeText(getContext(), "试用通道已关闭，请注册为正式用户", Toast.LENGTH_SHORT).show();
                            showFrag(unActiveFrag);

                        } else if (userType.equals(ServiceUtil.FORMAL_USER)) {
                            Toast.makeText(getContext(), "正式用户", Toast.LENGTH_SHORT).show();
                            formalUserFrag.setArguments(args);
                            showFrag(formalUserFrag);
                        }
                    } else {
                        Toast.makeText(context, intent.getStringExtra(ServiceUtil.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                        showFrag(unActiveFrag);
                    }
                } else if (action.equals(ServiceUtil.NFC_REGISTER_FINISH)) {
                    if (intent.getBooleanExtra(ServiceUtil.NFC_REGISTER_RESULT, false)) {
                        final Bundle args = new Bundle();
                        final ActiveInfo activeInfo = new ActiveInfo();
                        activeInfo.setActCode(intent.getStringExtra(ServiceUtil.ACTIVE_CODE));
                        activeInfo.setStartTime(intent.getLongExtra(ServiceUtil.START_TIME, 0));
                        activeInfo.setEndTime(intent.getLongExtra(ServiceUtil.END_TIME, 0));
                        args.putParcelable(ActiveInfo.class.getName(), activeInfo);
                        formalUserFrag.setArguments(args);
                        showFrag(formalUserFrag);
                    } else {
                        Toast.makeText(context, "激活失败" + intent.getStringExtra("ERROR_MESSAGE"), Toast.LENGTH_SHORT).show();
                        showFrag(unActiveFrag);
                    }
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nfcactive, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        showFrag(waitFragment);
        getContext().registerReceiver(broadcastReceiver, intentFilter);
        Intent intent = new Intent(getContext(), NFCActiveService.class);
        intent.setAction(ServiceUtil.NFC_SEARCH);
        getContext().startService(intent);

    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        try {
            getContext().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {

        }
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventActiveFormal(EventActiveFormal event) {
        showFrag(activeFrag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDoActive(EventDoActive event) {
        showFrag(waitFragment);
        Intent intent = new Intent(getContext(), NFCActiveService.class);
        intent.setAction(ServiceUtil.NFC_REGISTER);
        intent.putExtra(ServiceUtil.ACTIVE_CODE, event.getActiveCode());
        getContext().startService(intent);
    }


    private void showFrag(Fragment frag) {
        try {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.fragContainer, frag).commit();
        } catch (Exception e) {
            Timber.e("showFrag:" + Log.getStackTraceString(e));
        }
    }
}
