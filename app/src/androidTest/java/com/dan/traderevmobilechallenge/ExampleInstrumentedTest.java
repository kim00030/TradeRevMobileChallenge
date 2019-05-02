package com.dan.traderevmobilechallenge;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.view.fragments.GridViewFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * To run test, you can click errow indicator aligned on each test method for testing separately
 * or click the indicator aligned on class name for all in one test
 *
 * OR click on Run/debug button on main menu with selecting testing app
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Before
    public void setUp() {

        mainActivity = activityActivityTestRule.getActivity();
    }

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mainActivity;
    private Random random = new Random();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.dan.traderevmobilechallenge", appContext.getPackageName());
    }

    /**
     * Test for launch
     */
    @Test
    public void launchTest() {

        // check if container for loading Grid View fragment is null or not
        FrameLayout frameLayout = mainActivity.findViewById(R.id.fragment_container);
        assertNotNull(frameLayout);

        // add GridView fragment
        GridViewFragment gridViewFragment = new GridViewFragment();

        mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(frameLayout.getId(), gridViewFragment)
                .commitAllowingStateLoss();
        // give a little of time for loading fragment
        getInstrumentation().waitForIdleSync();
        // check if recyclerView in the fragment is null or not
        View view = gridViewFragment.getView().findViewById(R.id.recycler_view);
        assertNotNull(view);
    }

    /**
     * Test for scrolling to end of page and back to start
     */
    @Test
    public void scrollRecyclerView() {

        performIdle(5000);
        // Check if recycler view is displayed
        Espresso.onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(isDisplayed()));

        // To get Adaptor to get number of items presented in recycler view
        Fragment fragment = mainActivity.getSupportFragmentManager().findFragmentByTag(GridViewFragment.class.getSimpleName());
        GridViewFragment gridViewFragment = null;
        if (fragment instanceof GridViewFragment) {

            gridViewFragment = (GridViewFragment) fragment;
        }
        View view = gridViewFragment.getView();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        // scroll to end
        Espresso.onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                .scrollToPosition(recyclerView.getAdapter().getItemCount()-1));
        performIdle(500);
        //scroll to start
        Espresso.onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(0));
        performIdle(1000);

    }

    /**
     * Test for scrolling to a given random position among items loaded and click it to full screen
     */
    @Test
    public void scrollToPositionInRecyclerViewAndClickTheItem() {

        performIdle(5000);
        // Check if recycler view is displayed
        Espresso.onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(isDisplayed()));

        // To get Adaptor to get number of items presented in recycler view
        Fragment fragment = mainActivity.getSupportFragmentManager().findFragmentByTag(GridViewFragment.class.getSimpleName());
        GridViewFragment gridViewFragment = null;
        if (fragment instanceof GridViewFragment) {

            gridViewFragment = (GridViewFragment) fragment;
        }
        View view = gridViewFragment.getView();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        // Get random position
        int numOfItems = recyclerView.getAdapter().getItemCount();
        int randomPosition = (int) (Math.random() * ( numOfItems - 1 ));
        // check if recycler view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(ViewAssertions.matches(isDisplayed()));
        // click the item at the given position and click and see if the full screen presents
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(randomPosition-1, ViewActions.click()));
        performIdle(3000);
    }

    /**
     * Test for swapping photos in full screen and clicking button to
     * check if photo info is hide /show on toggle action
     */
    @Test
    public void swappingPhotosInFullScreen() {

        // go 100th position
        int targetToGo = 100;
        performIdle(5000);
        onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(targetToGo, scrollTo()));

        //open the item at 100th position
        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(targetToGo, ViewActions.click()));
        // check viewPager is displayed
        onView(ViewMatchers.withId(R.id.view_pager)).check(ViewAssertions.matches(isDisplayed()));

        //swap to right
        int untilWhichPosition = 130;
        for (int i = targetToGo; i < untilWhichPosition; i++) {

            onView(withId(R.id.view_pager)).perform(swipeRight());
            // check if Floating button and TextField are displayed
            onView(withId(R.id.fb_btn)).check(ViewAssertions.matches(isDisplayed()));
            onView(withId(R.id.tv_photo_info)).check(ViewAssertions.matches(isDisplayed()));
            // click floating button
            onView(withId(R.id.fb_btn)).perform(ViewActions.click());
            performIdle(50);
            // click floating button
            onView(withId(R.id.fb_btn)).perform(ViewActions.click());
            // turn it on again. TextView should be displayed
            onView(withId(R.id.tv_photo_info)).check(ViewAssertions.matches(isDisplayed()));
            performIdle(400);
        }
        //swap to left
        for (int i = untilWhichPosition - 1; i < 145; i++) {

            onView(withId(R.id.view_pager)).perform(swipeLeft());
            onView(withId(R.id.fb_btn)).check(ViewAssertions.matches(isDisplayed()));
            onView(withId(R.id.tv_photo_info)).check(ViewAssertions.matches(isDisplayed()));

            onView(withId(R.id.fb_btn)).perform(ViewActions.click());
            performIdle(50);

            onView(withId(R.id.fb_btn)).perform(ViewActions.click());
            // turn it on again
            onView(withId(R.id.tv_photo_info)).check(ViewAssertions.matches(isDisplayed()));

            performIdle(400);
        }
    }

    private void performIdle(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        mainActivity = null;
    }
}
