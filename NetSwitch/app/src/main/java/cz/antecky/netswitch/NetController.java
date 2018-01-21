package cz.antecky.netswitch;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.Manifest.permission.READ_PHONE_STATE;

public final class NetController extends BroadcastReceiver {
    private final static String MOBILE_DATA = "mobile_data";
    private final static String TAG = "NetController";
    private static String TRANSACTION_CODE = null;

    private Context appContext;
    private TelephonyManager teleManager;
    private WifiManager wifiManager;
    private SubscriptionManager subManager;
    private ContentResolver contentResolver;

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
            final String error = "Unable to get transaction code";
            Log.e(TAG, error);
            Toast.makeText(appContext, error, Toast.LENGTH_LONG).show();
        }

        return TRANSACTION_CODE;
    }

    private void grantPhonePermission() {
        Log.v(TAG, "trying to grant Phone permission");
        Command command = new Command(0,
                String.format("pm grant %s android.permission.READ_PHONE_STATE",
                        appContext.getPackageName()));
        try {
            RootTools.getShell(true).add(command);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(appContext, "Unable to grant Phone permission", Toast.LENGTH_LONG).show();
        }
    }

    public boolean obtainPermissions() {
        if (!RootTools.isAccessGiven(0, 0)) {
            Toast.makeText(appContext, "Root access required", Toast.LENGTH_LONG).show();
            return false;
        }

        if (ContextCompat.checkSelfPermission(appContext, READ_PHONE_STATE)
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
                    final String error = "Unable to get mobile data state";
                    Log.e(TAG, error);
                    Toast.makeText(appContext, error, Toast.LENGTH_LONG).show();
                    return false;
            }
        } else
            return false;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isInitialStickyBroadcast()) {
            Log.i(TAG, "onReceive");
            Log.i(TAG, "isMobileDataEnabled: " + String.valueOf(isMobileDataEnabled()));
            Log.i(TAG, "isWifiEnabled: " + String.valueOf(isWifiEnabled()));

        }
    }
}
