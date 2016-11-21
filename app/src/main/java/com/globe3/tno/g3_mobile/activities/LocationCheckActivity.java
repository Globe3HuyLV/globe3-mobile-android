package com.globe3.tno.g3_mobile.activities;

import android.app.SearchManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.LocationCheckStaffListAdapter;
import com.globe3.tno.g3_mobile.adapters.RegisterFingerStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;

import java.util.ArrayList;

public class LocationCheckActivity extends BaseActivity {
    LocationCheckActivity locationCheckActivity;

    StaffFactory staffFactory;

    ActionBar actionBar;
    Drawable upArrow;

    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;

    ArrayList<RowStaff> staff_list;

    SearchStaff searchStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_location_check);
        super.onCreate(savedInstanceState);

        locationCheckActivity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registerfinger, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.SearchAutoComplete search_box = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        ImageView search_close_btn = (ImageView) searchView.findViewById(R.id.search_close_btn);

        search_box.setHintTextColor(ActivityCompat.getColor(locationCheckActivity, R.color.colorActionBarHint));
        search_box.setHint(getString(R.string.hint_search));
        search_box.setTextColor(ActivityCompat.getColor(locationCheckActivity, R.color.colorBlackLight));
        search_close_btn.setColorFilter(ActivityCompat.getColor(locationCheckActivity, R.color.colorBlackLight));

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable searchTerm) {
                locationCheckActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(searchStaff!=null){
                            searchStaff.cancel(true);
                        }
                        searchStaff = new SearchStaff(searchTerm.toString());
                        searchStaff.execute();
                    }
                });
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
        staffFactory = new StaffFactory(locationCheckActivity);

        recycler_staff_list = (RecyclerView) findViewById(R.id.recycler_staff_list);
        rl_search_loader = (RelativeLayout) findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) findViewById(R.id.iv_search_loader);

        actionBar = getSupportActionBar();

        staff_list = new ArrayList<>();
        for(Staff staff : staffFactory.getActiveStaffs()){
            RowStaff rowStaff = new RowStaff();
            rowStaff.setStaffCode(staff.getStaff_num());
            rowStaff.setStaffName(staff.getStaff_desc());
            rowStaff.setStaffFingerCount((staff.getFingerprint_image1()==null?0:1)+(staff.getFingerprint_image2()==null?0:1));

            if(staff.getPhoto1()!=null){
                Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

                int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

                rowStaff.setStaffPhoto(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
            }else{
                rowStaff.setStaffPhoto(null);
            }

            rowStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            staff_list.add(rowStaff);
        }
    }

    public void onActivityReady(){
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                upArrow = ContextCompat.getDrawable(locationCheckActivity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                upArrow.setColorFilter(ContextCompat.getColor(locationCheckActivity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        iv_search_loader.setAnimation(AnimationUtils.loadAnimation(locationCheckActivity, R.anim.animate_rotate_clockwise));

        recycler_staff_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(locationCheckActivity);
        recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new LocationCheckStaffListAdapter(staff_list, locationCheckActivity);
        recycler_staff_list.setAdapter(recyclerViewAdapter);
    }


    public class SearchStaff extends AsyncTask<Void, Void, Void>
    {
        String searchTerm;

        public SearchStaff(String searchTerm){
            this.searchTerm = searchTerm;
            staff_list.clear();
        }

        @Override
        protected  void onPreExecute()
        {
            recycler_staff_list.setVisibility(View.GONE);
            rl_search_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... param) {
            for(Staff staff : (searchTerm.equals("")?staffFactory.getActiveStaffs():staffFactory.searchStaffs(searchTerm))){
                RowStaff rowStaff = new RowStaff();
                rowStaff.setStaffCode(staff.getStaff_num());
                rowStaff.setStaffName(staff.getStaff_desc());
                rowStaff.setStaffFingerCount((staff.getFingerprint_image1()==null?0:1)+(staff.getFingerprint_image2()==null?0:1));

                if(staff.getPhoto1()!=null){
                    Bitmap staffPhoto = BitmapFactory.decodeByteArray(staff.getPhoto1(), 0, staff.getPhoto1().length);

                    int newSize = staffPhoto.getWidth() < staffPhoto.getHeight() ? staffPhoto.getWidth() : staffPhoto.getHeight();

                    rowStaff.setStaffPhoto(Bitmap.createBitmap(staffPhoto, 0, 0, newSize, newSize));
                }else{
                    rowStaff.setStaffPhoto(null);
                }

                rowStaff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*FragmentManager fragmentManager = getFragmentManager();
                        registerFingerFragment = new RegisterFingerFragment();
                        registerFingerFragment.setCancelable(false);
                        registerFingerFragment.show(fragmentManager, getString(R.string.label_register_finger));*/
                    }
                });
                staff_list.add(rowStaff);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_staff_list.setHasFixedSize(true);

            recyclerViewLayoutManager = new LinearLayoutManager(locationCheckActivity);
            recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

            recyclerViewAdapter = new RegisterFingerStaffListAdapter(staff_list, locationCheckActivity);
            recycler_staff_list.setAdapter(recyclerViewAdapter);

            recycler_staff_list.setVisibility(View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
        }
    }
}
