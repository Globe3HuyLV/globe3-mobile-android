package com.globe3.tno.g3_mobile.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe3.tno.g3_mobile.adapters.PhotosStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Staff;
import com.globe3.tno.g3_mobile.app_objects.factory.StaffFactory;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;
import com.globe3.tno.g3_mobile.R;

import java.util.ArrayList;

public class PhotosStaffFragment extends Fragment {
    StaffFactory staffFactory;

    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    ArrayList<RowStaff> staff_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staffFactory = new StaffFactory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View staffFragment = inflater.inflate(R.layout.fragment_photos_staff_list, viewGroup, false);

        recycler_staff_list = (RecyclerView) staffFragment.findViewById(R.id.recycler_staff_list);

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

        recycler_staff_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(viewGroup.getContext());
        recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new PhotosStaffListAdapter(staff_list, viewGroup.getContext());
        recycler_staff_list.setAdapter(recyclerViewAdapter);

        return staffFragment;
    }
}
