package com.mc.app.hotel.common.facealignment.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mc.app.hotel.R;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceAlignmentSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceAlignmentSettingFragment extends Fragment {


    @BindView(R.id.confidenceTv)
    TextView confidenceTv;
    @BindView(R.id.confidenceSeekBar)
    AppCompatSeekBar confidenceSeekBar;

    public FaceAlignmentSettingFragment() {
        // Required empty public constructor
    }

    public static FaceAlignmentSettingFragment newInstance() {
        FaceAlignmentSettingFragment fragment = new FaceAlignmentSettingFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_face_alignment_setting, container, false);
        ButterKnife.bind(this, view);
        confidenceTv.setText(String.valueOf((int) PrefUtil.getMinConfidence()));
        confidenceSeekBar.setProgress((int) PrefUtil.getMinConfidence());
        confidenceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                confidenceTv.setText(String.valueOf(progress));
                PrefUtil.setMinConfidence(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

}
