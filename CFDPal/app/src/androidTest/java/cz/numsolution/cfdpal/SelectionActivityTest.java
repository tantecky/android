package cz.numsolution.cfdpal;

import android.app.Activity;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.numsolution.cfdpal.view.SelectionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
    public void clickOnQuantitiesCalculation() {
        onView(withId(R.id.btnQuantities)).perform(click());

        Activity activity = getCurrentActivity();
        assertThat(activity.getTitle().toString(),
                is(activity.getResources().getString(R.string.turbulent_quantities)));

    }

    @Test
    public void clickOnGridCalculation() {
        onView(withId(R.id.btnGrid)).perform(click());

        Activity activity = getCurrentActivity();
        assertThat(activity.getTitle().toString(),
                is(activity.getResources().getString(R.string.grid_convergence)));

    }
}
