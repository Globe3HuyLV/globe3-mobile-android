package com.globe3.tno.g3_mobile.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.globe3.tno.g3_mobile.adapters.LogTimeStaffListAdapter;
import com.globe3.tno.g3_mobile.app_objects.Project;
import com.globe3.tno.g3_mobile.view_objects.RowStaff;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Staff;

import java.util.ArrayList;

public class LogTimeStaffFragment extends DialogFragment {
    Context parentContext;

    Project project;

    RecyclerView recycler_staff_list;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    TextView tv_project_code;
    TextView tv_project_desc;
    TextView tv_auto_screening;
    TextView tv_cancel;

    LogTimeAutoFragment logTimeAutoFragment;
    LogTimeFragment logTimeFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View logTimeStaffFragment = inflater.inflate(R.layout.fragment_log_time_staff, viewGroup, false);
        parentContext = logTimeStaffFragment.getContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if(getArguments() != null && getArguments().containsKey("entproject")){
            project = (Project) getArguments().getSerializable("entproject");

            tv_project_code = (TextView) logTimeStaffFragment.findViewById(R.id.tv_project_code);
            tv_project_code.setText(project.getCode());

            tv_project_desc = (TextView) logTimeStaffFragment.findViewById(R.id.tv_project_desc);
            tv_project_desc.setText(project.getDesc());
        }

        recycler_staff_list = (RecyclerView) logTimeStaffFragment.findViewById(R.id.recycler_staff_list);
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
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    logTimeFragment = new LogTimeFragment();
                    logTimeFragment.setCancelable(false);

                    if(project != null){
                        Bundle logTimeBundle = new Bundle();
                        logTimeBundle.putSerializable("entproject", project);

                        Staff staff = new Staff();
                        staff.setStaff_num(String.valueOf(n));
                        staff.setStaff_desc("Staff Name");

                        logTimeBundle.putSerializable("staff", staff);

                        logTimeFragment.setArguments(logTimeBundle);
                    }

                    dismiss();
                    logTimeFragment.show(fragmentManager, getString(R.string.label_log_time));
                }
            });
            staff_list.add(rowStaff);
        }

        recycler_staff_list.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recycler_staff_list.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new LogTimeStaffListAdapter(staff_list, getActivity());
        recycler_staff_list.setAdapter(recyclerViewAdapter);

        tv_auto_screening = (TextView) logTimeStaffFragment.findViewById(R.id.tv_auto_screening);
        tv_auto_screening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                logTimeAutoFragment = new LogTimeAutoFragment();
                logTimeAutoFragment.setCancelable(false);

                if(project != null){
                    Bundle projectBundle = new Bundle();
                    projectBundle.putSerializable("entproject", project);
                    logTimeAutoFragment.setArguments(projectBundle);
                }

                dismiss();
                logTimeAutoFragment.show(fragmentManager, getString(R.string.label_log_time_auto));
            }
        });

        tv_cancel = (TextView) logTimeStaffFragment.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return logTimeStaffFragment;
    }
}
