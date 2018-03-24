package cz.numsolution.cfdpal.view;

/**
 * Created by Tomas Antecky on 21. 3. 2018.
 */

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cz.numsolution.cfdpal.R;

public class SelectionActivity extends AppCompatActivity implements SelectionView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        SelectionFragment selectionFragment = new SelectionFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, selectionFragment).commit();
    }

    @Override
    public void onCalculationSelected() {

    }
}
