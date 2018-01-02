package cz.antecky.netswitch;

import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class NetController {
    private final static String MOBILE_DATA = "mobile_data";

    private TelephonyManager teleManager;
    private WifiManager wifiManager;
    private SubscriptionManager subManager;
    private Context appContext;

    private String getTransactionCode() {
        try {
            final TelephonyManager tManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            final Class<?> tClass = Class.forName(tManager.getClass().getName());
            final Method tMethod = tClass.getDeclaredMethod("getITelephony");
            tMethod.setAccessible(true);
            final Object tStub = tMethod.invoke(tManager);
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

    public NetController(Context appContext) {
        this.appContext = appContext;

        teleManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        subManager = (SubscriptionManager) appContext.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);

    }

    public boolean isMobileDataEnabled() {
        boolean mobileData = false;
        if (teleManager.getSimState() == TelephonyManager.SIM_STATE_READY) {

            mobileData = Settings.Global.getInt(appContext.getContentResolver(), MOBILE_DATA,
                    0) == 1;

        }

        return mobileData;
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

    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public boolean setWifiEnabled(boolean value) {
        return wifiManager.setWifiEnabled(value);
    }
}
