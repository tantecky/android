package cz.antecky.netswitch.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
    private NetController netController;

    private Switch wifiSwitch;
    private Switch mobileDataSwitch;

    private void onWifiSwitchClick(View view) {
        boolean checked = wifiSwitch.isChecked();
        boolean success = netController.setWifiEnabled(checked);

        if (!success) {
            wifiSwitch.setChecked(!checked);
        }
    }

    private void onMobileSwitchClick(View view) {
        boolean checked = mobileDataSwitch.isChecked();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            mobileDataSwitch.setChecked(!checked);
            ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE},
                    1);
            return;
        }

        boolean success = netController.setMobileDataEnabled(checked);

        if (!success) {
            mobileDataSwitch.setChecked(!checked);
        }

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

        Log.i("Run", "onCreate");


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Run", "onResume");

        wifiSwitch.setChecked(netController.isWifiEnabled());
        mobileDataSwitch.setChecked(netController.isMobileDataEnabled());
    }
}
