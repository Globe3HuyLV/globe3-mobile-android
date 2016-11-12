package com.globe3.tno.g3_mobile.fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe3.tno.g3_mobile.adapters.TimesheetStaffListAdapter;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Staff;

import java.util.ArrayList;

public class TimesheetStaffFragment extends Fragment {
    FloatingActionButton fab_auto_screening;
    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    LogTimeAutoFragment logTimeAutoFragment;
    LogTimeFragment logTimeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View staffFragment = inflater.inflate(R.layout.fragment_timesheet_staff_list, viewGroup, false);

        fab_auto_screening = (FloatingActionButton) staffFragment.findViewById(R.id.fab_auto_screening);
        recycler_staff_list = (RecyclerView) staffFragment.findViewById(R.id.recycler_staff_list);

        fab_auto_screening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                logTimeAutoFragment = new LogTimeAutoFragment();
                logTimeAutoFragment.setCancelable(false);
                logTimeAutoFragment.show(fragmentManager, getString(R.string.label_log_time_auto));
            }
        });
        ArrayList<RowStaff> staff_list = new ArrayList<>();
        for(int i=1001;i<=1027;i++){
            final int n = i;
            RowStaff rowStaff = new RowStaff();
            rowStaff.setStaffCode(String.valueOf(i));
            rowStaff.setStaffName("Staff Name");
            rowStaff.setStaffFingerCount(i%3);
            rowStaff.setStaffPhoto(null);
            rowStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle staffBundle = new Bundle();

                    Staff staff = new Staff();
                    staff.setStaff_num(String.valueOf(n));
                    staff.setStaff_desc("Staff Name");

                    staffBundle.putSerializable("staff", staff);

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    logTimeFragment = new LogTimeFragment();
                    logTimeFragment.setCancelable(false);
                    logTimeFragment.setArguments(staffBundle);
                    logTimeFragment.show(fragmentManager, getString(R.string.label_log_time));
                }
            });
            staff_list.add(rowStaff);
        }

        recycler_staff_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(viewGroup.getContext());
        recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new TimesheetStaffListAdapter(staff_list, viewGroup.getContext());
        recycler_staff_list.setAdapter(recyclerViewAdapter);

        return staffFragment;
    }
}
