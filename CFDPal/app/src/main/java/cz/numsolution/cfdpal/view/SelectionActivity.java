package cz.numsolution.cfdpal.view;

/**
 * Created by Tomas Antecky on 21. 3. 2018.
 */

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);

        if (savedInstanceState == null) {
            if (Utils.isTwoPane(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container_master, SelectionFragment.newInstance())
                        .commit();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, SelectionFragment.newInstance())
                        .commit();

            }
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
