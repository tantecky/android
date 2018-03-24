package cz.numsolution.cfdpal.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.model.CalculationType;

public class CalculationActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_calculation);

        Intent intent = this.getIntent();
        CharSequence calculationName = intent.getStringExtra(EXTRA_CALCULATION_NAME);
        setTitle(calculationName);
    }
}
