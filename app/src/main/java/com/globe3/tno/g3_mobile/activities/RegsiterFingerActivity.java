package com.globe3.tno.g3_mobile.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.adapters.RegisterFingerStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.fragments.RegisterFingerFragment;
import com.globe3.tno.g3_mobile.model.StaffdataRepo;
import com.globe3.tno.g3_mobile.util.FileUtility;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;

import java.util.ArrayList;

public class RegsiterFingerActivity extends BaseActivity {
    RegsiterFingerActivity registerFingerActivity;

    StaffFactory staffFactory;

    RegisterFingerFragment registerFingerFragment;

    Drawable upArrow;
    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    ArrayList<RowStaff> staff_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regsiter_finger);
        super.onCreate(savedInstanceState);
    }

    public void onActivityLoading(){
        try {
            registerFingerActivity = this;

            staffFactory = new StaffFactory(registerFingerActivity);

            recycler_staff_list = (RecyclerView) findViewById(R.id.recycler_staff_list);

            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                    upArrow = ContextCompat.getDrawable(registerFingerActivity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                    upArrow.setColorFilter(ContextCompat.getColor(registerFingerActivity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                }
            }

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
                        /*FragmentManager fragmentManager = getFragmentManager();
                        registerFingerFragment = new RegisterFingerFragment();
                        registerFingerFragment.setCancelable(false);
                        registerFingerFragment.show(fragmentManager, getString(R.string.label_register_finger));*/
                    }
                });
                staff_list.add(rowStaff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityReady(){
        recycler_staff_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new RegisterFingerStaffListAdapter(staff_list, registerFingerActivity);
        recycler_staff_list.setAdapter(recyclerViewAdapter);
    }

    public void startVerify(View view){
        //registerFingerFragment.startVerify();
    }

    public void startRegistration(View view){
        //registerFingerFragment.startRegistration();
    }

    public void simulate(View view){
        registerFingerFragment.simulate();
    }

    public void finishRegistration(View view){
        registerFingerFragment.finishRegistration();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
