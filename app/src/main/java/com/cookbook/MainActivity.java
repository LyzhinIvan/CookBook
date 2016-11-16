package com.cookbook;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cookbook.fragments.CategoriesFragment;
import com.cookbook.fragments.RecipesListFragment;
import com.cookbook.fragments.SearchRecipeFragment;
import com.cookbook.fragments.ShopingListFragment;
import com.cookbook.fragments.UpdateDatabaseFragment;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "CookBook";
    CategoriesFragment categoriesFragment = new CategoriesFragment();
    SearchRecipeFragment searchRecipeFragment = new SearchRecipeFragment();
    ShopingListFragment shopingListFragment = new ShopingListFragment();
    UpdateDatabaseFragment updateDatabaseFragment = new UpdateDatabaseFragment();

    Fragment currentFragment;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout mDrawerLayout;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        initDrawer(toolbar);
        currentFragment = categoriesFragment;
        setFragment(categoriesFragment, false);
    }

    public void setFragment(Fragment fragment, boolean backEnabled) {
        FragmentTransaction fTrans = fragmentManager.beginTransaction();
        fTrans = fTrans.replace(R.id.frame_layout, fragment, "currentFragment");
        if (backEnabled) {
            fTrans = fTrans.addToBackStack(null);
        } else {
            clearBackStack();
        }
        fTrans.commit();
    }

    private void clearBackStack() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private void initDrawer(Toolbar toolbar) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                InputMethodManager inputMethodManager = (InputMethodManager)  MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
        };


        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_categories);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_serach) {
            setFragment(searchRecipeFragment, false);
        } else if (id == R.id.nav_categories) {
            setFragment(categoriesFragment, false);
        } else if (id == R.id.nav_favorite) {
            RecipesListFragment favRecipes = RecipesListFragment.newInstance("Любимые рецепты");
            setFragment(favRecipes, false);
        } else if (id == R.id.nav_shop_list) {
            setFragment(shopingListFragment, false);
        } else if (id == R.id.nav_update) {
            setFragment(updateDatabaseFragment, false);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackStackChanged() {
        //устанавливаем текущим верхий фрагмент из стека
        currentFragment = fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount());
        if (fragmentManager.getBackStackEntryCount() != 0) {
            drawerToggle.setDrawerIndicatorEnabled(false);
            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_back);
            drawerToggle.setHomeAsUpIndicator(drawable);
            drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });
        }
    }
}
