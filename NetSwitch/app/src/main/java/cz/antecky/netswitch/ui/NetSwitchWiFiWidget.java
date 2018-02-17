package cz.antecky.netswitch.ui;

import android.content.Context;

public class NetSwitchWiFiWidget extends NetSwitchWidget {
    @Override
    protected boolean ShowMobileButton() {
        return false;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        // No dangerous/root permissions required
    }
}

