package com.cookbook;

import android.support.v4.app.FragmentActivity;

import com.cookbook.fragments.CategoriesFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CategoriesFragmentTests {

    private final static int VISIBLE = 0;

    private CategoriesFragment fragment;
    private FragmentActivity activity;

    @Before
    public void setUp() {
        fragment = new CategoriesFragment();
        SupportFragmentTestUtil.startFragment(fragment);
        activity = fragment.getActivity();

    }

    @Test
    public void activityTitleCheck() {
        assertThat(activity.getTitle().toString(), is("Категории"));
    }

    /*@Test
    public void recyclerViewVisible() {
        View recycler = fragment.getView().findViewById(R.id.recyclerView);
        assertThat(recycler.getVisibility(), is(VISIBLE));
    }

    @Test
    public void recyclerAdapter() {
        RecyclerView recycler = (RecyclerView)fragment.getView().findViewById(R.id.recyclerView);
        assertThat(recycler.getAdapter(), is(CoreMatchers.instanceOf(CategoriesGridAdapter.class)));
    }

    @Test
    public void recyclerIsEmpty() {
        RecyclerView recycler = (RecyclerView)fragment.getView().findViewById(R.id.recyclerView);
        assertThat(recycler.getAdapter().getItemCount(), is(0));
    }*/

}
