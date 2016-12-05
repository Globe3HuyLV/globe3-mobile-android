package com.globe3.tno.g3_mobile.activities;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.RegisterFingerStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;

import com.globe3.tno.g3_mobile.fragments.SelectFingerFragment;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;

import java.util.ArrayList;

public class RegisterFingerActivity extends BaseActivity {
    RegisterFingerActivity register_finger_activity;

    AuditFactory audit_factory;
    StaffFactory staff_factory;

    SelectFingerFragment select_finger_fragment;

    ActionBar action_bar;
    Drawable up_arrow;

    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recycler_view_adapter;
    RecyclerView.LayoutManager recycler_view_layout_manager;

    RelativeLayout rl_search_loader;
    ImageView iv_search_loader;
    RelativeLayout rl_no_record;

    ArrayList<RowStaff> staff_list;

    SearchView.SearchAutoComplete search_box;
    SearchStaff search_staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regsiter_finger);
        super.onCreate(savedInstanceState);

        register_finger_activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        search_box = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        ImageView search_close_btn = (ImageView) searchView.findViewById(R.id.search_close_btn);

        search_box.setHintTextColor(ActivityCompat.getColor(register_finger_activity, R.color.colorActionBarHint));
        search_box.setHint(getString(R.string.hint_search));
        search_box.setTextColor(ActivityCompat.getColor(register_finger_activity, R.color.colorBlackLight));
        search_close_btn.setColorFilter(ActivityCompat.getColor(register_finger_activity, R.color.colorBlackLight));

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable searchTerm) {
                register_finger_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(search_staff !=null){
                            search_staff.cancel(true);
                        }
                        search_staff = new SearchStaff(searchTerm.toString());
                        search_staff.execute();
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
        audit_factory = new AuditFactory(register_finger_activity);
        staff_factory = new StaffFactory(register_finger_activity);

        recycler_staff_list = (RecyclerView) findViewById(R.id.recycler_staff_list);
        rl_search_loader = (RelativeLayout) findViewById(R.id.rl_search_loader);
        iv_search_loader = (ImageView) findViewById(R.id.iv_search_loader);
        rl_no_record = (RelativeLayout) findViewById(R.id.rl_no_record);

        action_bar = getSupportActionBar();

        staff_list = new ArrayList<>();
        for(final Staff staff : staff_factory.getActiveStaffs()){
            staff_list.add(createRowStaff(staff));
        }
    }

    public void onActivityReady(){
        if(action_bar != null){
            action_bar.setDisplayHomeAsUpEnabled(true);
            if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                up_arrow = ContextCompat.getDrawable(register_finger_activity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                up_arrow.setColorFilter(ContextCompat.getColor(register_finger_activity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                action_bar.setHomeAsUpIndicator(up_arrow);
            }
        }

        iv_search_loader.setAnimation(AnimationUtils.loadAnimation(register_finger_activity, R.anim.animate_rotate_clockwise));

        recycler_staff_list.setHasFixedSize(true);

        recycler_view_layout_manager = new LinearLayoutManager(register_finger_activity);
        recycler_staff_list.setLayoutManager(recycler_view_layout_manager);

        recycler_view_adapter = new RegisterFingerStaffListAdapter(staff_list, register_finger_activity);
        recycler_staff_list.setAdapter(recycler_view_adapter);
    }

    private RowStaff createRowStaff(final Staff staff){
        RowStaff rowStaff = new RowStaff();
        rowStaff.setStaffCode(staff.getStaff_num());
        rowStaff.setStaffName(staff.getStaff_desc());

        int staff_finger_count = 0;
        staff_finger_count += (staff.getFingerprint_image1()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image2()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image3()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image4()==null?0:1);
        staff_finger_count += (staff.getFingerprint_image5()==null?0:1);

        rowStaff.setStaffFingerCount(staff_finger_count);

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
                FragmentManager fragmentManager = getFragmentManager();

                select_finger_fragment = new SelectFingerFragment();
                select_finger_fragment.setCancelable(false);
                select_finger_fragment.setStaff(staff);
                select_finger_fragment.setAuditFactory(audit_factory);
                select_finger_fragment.setStaffFactory(staff_factory);
                select_finger_fragment.show(fragmentManager, getString(R.string.label_register_finger));
            }
        });

        return rowStaff;
    }

    public void startRegister(View view){
        select_finger_fragment.startRegister();
    }

    public void cancelSelectFragment(View view){
        if(select_finger_fragment != null){
            select_finger_fragment.dismiss();
            select_finger_fragment = null;
        }
    }

    public void finishRegistration(){
        register_finger_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(search_staff !=null){
                    search_staff.cancel(true);
                }
                search_staff = new SearchStaff(search_box.getText().toString());
                search_staff.execute();
            }
        });
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
            for(final Staff staff : (searchTerm.equals("")? staff_factory.getActiveStaffs(): staff_factory.searchStaffs(searchTerm))){
                staff_list.add(createRowStaff(staff));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recycler_staff_list.setHasFixedSize(true);

            recycler_view_layout_manager = new LinearLayoutManager(register_finger_activity);
            recycler_staff_list.setLayoutManager(recycler_view_layout_manager);

            recycler_view_adapter = new RegisterFingerStaffListAdapter(staff_list, register_finger_activity);
            recycler_staff_list.setAdapter(recycler_view_adapter);

            recycler_staff_list.setVisibility(staff_list.size()==0?View.GONE:View.VISIBLE);
            rl_search_loader.setVisibility(View.GONE);
            rl_no_record.setVisibility(staff_list.size()==0?View.VISIBLE:View.GONE);
        }
    }
}
