package com.miaolian.facead.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.miaolian.facead.R;
import com.miaolian.facead.thread.CheckUpdateThread;

public class CheckUpdateService extends Service {
    CheckUpdateThread checkUpdateThread;
    public static final String ACTION_START_CHECK_UPDATE = "com.miaolian.action.START_CHECK_UPDATE";
    public static final String ACTION_CHECK_UPDATE_IMMEDIATELY = "com.miaolian.action.CHECK_UPDATE_IMMEDIATELY";
    Notification notification;
    private static final int NOTIFICATION_ID = 2;
    PendingIntent pi;

    public CheckUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkUpdateThread = new CheckUpdateThread(getApplicationContext());
        checkUpdateThread.start();
        Intent intent = new Intent(getApplicationContext(), CheckUpdateService.class);
        intent.setAction(ACTION_CHECK_UPDATE_IMMEDIATELY);
        pi = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.CHECK_UPDATE_SERVICE_START_UP))
                .setContentTitle(getString(R.string.CHECK_UPDATE_SERVICE_TITLE))
                .setContentText(getString(R.string.CHECK_UPDATE_SERVICE_CONTENT))
                .setContentIntent(pi)
                .setOngoing(true);
        notification = builder.getNotification();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = null;
        try {
            action = intent.getAction();
        } catch (Exception e) {
            action = null;
        }
        if (action == null) {
            return START_STICKY;
        }
        switch (action) {
            case ACTION_START_CHECK_UPDATE:
                break;
            case ACTION_CHECK_UPDATE_IMMEDIATELY:
                checkUpdateThread.checkUpdateImmediately();
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        checkUpdateThread.shutdown();
        super.onDestroy();
    }

    public static void startCheckUpdate(Context context) {
        Intent intent = new Intent(context, CheckUpdateService.class);
        intent.setAction(ACTION_START_CHECK_UPDATE);
        context.startService(intent);
    }

    public static void checkUpdateImmediately(Context context) {
        Intent intent = new Intent(context, CheckUpdateService.class);
        intent.setAction(ACTION_CHECK_UPDATE_IMMEDIATELY);
        context.startService(intent);
    }

    public static void stopCheckUpdate(Context context) {
        Intent intent = new Intent(context, CheckUpdateService.class);
        context.stopService(intent);
    }
}
