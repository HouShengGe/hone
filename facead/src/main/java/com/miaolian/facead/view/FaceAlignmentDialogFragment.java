package com.miaolian.facead.view;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.miaolian.facead.R;


public class FaceAlignmentDialogFragment extends DialogFragment {
    private static final String TAG = FaceAlignmentDialogFragment.class.getSimpleName();
    public static final int WAIT_FRAGMENT = 0;
    public static final int SUCCESS_FRAGMENT = 1;
    public static final int FAILED_FRAGMENT = 2;
    private static final String CURRENT_FRAGMENT_INDEX_KEY = "CURRENT_FRAGMENT_INDEX_KEY";
    FaceAlignmentWaitingFragment faceAlignmentWaitingFragment;
    FaceAlignmentFailedFragment faceAlignmentFailedFragment;
    FaceAlignmentSuccessFragment faceAlignmentSuccessFragment;


    public FaceAlignmentDialogFragment() {
        faceAlignmentFailedFragment = FaceAlignmentFailedFragment.newInstance();
        faceAlignmentSuccessFragment = FaceAlignmentSuccessFragment.newInstance();
        faceAlignmentWaitingFragment = FaceAlignmentWaitingFragment.newInstance();
    }


    public static FaceAlignmentDialogFragment newInstance() {
        FaceAlignmentDialogFragment fragment = new FaceAlignmentDialogFragment();
        fragment.setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_face_alignment_dialog, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) return;
        switch (args.getInt(CURRENT_FRAGMENT_INDEX_KEY, WAIT_FRAGMENT)) {
            case WAIT_FRAGMENT: {
                showFragment(faceAlignmentWaitingFragment);
                break;
            }
            case SUCCESS_FRAGMENT: {
                showFragment(faceAlignmentSuccessFragment);
                break;
            }
            case FAILED_FRAGMENT: {
                showFragment(faceAlignmentFailedFragment);
                break;
            }
        }

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

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.fragContainer, fragment);
        ft.commit();
    }

    public void showFragment(FragmentManager fm, int fragmentIndex) {
        Fragment fragment;
        switch (fragmentIndex) {
            case FAILED_FRAGMENT: {
                fragment = faceAlignmentFailedFragment;
                break;
            }
            case SUCCESS_FRAGMENT: {
                fragment = faceAlignmentSuccessFragment;
                break;
            }
            case WAIT_FRAGMENT: {
                fragment = faceAlignmentWaitingFragment;
                break;
            }
            default: {
                fragment = faceAlignmentWaitingFragment;
                break;
            }
        }
        if (getDialog() != null && getDialog().isShowing()) {
            showFragment(fragment);
        } else {
            Bundle args = new Bundle();
            args.putInt(CURRENT_FRAGMENT_INDEX_KEY, fragmentIndex);
            setArguments(args);
            show(fm, "");
        }
    }


    @Override
    public void dismiss() {
        if (getDialog() != null && getDialog().isShowing()) {
            super.dismiss();
        }
    }
}
