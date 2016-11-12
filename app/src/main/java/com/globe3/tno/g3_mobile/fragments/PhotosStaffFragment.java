package com.globe3.tno.g3_mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe3.tno.g3_mobile.adapters.PhotosStaffListAdapter;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;
import com.globe3.tno.g3_mobile.R;

import java.util.ArrayList;

public class PhotosStaffFragment extends Fragment {
    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View staffFragment = inflater.inflate(R.layout.fragment_photos_staff_list, viewGroup, false);

        recycler_staff_list = (RecyclerView) staffFragment.findViewById(R.id.recycler_staff_list);

        ArrayList<RowStaff> staff_list = new ArrayList<>();
        for(int i=1001;i<=1027;i++){
            RowStaff rowStaff = new RowStaff();
            rowStaff.setStaffCode(String.valueOf(i));
            rowStaff.setStaffName("Staff Name");
            rowStaff.setStaffFingerCount(i%3);
            rowStaff.setStaffPhoto(null);

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
