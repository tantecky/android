package com.tantecky.offlinedpp;

import android.util.Log;

public class Utils {
    private final static String sMY_TAG = "OFFLINEDPP_APP";

    public static boolean IsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static void log(int i) {
        log(Integer.toString(i));
    }

    public static void log(String msg) {
        Log.d(sMY_TAG, msg);
    }

    public static void logWTF(String msg) {
        Log.wtf(sMY_TAG, msg);
    }
}
