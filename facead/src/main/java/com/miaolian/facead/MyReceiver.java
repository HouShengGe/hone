package com.miaolian.facead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = MyReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = null;
        try {
            action = intent.getAction();
        } catch (Exception e) {
            action = null;
        }
        if (action == null) return;

        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            default:
                break;
        }
    }
}
