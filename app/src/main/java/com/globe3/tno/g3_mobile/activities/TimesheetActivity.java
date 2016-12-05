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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.TimesheetTabAdapter;
import com.globe3.tno.g3_mobile.fragments.TimesheetProjectFragment;
import com.globe3.tno.g3_mobile.fragments.TimesheetStaffFragment;

public class TimesheetActivity extends BaseActivity {
    TimesheetActivity timsheet_activity;

    ActionBar action_bar;
    Drawable up_arrow;

    TabLayout tab_layout;
    ViewPager view_pager;
    TimesheetTabAdapter tab_adapter;

    SearchView.SearchAutoComplete search_box;

    int[] search_box_hint = {R.string.hint_search, R.string.hint_project_code};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_timesheet);
        super.onCreate(savedInstanceState);

        timsheet_activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        search_box = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        ImageView search_close_btn = (ImageView) searchView.findViewById(R.id.search_close_btn);

        search_box.setHintTextColor(ActivityCompat.getColor(timsheet_activity, R.color.colorActionBarHint));
        search_box.setHint(getString(R.string.hint_search));
        search_box.setTextColor(ActivityCompat.getColor(timsheet_activity, R.color.colorBlackLight));
        search_close_btn.setColorFilter(ActivityCompat.getColor(timsheet_activity, R.color.colorBlackLight));

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

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        view_pager = (ViewPager) findViewById(R.id.tab_layout_pager);
        tab_adapter = new TimesheetTabAdapter(getSupportFragmentManager());
    }

    public void onActivityReady(){
        if(action_bar != null){
            action_bar.setDisplayHomeAsUpEnabled(true);
            up_arrow = ContextCompat.getDrawable(timsheet_activity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            up_arrow.setColorFilter(ContextCompat.getColor(timsheet_activity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
            action_bar.setHomeAsUpIndicator(up_arrow);
        }

        tab_layout.removeAllTabs();
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.label_staff)));
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.label_project)));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        view_pager.setAdapter(tab_adapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                search_box.setHint(getString(search_box_hint[tab.getPosition()]));
                view_pager.setCurrentItem(tab.getPosition());
                doSearch(search_box.getText().toString());
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
        Fragment selectedFragment = tab_adapter.getRegisteredFragment(view_pager.getCurrentItem());

        if (view_pager.getCurrentItem() == 0 && selectedFragment != null) {
            ((TimesheetStaffFragment)selectedFragment).searchStaff(searchTerm.toString());
        }

        if (view_pager.getCurrentItem() == 1 && selectedFragment != null) {
            ((TimesheetProjectFragment)selectedFragment).searchProject(searchTerm.toString());
        }
    }
}
