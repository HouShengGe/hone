package com.mc.app.hotel.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.mc.app.hotel.BuildConfig;
import com.mc.app.hotel.common.facealignment.util.OCRUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.ServiceUtil;
import com.mc.app.hotel.common.facealignment.util.StateUtil;
import com.mc.app.hotel.common.util.SPerfUtil;

import java.util.Stack;

import timber.log.Timber;

/**
 * Created by baixiaokang on 16/4/23.
 */
public class App extends Application {
    private static App mApp;
    public static Stack<Activity> store;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        store = new Stack<>();
        PrefUtil.init(this);
        SPerfUtil.init(this);
        StateUtil.init(this);
        OCRUtil.init(this);
        if (StateUtil.SupportNFC == false) {
            PrefUtil.setLinkType(ServiceUtil.OTG);
        }
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        Timber.uprootAll();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + "[LINE]" + element.getLineNumber();
                }
            });
        } else {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + "[LINE]" + element.getLineNumber();
                }

                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    if (priority == Log.VERBOSE || priority == Log.DEBUG)
                        return;
                    super.log(priority, tag, message, t);
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
        }
    }

    public static Context getAppContext() {
        return mApp;
    }

    public static Resources getAppResources() {
        return mApp.getResources();
    }

    private class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }
}
