package cz.numsolution.cfdpal;

import android.app.Activity;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.numsolution.cfdpal.view.SelectionActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectionActivityTest {

    private Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        onView(isRoot()).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                activity[0] = (Activity) view.getContext();
            }
        });
        return activity[0];
    }

    @Rule
    public ActivityTestRule<SelectionActivity> mActivityRule =
            new ActivityTestRule(SelectionActivity.class);

    @Test
    public void clickOnHieghtCalculation() {
        onView(withId(R.id.btnHeight)).perform(click());

        Activity activity = getCurrentActivity();
        assertThat(activity.getTitle().toString(),
                is(activity.getResources().getString(R.string.first_cell_height)));

    }

    @Test
    @Ignore
    public void clickOnQuantitiesCalculation() {
        onView(withId(R.id.btnQuantities)).perform(click());

        Activity activity = getCurrentActivity();
        assertThat(activity.getTitle().toString(),
                is(activity.getResources().getString(R.string.turbulent_quantities)));

    }

    @Test
    @Ignore
    public void clickOnGridCalculation() {
        onView(withId(R.id.btnGrid)).perform(click());

        Activity activity = getCurrentActivity();
        assertThat(activity.getTitle().toString(),
                is(activity.getResources().getString(R.string.grid_convergence)));

    }
}
