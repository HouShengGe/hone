package com.mc.app.hotel.common.facealignment;

import android.app.Application;
import android.util.Log;

import com.mc.app.hotel.BuildConfig;
import com.mc.app.hotel.common.facealignment.util.OCRUtil;
import com.mc.app.hotel.common.facealignment.util.PrefUtil;
import com.mc.app.hotel.common.facealignment.util.ServiceUtil;
import com.mc.app.hotel.common.facealignment.util.StateUtil;

import timber.log.Timber;

/**
 * Created by gaofeng on 2017-02-28.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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
}
