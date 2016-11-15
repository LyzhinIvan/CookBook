package com.cookbook;

import android.app.Activity;
import android.view.View;

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

    Activity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void FrameIsVisible() {
        View frame = activity.findViewById(R.id.frame_layout);
        assertThat(frame.getVisibility(), is(VISIBLE));
    }

    @Test
    public void ActivityTitle() {
        assertThat(activity.getTitle().toString(), is("Категории"));
    }

    @Test
    public void ActivityMenu() {
        View menuSearch = activity.findViewById(R.id.action_search);
        assertThat(menuSearch.getVisibility(), is(VISIBLE));
    }
}
