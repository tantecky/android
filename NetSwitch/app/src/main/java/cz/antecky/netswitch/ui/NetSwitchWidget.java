package cz.antecky.netswitch.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;

import cz.antecky.netswitch.NetChangeJobService;
import cz.antecky.netswitch.NetController;
import cz.antecky.netswitch.R;


public class NetSwitchWidget extends AppWidgetProvider {

    private final static String MOBILE_DATA_CLICKED = "mobile_data_clicked";
    private final static String WIFI_CLICKED = "wifi_clicked";
    private final static String TAG = "NetSwitchWidget";

    private RemoteViews getRemoteViews(Context context, NetController nt) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.net_switch_widget);

        views.setOnClickPendingIntent(R.id.button_mobile_data,
                getPendingSelfIntent(context, MOBILE_DATA_CLICKED));
        views.setOnClickPendingIntent(R.id.button_wifi,
                getPendingSelfIntent(context, WIFI_CLICKED));

        Resources resources = context.getResources();

        views.setTextColor(R.id.button_mobile_data,
                nt.isMobileDataEnabled() ? resources.getColor(R.color.green)
                        : resources.getColor(R.color.red));

        views.setTextColor(R.id.button_wifi,
                nt.isWifiEnabled() ? resources.getColor(R.color.green)
                        : resources.getColor(R.color.red));

        return views;
    }

    public static void requestUpdate(Context context) {
        Log.v(TAG, "requestUpdate");
        Intent intent = new Intent(context, NetSwitchWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, NetSwitchWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, NetSwitchWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        NetController nt = new NetController(context);
        RemoteViews views = getRemoteViews(context, nt);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        NetController nt = new NetController(context);
        nt.obtainPermissions();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void onNetToggled(Context context) {
        Log.v(TAG, "onNetToggled");

        NetChangeJobService.schedule(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        Log.v(TAG, "action received: " + action);

        if (action.equals(WIFI_CLICKED)) {
            NetController nt = new NetController(context);

            if (nt.setWifiEnabled(!nt.isWifiEnabled())) {
                onNetToggled(context);
            }
        } else if (action.equals(MOBILE_DATA_CLICKED)) {
            NetController nt = new NetController(context);

            if (nt.setMobileDataEnabled(!nt.isMobileDataEnabled())) {
                onNetToggled(context);
            }

        }
    }
}

