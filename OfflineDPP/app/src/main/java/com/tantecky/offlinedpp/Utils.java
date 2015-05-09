package com.tantecky.offlinedpp;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    private final static String sMY_TAG = "OFFLINEDPP_APP";

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static void log(int i) {
        log(Integer.toString(i));
    }

    public static void log(boolean i) {
        log(Boolean.toString(i));
    }

    public static void log(String msg) {
        Log.d(sMY_TAG, msg);
    }

    public static void logWTF(String msg) {
        Log.wtf(sMY_TAG, msg);
    }

    public static String readRawTextFile(Resources resources, int resId) {
        InputStream inputStream = resources.openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);

        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }

        return text.toString();
    }
}
