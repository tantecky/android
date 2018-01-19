package cz.antecky.netswitch;

import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.Manifest.permission.READ_PHONE_STATE;

public final class NetController extends BroadcastReceiver {
    private final static String MOBILE_DATA = "mobile_data";
    private final static String TAG = "NetController";

    private Context appContext;
    private TelephonyManager teleManager;
    private WifiManager wifiManager;
    private SubscriptionManager subManager;
    private ContentResolver contentResolver;

    private MutableLiveData<Boolean> isMobileDataEnabled;
    private MutableLiveData<Boolean> isWifiEnabled;

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

    private void grantPhonePermission() {
        Log.v(TAG, "grantPhonePermission");
        Command command = new Command(0, "pm grant cz.antecky.netswitch android.permission.READ_PHONE_STATE");

        try {
            RootTools.getShell(true).add(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(appContext, "Perm granted", Toast.LENGTH_LONG).show();
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
        this.appContext = appContext;

        teleManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        subManager = (SubscriptionManager) appContext.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        contentResolver = appContext.getContentResolver();


        updateState();
    }

    private void updateState() {
        boolean dataEnabled = determineIsMobileDataEnabled();

        if (isMobileDataEnabled == null) {
            isMobileDataEnabled = new MutableLiveData<>();
            isMobileDataEnabled.setValue(dataEnabled);
        } else if (dataEnabled != isMobileDataEnabled.getValue().booleanValue()) {
            isMobileDataEnabled.setValue(dataEnabled);
        }

        boolean wifiEnable = determineIsWifiEnabled();

        if (isWifiEnabled == null) {
            isWifiEnabled = new MutableLiveData<>();
            isWifiEnabled.setValue(wifiEnable);
        } else if (wifiEnable != isWifiEnabled.getValue().booleanValue()) {
            isWifiEnabled.setValue(wifiEnable);
        }

    }

    public void observeMobileData(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<Boolean> observer) {

        isMobileDataEnabled.observe(owner, observer);
    }

    public void observeWifi(@NonNull LifecycleOwner owner,
                            @NonNull Observer<Boolean> observer) {

        isWifiEnabled.observe(owner, observer);
    }

    public boolean getIsMobileDataEnabled() {
        return isMobileDataEnabled.getValue().booleanValue();
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

        if (ContextCompat.checkSelfPermission(appContext, READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            grantPhonePermission();
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
        return isWifiEnabled.getValue().booleanValue();
    }

    public boolean setWifiEnabled(boolean value) {
        return wifiManager.setWifiEnabled(value);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isInitialStickyBroadcast()) {
            Log.i(TAG, "onReceive");
            updateState();
            Log.i(TAG, "getIsMobileDataEnabled: " + String.valueOf(getIsMobileDataEnabled()));
            Log.i(TAG, "getIsWifiEnabled: " + String.valueOf(getIsWifiEnabled()));

        }
    }
}
