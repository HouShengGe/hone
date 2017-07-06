package com.mc.app.hotel.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.mc.app.hotel.BuildConfig;
import com.mc.app.hotel.common.facealignment.util.OCRUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.ServiceUtil;
import com.mc.app.hotel.common.facealignment.util.StateUtil;

import timber.log.Timber;

/**
 * Created by baixiaokang on 16/4/23.
 */
public class App extends Application {
    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        PrefUtil.init(this);
        StateUtil.init(this);
        OCRUtil.init(this);
        if(StateUtil.SupportNFC==false){
            PrefUtil.setLinkType(ServiceUtil.OTG);
        }

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
    }

    public static Context getAppContext() {
        return mApp;
    }

    public static Resources getAppResources() {
        return mApp.getResources();
    }

}
