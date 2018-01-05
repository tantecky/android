package cz.antecky.netswitch.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import cz.antecky.netswitch.NetController;
import cz.antecky.netswitch.R;

import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {
    private final static int READ_PHONE_STATE_REQUEST = READ_PHONE_STATE.hashCode() & 0xFFFF;
    private final static String TAG = "MainActivity";

    private NetController netController;

    private Switch wifiSwitch;
    private Switch mobileDataSwitch;

    private void onWifiSwitchChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i(TAG, "onWifiSwitchChanged: " + String.valueOf(isChecked));

        boolean success = netController.setWifiEnabled(isChecked);

        if (!success) {
            wifiSwitch.setOnCheckedChangeListener(null);
            wifiSwitch.toggle();
            wifiSwitch.setOnCheckedChangeListener(this::onWifiSwitchChanged);
        }
    }

    private void onMobileSwitchChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i(TAG, "onMobileSwitchChanged: " + String.valueOf(isChecked));

        if (ContextCompat.checkSelfPermission(this, READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE},
                    READ_PHONE_STATE_REQUEST);
            return;
        }

        boolean success = netController.setMobileDataEnabled(isChecked);

        if (!success) {
            mobileDataSwitch.setOnCheckedChangeListener(null);
            mobileDataSwitch.toggle();
            mobileDataSwitch.setOnCheckedChangeListener(this::onMobileSwitchChanged);
        }

    }

    private void goToSettings() {
        Intent appSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        appSettings.addCategory(Intent.CATEGORY_DEFAULT);
        appSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(appSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        netController = new NetController(getApplicationContext());

        wifiSwitch = findViewById(R.id.switch_wifi);
        wifiSwitch.setOnCheckedChangeListener(this::onWifiSwitchChanged);

        mobileDataSwitch = findViewById(R.id.switch_mobile_data);
        mobileDataSwitch.setOnCheckedChangeListener(this::onMobileSwitchChanged);

        wifiSwitch.setChecked(netController.getIsWifiEnabled());
        mobileDataSwitch.setChecked(netController.getIsMobileDataEnabled());

        registerReceiver(netController, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        netController.observeMobileData(this, value -> {
            Log.i("observeMobileData", value.toString());
            mobileDataSwitch.setOnCheckedChangeListener(null);
            mobileDataSwitch.setChecked(value);
            mobileDataSwitch.setOnCheckedChangeListener(this::onMobileSwitchChanged);
        });

        netController.observeWifi(this, value -> {
            Log.i("observeWifi", value.toString());
            wifiSwitch.setOnCheckedChangeListener(null);
            wifiSwitch.setChecked(value);
            wifiSwitch.setOnCheckedChangeListener(this::onWifiSwitchChanged);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

        unregisterReceiver(netController);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PHONE_STATE_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMobileSwitchChanged(null, mobileDataSwitch.isChecked());
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        READ_PHONE_STATE)) {
                    Snackbar.make(mobileDataSwitch, "Phone permission request was denied.",
                            Snackbar.LENGTH_LONG)
                            .show();

                } else {
                    Snackbar snackbar = Snackbar.make(mobileDataSwitch,
                            "Phone permission request was denied.",
                            Snackbar.LENGTH_LONG);
                    snackbar.setAction("CHANGE", v -> goToSettings());
                    snackbar.show();
                }
                mobileDataSwitch.setOnCheckedChangeListener(null);
                mobileDataSwitch.toggle();
                mobileDataSwitch.setOnCheckedChangeListener(this::onMobileSwitchChanged);
            }
        }
    }
}
