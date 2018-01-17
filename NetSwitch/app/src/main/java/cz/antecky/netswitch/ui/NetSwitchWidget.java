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

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId,
                                        NetSwitchWidget widget, NetController nt) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.net_switch_widget);

        views.setOnClickPendingIntent(R.id.button_mobile_data,
                widget.getPendingSelfIntent(context, MOBILE_DATA_CLICKED));
        views.setOnClickPendingIntent(R.id.button_wifi,
                widget.getPendingSelfIntent(context, WIFI_CLICKED));

        Resources resources = context.getResources();

        views.setTextColor(R.id.button_mobile_data,
                nt.getIsMobileDataEnabled() ? resources.getColor(R.color.green)
                        : resources.getColor(R.color.red));

        views.setTextColor(R.id.button_wifi,
                nt.getIsWifiEnabled() ? resources.getColor(R.color.green)
                        : resources.getColor(R.color.red));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, this, nt);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void onButtonClicked(Context context) {
        Log.v(TAG, "onButtonClicked");

      /* RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.net_switch_widget);
        views.setBoolean(R.id.button_mobile_data, "setEnabled", false);
        views.setBoolean(R.id.button_wifi, "setEnabled", false);

        requestUpdate(context);*/
        NetChangeJobService.schedule(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        Log.v(TAG, "Action received = " + action);

        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            //NetChangeJobService.schedule(context);
        } else if (action.equals(WIFI_CLICKED)) {
            onButtonClicked(context);
            NetController nt = new NetController(context);
            nt.setWifiEnabled(!nt.getIsWifiEnabled());
        } else if (action.equals(MOBILE_DATA_CLICKED)) {
            onButtonClicked(context);
            NetController nt = new NetController(context);
            nt.setMobileDataEnabled(!nt.getIsMobileDataEnabled());
        }
    }
}

