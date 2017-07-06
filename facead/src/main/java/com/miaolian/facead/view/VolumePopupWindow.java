package com.miaolian.facead.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.miaolian.facead.R;

/**
 * Created by user1 on 2017/5/19.
 */

public class VolumePopupWindow extends PopupWindow {
    public static final String TAG = VolumePopupWindow.class.getSimpleName();
    Context context;
    int popupWindowWidth = 0;
    int popupWindowHeight = 0;
    SeekBar volumeSeekBar;
    TextView volumeTv;
    boolean trackingTouch = false;
    AudioManager am;
    int maxVolume = 0;

    public VolumePopupWindow(final Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.view_volume_popupwindow, null), context.getResources().getDimensionPixelOffset(R.dimen.volume_popupwindow_width), context.getResources().getDimensionPixelOffset(R.dimen.volume_popupwindow_height), true);
        this.context = context;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar = (SeekBar) getContentView().findViewById(R.id.volumeSeekBar);
        volumeTv = (TextView) getContentView().findViewById(R.id.volumeTv);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (trackingTouch) {
                    volumeTv.setText(String.valueOf(progress));
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) ((float) progress / 100 * maxVolume), 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                trackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                trackingTouch = false;
            }
        });
        popupWindowWidth = context.getResources().getDimensionPixelOffset(R.dimen.volume_popupwindow_width);
        popupWindowHeight = context.getResources().getDimensionPixelOffset(R.dimen.volume_popupwindow_height);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.PopupWindowAnimation);
    }

    public void show(View view) {
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int volumePercent = (int) (((float) currentVolume / maxVolume) * 100);
        volumeSeekBar.setProgress(volumePercent);
        volumeTv.setText(String.valueOf(volumePercent));
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        showAtLocation(view.getRootView(), Gravity.NO_GRAVITY, location[0] + (view.getMeasuredWidth() - popupWindowWidth) / 2, location[1] + (int) (view.getMeasuredHeight() * 1.1));
    }
}
