package com.globe3.tno.g3_mobile.activities;

import android.app.Activity;
import android.app.FragmentManager;
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
import com.globe3.tno.g3_mobile.fragments.RegisterFingerFragment;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;

import java.util.ArrayList;

public class RegsiterFingerActivity extends BaseActivity {
    Activity registerFingerActivity;

    RegisterFingerFragment registerFingerFragment;

    Drawable upArrow;
    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regsiter_finger);
        super.onCreate(savedInstanceState);
    }

    public void onActivityLoading(){
        Thread thread = new Thread();
        try {
            registerFingerActivity = this;

            recycler_staff_list = (RecyclerView) findViewById(R.id.recycler_staff_list);

            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if(getResources().getResourceName(R.drawable.abc_ic_ab_back_mtrl_am_alpha) != null){
                    upArrow = ContextCompat.getDrawable(registerFingerActivity, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                    upArrow.setColorFilter(ContextCompat.getColor(registerFingerActivity, R.color.colorMenuDark), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                }
            }

            ArrayList<RowStaff> staff_list = new ArrayList<>();
            for(int i=1001;i<=1027;i++){
                RowStaff rowStaff = new RowStaff();
                rowStaff.setStaffCode(String.valueOf(i));
                rowStaff.setStaffName("Staff Name");
                rowStaff.setStaffFingerCount(i%3);
                rowStaff.setStaffPhoto(null);
                rowStaff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = getFragmentManager();
                        registerFingerFragment = new RegisterFingerFragment();
                        registerFingerFragment.setCancelable(false);
                        registerFingerFragment.show(fragmentManager, getString(R.string.label_register_finger));
                    }
                });
                staff_list.add(rowStaff);
            }

            recycler_staff_list.setHasFixedSize(true);

            recyclerViewLayoutManager = new LinearLayoutManager(this);
            recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

            recyclerViewAdapter = new RegisterFingerStaffListAdapter(staff_list, registerFingerActivity);
            recycler_staff_list.setAdapter(recyclerViewAdapter);

            thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onActivityReady(){
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
