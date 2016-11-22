package com.globe3.tno.g3_mobile.activities;

import android.app.SearchManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.PhotosTabAdapter;
import com.globe3.tno.g3_mobile.fragments.PhotosProjectFragment;
import com.globe3.tno.g3_mobile.fragments.PhotosStaffFragment;

public class PhotosActivity extends BaseActivity {
    PhotosActivity photosActivity;

    ActionBar actionBar;
    Drawable upArrow;

    TabLayout tabLayout;
    ViewPager viewPager;
    PhotosTabAdapter photosAdapter;

    SearchView.SearchAutoComplete search_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_photos);
        super.onCreate(savedInstanceState);

        photosActivity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        search_box = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        ImageView search_close_btn = (ImageView) searchView.findViewById(R.id.search_close_btn);

        search_box.setHintTextColor(ActivityCompat.getColor(photosActivity, R.color.colorActionBarHint));
        search_box.setHint(getString(R.string.hint_search));
        search_box.setTextColor(ActivityCompat.getColor(photosActivity, R.color.colorBlackLight));
        search_close_btn.setColorFilter(ActivityCompat.getColor(photosActivity, R.color.colorBlackLight));

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable searchTerm) {
                doSearch(searchTerm.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void onActivityLoading(){
        actionBar = getSupportActionBar();
        upArrow = ContextCompat.getDrawable(photosActivity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(photosActivity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.tab_layout_pager);
        photosAdapter = new PhotosTabAdapter(getSupportFragmentManager());
    }

    public void onActivityReady(){
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(upArrow);

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.label_project)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.label_staff)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(photosAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void doSearch(String searchTerm){
        Fragment selectedFragment = photosAdapter.getRegisteredFragment(viewPager.getCurrentItem());

        if (viewPager.getCurrentItem() == 0 && selectedFragment != null) {
            ((PhotosProjectFragment)selectedFragment).searchProject(searchTerm.toString());
        }

        if (viewPager.getCurrentItem() == 1 && selectedFragment != null) {
            ((PhotosStaffFragment)selectedFragment).searchStaff(searchTerm.toString());
        }
    }
}
