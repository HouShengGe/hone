package com.mc.app.hotel.common.facealignment.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by gaofeng on 2017-02-22.
 */

public class PlaySoundUtil {
    public static void play(final Context context, final int resId) {
        new Thread() {
            @Override
            public void run() {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(1, 1);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
                while (mediaPlayer.isPlaying()) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
