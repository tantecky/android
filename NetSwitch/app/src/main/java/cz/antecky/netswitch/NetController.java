package cz.antecky.netswitch;

import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import android.arch.lifecycle.ViewModel;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class NetController extends ViewModel {
    private final static String MOBILE_DATA = "mobile_data";

    private TelephonyManager teleManager;
    private WifiManager wifiManager;
    private SubscriptionManager subManager;
    private ContentResolver contentResolver;

    private boolean isMobileDataEnabled;
    private boolean isWifiEnabled;
    private BroadcastReceiver networkChangeReceiver;

    private String getTransactionCode() {
        try {
            final Class<?> tClass = Class.forName(teleManager.getClass().getName());
            final Method tMethod = tClass.getDeclaredMethod("getITelephony");
            tMethod.setAccessible(true);
            final Object tStub = tMethod.invoke(teleManager);
            final Class<?> tSubClass = Class.forName(tStub.getClass().getName());
            final Class<?> mClass = tSubClass.getDeclaringClass();
            final Field field = mClass.getDeclaredField("TRANSACTION_setDataEnabled");
            field.setAccessible(true);

            return String.valueOf(field.getInt(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private boolean determineIsMobileDataEnabled() {
        boolean mobileData = false;
        if (teleManager.getSimState() == TelephonyManager.SIM_STATE_READY) {

            mobileData = Settings.Global.getInt(contentResolver, MOBILE_DATA,
                    0) == 1;

        }

        return mobileData;
    }

    private boolean determineIsWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public NetController(Context appContext) {

        teleManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        subManager = (SubscriptionManager) appContext.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        contentResolver = appContext.getContentResolver();
        networkChangeReceiver = new NetworkChangeReceiver(this);

        updateState();
    }

    public void updateState() {
        boolean dataEnabled = determineIsMobileDataEnabled();

        if (dataEnabled != isMobileDataEnabled) {
            isMobileDataEnabled = dataEnabled;
        }

        boolean wifiEnable = determineIsWifiEnabled();

        if (wifiEnable != isWifiEnabled) {
            isWifiEnabled = wifiEnable;
        }

    }

    public BroadcastReceiver getNetworkChangeReceiver() {
        return networkChangeReceiver;
    }

    public boolean getIsMobileDataEnabled() {
        return isMobileDataEnabled;
    }

    public boolean setMobileDataEnabled(boolean value) {
        String transCode = getTransactionCode();
        int state = value ? 1 : 0;

        if (transCode == null && transCode.length() > 0) {
            return false;
        }

        if (!RootTools.isAccessGiven(0, 0)) {
            return false;
        }

        // Loop through the subscription list i.e. SIM list.
        for (int i = 0; i < subManager.getActiveSubscriptionInfoCountMax(); i++) {
            // Get the active subscription ID for a given SIM card.
            int subscriptionId = subManager.getActiveSubscriptionInfoList().get(i).getSubscriptionId();
            String cmd = String.format("service call phone %s i32 %d i32 %d",
                    transCode, subscriptionId, state);

            Command command = new Command(0, cmd);
            try {
                RootTools.getShell(true).add(command);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public boolean getIsWifiEnabled() {
        return isWifiEnabled;
    }

    public boolean setWifiEnabled(boolean value) {
        return wifiManager.setWifiEnabled(value);
    }
}
