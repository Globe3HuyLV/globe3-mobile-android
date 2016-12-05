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
    PhotosActivity photos_activity;

    ActionBar action_bar;
    Drawable up_arrow;

    TabLayout tab_layout;
    ViewPager view_pager;
    PhotosTabAdapter photos_adapter;

    SearchView.SearchAutoComplete search_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_photos);
        super.onCreate(savedInstanceState);

        photos_activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        search_box = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        ImageView search_close_btn = (ImageView) searchView.findViewById(R.id.search_close_btn);

        search_box.setHintTextColor(ActivityCompat.getColor(photos_activity, R.color.colorActionBarHint));
        search_box.setHint(getString(R.string.hint_search));
        search_box.setTextColor(ActivityCompat.getColor(photos_activity, R.color.colorBlackLight));
        search_close_btn.setColorFilter(ActivityCompat.getColor(photos_activity, R.color.colorBlackLight));

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
        action_bar = getSupportActionBar();
        up_arrow = ContextCompat.getDrawable(photos_activity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        up_arrow.setColorFilter(ContextCompat.getColor(photos_activity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        view_pager = (ViewPager) findViewById(R.id.tab_layout_pager);
        photos_adapter = new PhotosTabAdapter(getSupportFragmentManager());
    }

    public void onActivityReady(){
        action_bar.setDisplayHomeAsUpEnabled(true);
        action_bar.setHomeAsUpIndicator(up_arrow);

        tab_layout.removeAllTabs();
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.label_project)));
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.label_staff)));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        view_pager.setAdapter(photos_adapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void finishStaffTakePhoto(){
        photos_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doSearch(search_box.getText().toString());
            }
        });
    }

    public void doSearch(String searchTerm){
        Fragment selectedFragment = photos_adapter.getRegisteredFragment(view_pager.getCurrentItem());

        if (view_pager.getCurrentItem() == 0 && selectedFragment != null) {
            ((PhotosProjectFragment)selectedFragment).searchProject(searchTerm.toString());
        }

        if (view_pager.getCurrentItem() == 1 && selectedFragment != null) {
            ((PhotosStaffFragment)selectedFragment).searchStaff(searchTerm.toString());
        }
    }
}
