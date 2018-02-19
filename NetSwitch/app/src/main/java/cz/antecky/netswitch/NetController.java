package cz.antecky.netswitch;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;


public final class NetController {
    private final static String MOBILE_DATA = "mobile_data";
    private final static String TAG = "NetController";

    private final Context appContext;
    private final WifiManager wifiManager;
    private final ContentResolver contentResolver;


    public boolean obtainPermissions() {
        if (!Shell.SU.available()) {
            final String error = "Root access required to toggle mobile data.";
            Utils.logE(TAG, error);
            Utils.showToast(appContext, error);
            return false;
        }

        return true;

    }

    @SuppressLint("WifiManagerPotentialLeak")
    public NetController(Context context) {
        appContext = context.getApplicationContext();

        wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        contentResolver = appContext.getContentResolver();
    }

    public boolean isMobileDataEnabled() {

        switch (Settings.Global.getInt(contentResolver, MOBILE_DATA, -1)) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                final String error = "Unable to get mobile data state.";
                Utils.logE(TAG, error);
                Utils.showToast(appContext, error);
                return false;
        }

    }

    public boolean setMobileDataEnabled(boolean value) {

        if (!obtainPermissions()) {
            return false;
        }

        final String state = value ? "enable" : "disable";

        String cmd = String.format("svc data %s", state);

        List<String> result = Shell.SU.run(cmd);

        if (result == null) {
            final String error = "svc data";
            Utils.logE(TAG, error);
            Utils.showToast(appContext, error);
            return false;
        }

        return true;
    }

    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public boolean setWifiEnabled(boolean value) {
        return wifiManager.setWifiEnabled(value);
    }

    public void logCurrentState() {
        Utils.logD(TAG, "logCurrentState");
        Utils.logD(TAG, "isMobileDataEnabled: " + String.valueOf(isMobileDataEnabled()));
        Utils.logD(TAG, "isWifiEnabled: " + String.valueOf(isWifiEnabled()));
    }
}