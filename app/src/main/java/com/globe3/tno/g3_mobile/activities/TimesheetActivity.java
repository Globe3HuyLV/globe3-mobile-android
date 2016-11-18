package com.globe3.tno.g3_mobile.activities;

import android.app.SearchManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.TimesheetTabAdapter;
import com.globe3.tno.g3_mobile.fragments.TimesheetProjectFragment;
import com.globe3.tno.g3_mobile.fragments.TimesheetStaffFragment;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;

public class TimesheetActivity extends BaseActivity {
    TimesheetActivity timsheetActivity;

    ViewPager viewPager;
    TimesheetTabAdapter tabAdapter;

    SearchView.SearchAutoComplete search_box;

    int[] search_box_hint = {R.string.hint_search, R.string.hint_project_code};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_timesheet);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registerfinger, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_staff));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        search_box = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        ImageView search_close_btn = (ImageView) searchView.findViewById(R.id.search_close_btn);

        search_box.setHintTextColor(ActivityCompat.getColor(timsheetActivity, R.color.colorActionBarHint));
        search_box.setHint(getString(R.string.hint_search));
        search_box.setTextColor(ActivityCompat.getColor(timsheetActivity, R.color.colorBlackLight));
        search_close_btn.setColorFilter(ActivityCompat.getColor(timsheetActivity, R.color.colorBlackLight));

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

    public void onActivityLoading(){
        try {
            timsheetActivity = this;

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            upArrow.setColorFilter(ContextCompat.getColor(timsheetActivity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.label_staff)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.label_project)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            viewPager = (ViewPager) findViewById(R.id.tab_layout_pager);
            tabAdapter = new TimesheetTabAdapter(getSupportFragmentManager());
            viewPager.setAdapter(tabAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    search_box.setHint(getString(search_box_hint[tab.getPosition()]));
                    viewPager.setCurrentItem(tab.getPosition());
                    doSearch(search_box.getText().toString());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doSearch(String searchTerm){
        Fragment selectedFragment = tabAdapter.getRegisteredFragment(viewPager.getCurrentItem());

        if (viewPager.getCurrentItem() == 0 && selectedFragment != null) {
            ((TimesheetStaffFragment)selectedFragment).searchStaff(searchTerm.toString());
        }

        if (viewPager.getCurrentItem() == 1 && selectedFragment != null) {
            ((TimesheetProjectFragment)selectedFragment).searchProject(searchTerm.toString());
        }
    }

    public void onActivityReady(){
    }
}
