package cz.antecky.netswitch.ui;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import cz.antecky.netswitch.NetController;
import cz.antecky.netswitch.R;

import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {
    private final static int READ_PHONE_STATE_REQUEST = READ_PHONE_STATE.hashCode() & 0xFFFF;

    private NetController netController;

    private Switch wifiSwitch;
    private Switch mobileDataSwitch;

    private void onWifiSwitchClick(View view) {
        boolean checked = wifiSwitch.isChecked();
        Log.i("onWifiSwitchClick", String.valueOf(checked));

        boolean success = netController.setWifiEnabled(checked);

        if (!success) {
            wifiSwitch.setChecked(!checked);
        }
    }

    private void onMobileSwitchClick(View view) {
        boolean checked = mobileDataSwitch.isChecked();
        Log.i("onMobileSwitchClick", String.valueOf(checked));

        if (ContextCompat.checkSelfPermission(this, READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            mobileDataSwitch.setChecked(!checked);
            ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE},
                    READ_PHONE_STATE_REQUEST);
            return;
        }

        boolean success = netController.setMobileDataEnabled(checked);

        if (!success) {
            mobileDataSwitch.setChecked(!checked);
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
        setContentView(R.layout.activity_main);

        netController = new NetController(getApplicationContext());

        wifiSwitch = findViewById(R.id.switch_wifi);
        wifiSwitch.setOnClickListener(this::onWifiSwitchClick);

        mobileDataSwitch = findViewById(R.id.switch_mobile_data);
        mobileDataSwitch.setOnClickListener(this::onMobileSwitchClick);

        wifiSwitch.setChecked(netController.isWifiEnabled());
        mobileDataSwitch.setChecked(netController.isMobileDataEnabled());

        Log.i("Run", "onCreate");
    }


    @Override
    protected void onResume() {
        super.onResume();

        //  wifiSwitch.setChecked(netController.isWifiEnabled());
        //  mobileDataSwitch.setChecked(netController.isMobileDataEnabled());
        //   Log.i("onResume data", String.valueOf(netController.isMobileDataEnabled()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PHONE_STATE_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mobileDataSwitch.performClick();
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
            }
        }

    }
}
