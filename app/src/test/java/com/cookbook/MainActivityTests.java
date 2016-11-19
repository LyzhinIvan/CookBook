package com.cookbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cookbook.fragments.CategoriesFragment;
import com.cookbook.fragments.SearchRecipeFragment;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTests {

    private final static int VISIBLE = 0;

    AppCompatActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void frameIsVisible() {
        View frame = activity.findViewById(R.id.frame_layout);
        assertThat(frame.getVisibility(), is(VISIBLE));
    }


    @Test
    public void activityMenuItems() {
        View menuSearch = activity.findViewById(R.id.action_search);
        assertThat(menuSearch.getVisibility(), is(VISIBLE));
    }

    @Test
    public void checkDefaultFragment() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentByTag("currentFragment");
        assertThat(currentFragment, is(CoreMatchers.instanceOf(CategoriesFragment.class)));
    }

    @Test
    public void searchClickTest() {
        View menuSearch = activity.findViewById(R.id.action_search);
        menuSearch.performClick();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentByTag("currentFragment");
        assertThat(currentFragment, is(CoreMatchers.instanceOf(SearchRecipeFragment.class)));
    }
}
