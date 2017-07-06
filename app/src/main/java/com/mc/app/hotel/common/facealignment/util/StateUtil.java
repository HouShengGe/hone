package com.mc.app.hotel.common.facealignment.util;

import android.content.Context;
import android.nfc.NfcAdapter;

/**
 * Created by gaofeng on 2017-03-03.
 */

public class StateUtil {
    public static boolean SupportNFC;
    private static Context context;

    public static void init(Context context) {
        StateUtil.context = context;
        SupportNFC=NfcAdapter.getDefaultAdapter(context)!=null;
    }
}
