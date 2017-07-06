package com.miaolian.facead.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaolian.facead.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceAlignmentWaitingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceAlignmentWaitingFragment extends Fragment {


    // TODO: Rename and change types of parameters


    public FaceAlignmentWaitingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FaceAlignmentWaitingFragment newInstance() {
        FaceAlignmentWaitingFragment fragment = new FaceAlignmentWaitingFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facealignment_waiting, container, false);
    }

}
