package com.miaolian.facead.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miaolian.facead.R;
import com.miaolian.facead.util.PrefUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetFragment extends Fragment {


    Unbinder unbinder;

    public ResetFragment() {
        // Required empty public constructor
    }

    public static ResetFragment newInstance() {
        ResetFragment fragment = new ResetFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.resetBtn)
    public void onViewClicked() {
        PrefUtil.reset();
        Toast.makeText(getContext(), R.string.RESET_SUCCESS, Toast.LENGTH_SHORT).show();
    }
}
