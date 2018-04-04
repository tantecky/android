package cz.numsolution.cfdpal.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.model.CalculationType;

public class CalculationActivity extends AppCompatActivity {

    private static final String TAG = "CalculationActivity";

    private static final String EXTRA_CALCULATION_TYPE = "CalculationType";
    private static final String EXTRA_CALCULATION_NAME = "CalculationName";

    public static void start(Context context, @CalculationType int calcType,
                             CharSequence calculationName) {
        Intent intent = new Intent(context, CalculationActivity.class);
        intent.putExtra(EXTRA_CALCULATION_TYPE, calcType);
        intent.putExtra(EXTRA_CALCULATION_NAME, calculationName);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.logD(TAG, "onCreate");
        setContentView(R.layout.activity_calculation);

        Intent intent = this.getIntent();
        CharSequence calculationName = intent.getStringExtra(EXTRA_CALCULATION_NAME);
        setTitle(calculationName);

        if (savedInstanceState == null) {
            @CalculationType int calcType = intent.getIntExtra(EXTRA_CALCULATION_TYPE,
                    CalculationType.UNKNOWN);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, CalculationFragment.newInstance(calcType))
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuWeb:
                Utils.openUrl(this, getString(R.string.web_url));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
