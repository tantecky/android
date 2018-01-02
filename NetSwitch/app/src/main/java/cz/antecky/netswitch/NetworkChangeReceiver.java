package cz.antecky.netswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private final static String TAG = "NetworkChangeReceiver";

    private NetController netController;

    public NetworkChangeReceiver(NetController netController)
    {
        this.netController = netController;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isInitialStickyBroadcast()) {
            Log.i(TAG, "onReceive");
            netController.updateState();
            Log.i(TAG, "getIsMobileDataEnabled: " + String.valueOf(netController.getIsMobileDataEnabled()));
            Log.i(TAG, "getIsWifiEnabled: " + String.valueOf(netController.getIsWifiEnabled()));

        }
    }
}
