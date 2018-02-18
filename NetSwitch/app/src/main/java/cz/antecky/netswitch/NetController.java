package cz.antecky.netswitch;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

import static android.Manifest.permission.READ_PHONE_STATE;

public final class NetController {
    private final static String MOBILE_DATA = "mobile_data";
    private final static String TAG = "NetController";
    private static String TRANSACTION_CODE = null;

    private final Context appContext;
    private final TelephonyManager teleManager;
    private final WifiManager wifiManager;
    private final SubscriptionManager subManager;
    private final ContentResolver contentResolver;

    private String getTransactionCode() {
        if (TRANSACTION_CODE != null) {
            return TRANSACTION_CODE;
        }

        try {
            final Class<?> tClass = Class.forName(teleManager.getClass().getName());
            final Method tMethod = tClass.getDeclaredMethod("getITelephony");
            tMethod.setAccessible(true);
            final Object tStub = tMethod.invoke(teleManager);
            final Class<?> tSubClass = Class.forName(tStub.getClass().getName());
            final Class<?> mClass = tSubClass.getDeclaringClass();
            final Field field = mClass.getDeclaredField("TRANSACTION_setDataEnabled");
            field.setAccessible(true);

            TRANSACTION_CODE = String.valueOf(field.getInt(null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TRANSACTION_CODE == null) {
            final String error = "Unable to get transaction code.";
            Utils.logE(TAG, error);
            Utils.showToast(appContext, error);
        }

        return TRANSACTION_CODE;
    }

    private void grantPhonePermission() {
        Utils.logD(TAG, "trying to grant Phone permission");
        List<String> result = Shell.SU.run(
                String.format("pm grant %s android.permission.READ_PHONE_STATE",
                        appContext.getPackageName()));

        if (result == null) {
            final String error = "Unable to grant Phone permission.";
            Utils.logE(TAG, error);
            Utils.showToast(appContext, error);
        }
    }

    public boolean obtainPermissions() {
        if (!Shell.SU.available()) {
            final String error = "Root access required to toggle mobile data.";
            Utils.logE(TAG, error);
            Utils.showToast(appContext, error);
            return false;
        }

        if (appContext.checkPermission(READ_PHONE_STATE, Process.myPid(), Process.myUid())
                != PackageManager.PERMISSION_GRANTED) {
            grantPhonePermission();
            return false;
        }

        return true;

    }

    public NetController(Context context) {
        appContext = context.getApplicationContext();

        teleManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        subManager = (SubscriptionManager) appContext.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        contentResolver = appContext.getContentResolver();
    }

    public boolean isMobileDataEnabled() {
        if (teleManager.getSimState() == TelephonyManager.SIM_STATE_READY) {

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
        } else {
            Utils.logE(TAG, "SimState not SIM_STATE_READY");
            return false;
        }

    }

    public boolean setMobileDataEnabled(boolean value) {
        String transCode = getTransactionCode();
        int state = value ? 1 : 0;

        if (transCode == null) {
            return false;
        }

        if (!obtainPermissions()) {
            return false;
        }

        List<SubscriptionInfo> listInfo = subManager.getActiveSubscriptionInfoList();

        if (listInfo == null) {
            final String error = "SubscriptionInfo list is null.";
            Utils.logE(TAG, error);
            Utils.showToast(appContext, error);
            return false;
        }

        Utils.logD(TAG, "listInfo size: " + String.valueOf(listInfo.size()));

        for (SubscriptionInfo info : listInfo) {
            // Get the active subscription ID for a given SIM card.
            int subscriptionId = info.getSubscriptionId();
            String cmd = String.format("service call phone %s i32 %d i32 %d",
                    transCode, subscriptionId, state);

            List<String> result = Shell.SU.run(cmd);

            if (result == null) {
                final String error = "service call failed.";
                Utils.logE(TAG, error);
                Utils.showToast(appContext, error);
                return false;
            }
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
