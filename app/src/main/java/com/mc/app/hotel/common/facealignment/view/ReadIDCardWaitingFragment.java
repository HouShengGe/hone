package com.mc.app.hotel.common.facealignment.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.app.hotel.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadIDCardWaitingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadIDCardWaitingFragment extends Fragment {


    public ReadIDCardWaitingFragment() {
        // Required empty public constructor
    }


    public static ReadIDCardWaitingFragment newInstance() {
        ReadIDCardWaitingFragment fragment = new ReadIDCardWaitingFragment();
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

        return inflater.inflate(R.layout.fragment_read_idcard_waiting, container, false);
    }
}
