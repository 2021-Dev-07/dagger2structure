package cof.gof.dagger2structure.MainActivity;

import android.content.Intent;
import android.support.test.espresso.core.deps.guava.base.Verify;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cof.gof.dagger2structure.R;
import cof.gof.dagger2structure.mainActivity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class TestInstrumentMainActivity {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method



    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addition_isCorrect() throws Exception {

        activityRule.launchActivity(new Intent());
        MainActivity mainActivity = activityRule.getActivity();

        assertEquals("flumbolate test", mainActivity.flumbolate());
    }

    @Test
    public void isViewShowing() throws  Exception{
        activityRule.launchActivity(new Intent());
        onView(withId(R.id.textView)).check(matches(withText("flumbolate test")));
    }


}
